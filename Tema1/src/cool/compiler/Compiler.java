package cool.compiler;

import com.sun.jdi.Field;
import cool.tree.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//import cool.lexer.*;
import cool.parser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);
            
            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);
                
            /*
            // Test lexer only.
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });
            */
            
            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);
            
            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;
                
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                                        line + ":" + (charPositionInLine + 1) + ", ";
                    
                    Token token = (Token)offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;
                    
                    System.err.println(newMsg);
                    errors = true;
                }
            };
            
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            
            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));
                    
            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }
            
            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;
        }

        // Stop before semantic analysis phase, in case errors occurred.
        if (lexicalSyntaxErrors) {
            System.err.println("Compilation halted");
            return;
        }

        var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>() {

            @Override
            public ASTNode visitInt(CoolParser.IntContext ctx) {
                return new RfInt(ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitString(CoolParser.StringContext ctx) {
                return new RfString(ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitBool_expr(CoolParser.Bool_exprContext ctx) {
                return new RfBool(ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new RfId(ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                return new RfArgument(ctx.ID().getSymbol().getText(), ctx.TYPE().getSymbol().getText(), ctx.start);
            }

            /**
             * ID LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPE LBRACE expr RBRACE
             */
            @Override
            public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                List<RfArgument> args = new ArrayList<>();
                ctx.formal().forEach(arg -> args.add((RfArgument) visit(arg)));

                CoolParser.ExprContext methodBody = ctx.expr();
                RfExpression rfMethodBody = (RfExpression) visit(methodBody);

                return new RfMethod(ctx.ID().getSymbol().getText(), ctx.TYPE().getSymbol().getText(), args, rfMethodBody, ctx.start);
            }

            /**
             * ID COLON TYPE (ASSIGN expr)?
             */
            @Override
            public ASTNode visitField(CoolParser.FieldContext ctx) {
                TerminalNode fieldName = ctx.ID();
                TerminalNode fieldType = ctx.TYPE();

                CoolParser.ExprContext expr = ctx.expr();
                RfExpression rfExpression = expr != null ? (RfExpression) visit(expr) : null;

                return new RfField(fieldName.getSymbol().getText(), fieldType.getSymbol().getText(), rfExpression, ctx.start);
            }

            /**
             * class : CLASS TYPE (INHERITS TYPE)? LBRACE (feature SEMI)* RBRACE ;
             */
            @Override
            public ASTNode visitClass(CoolParser.ClassContext ctx) {
                List<RfFeature> rfFeatures = new ArrayList<>();
                for (CoolParser.FeatureContext feature : ctx.feature())
                    rfFeatures.add((RfFeature) visit(feature));
                return new RfClass(ctx.TYPE(), rfFeatures, ctx.start);
            }

            /**
             * program : (class SEMI)+ EOF ;
             */
            @Override
            public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                List<CoolParser.ClassContext> classContexts = ctx.class_();
                List<RfClass> rfClasses = new ArrayList<>();
                for(CoolParser.ClassContext antlrClass : classContexts)
                    rfClasses.add((RfClass) visit(antlrClass));

                return new RfProgram(rfClasses, ctx.start);
            }
        };

        var ast = astConstructionVisitor.visit(globalTree);

        var printVisitor = new ASTVisitor<Void>() {

            int indent = 0;

            @Override
            public Void visit(RfProgram rfProgram) {
                printIndent("program");
                indent++;
                rfProgram.getRfClasses().forEach(rfClass -> rfClass.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfClass rfClass) {
                printIndent("class");
                indent++;
                rfClass.getTypes().forEach(rfType -> { printIndent(rfType.getSymbol().getText()); });
                rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfField rfField) {
                printIndent("attribute");
                indent++;
                printIndent(rfField.getFieldName());
                printIndent(rfField.getFieldType());
                RfExpression rfExpression = rfField.getRfExpression();
                if (rfExpression != null)
                    rfExpression.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfInt rfInt) {
                printIndent(rfInt.getValue().toString());
                return null;
            }

            @Override
            public Void visit(RfString rfString) {
                String value = rfString.getValue();
                if (value.startsWith("\"") && value.endsWith("\""))
                    value = value.substring(1, value.length() - 1);
                printIndent(value);
                return null;
            }

            @Override
            public Void visit(RfBool rfBool) {
                printIndent(rfBool.getValue().toString());
                return null;
            }

            @Override
            public Void visit(RfId rfId) {
                printIndent(rfId.getValue());
                return null;
            }

            @Override
            public Void visit(RfArgument rfArgument) {
                printIndent("formal");
                indent++;
                printIndent(rfArgument.getName());
                printIndent(rfArgument.getType());
                indent--;
                return null;
            }

            @Override
            public Void visit(RfMethod rfMethod) {
                printIndent("method");
                indent++;
                printIndent(rfMethod.getName());

                List<RfArgument> rfArgs = rfMethod.getArgs();
                rfArgs.forEach(arg -> arg.accept(this));

                printIndent(rfMethod.getReturnType());

                RfExpression rfMethodBody = rfMethod.getRfMethodBody();
                if (rfMethodBody != null)
                    rfMethodBody.accept(this);

                indent--;
                return null;
            }

            void printIndent(String str) {
                System.out.println("  ".repeat(Math.max(0, indent)) + str);
            }
        };

        ast.accept(printVisitor);
    }
}
