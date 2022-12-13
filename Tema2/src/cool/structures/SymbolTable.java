package cool.structures;

import java.io.File;
import java.lang.reflect.Type;

import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import jdk.jshell.TypeDeclSnippet;
import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;
        
        globals.add(TypeSymbolConstants.OBJECT);
        globals.add(TypeSymbolConstants.INT);
        globals.add(TypeSymbolConstants.STRING);
        globals.add(TypeSymbolConstants.BOOL);
        globals.add(TypeSymbolConstants.IO);
        globals.add(TypeSymbolConstants.SELF_TYPE);

        implicitDefinitionOfObjectMethods();
        implicitDefinitionOfIOMethods();
        implicitDefinitionOfStringMethods();
    }

    private static void implicitDefinitionOfStringMethods() {
        // concat definition

        MethodSymbol concat = new MethodSymbol("concat", TypeSymbolConstants.STRING_STR, TypeSymbolConstants.STRING);
        concat.setReturnTypeSymbol(TypeSymbolConstants.STRING);
        concat.setResolved(true);

        IdSymbol xString = new IdSymbol("x", TypeSymbolConstants.STRING_STR);
        xString.setTypeSymbol(TypeSymbolConstants.STRING);

        concat.add(xString);

        TypeSymbolConstants.STRING.add(concat);

        // length definition

        MethodSymbol length = new MethodSymbol("length", TypeSymbolConstants.INT_STR, TypeSymbolConstants.STRING);
        length.setReturnTypeSymbol(TypeSymbolConstants.INT);
        length.setResolved(true);

        TypeSymbolConstants.STRING.add(length);

        MethodSymbol substr = new MethodSymbol("substr", TypeSymbolConstants.STRING_STR, TypeSymbolConstants.STRING);
        substr.setReturnTypeSymbol(TypeSymbolConstants.STRING);
        substr.setResolved(true);

        IdSymbol index = new IdSymbol("index", TypeSymbolConstants.INT_STR);
        index.setTypeSymbol(TypeSymbolConstants.INT);
        index.setResolved(true);

        substr.add(index);

        IdSymbol str = new IdSymbol("str", TypeSymbolConstants.STRING_STR);
        str.setTypeSymbol(TypeSymbolConstants.STRING);
        str.setResolved(true);

        substr.add(str);

        TypeSymbolConstants.STRING.add(substr);
    }

    private static void implicitDefinitionOfIOMethods() {
        // out_string definition

        MethodSymbol outString = new MethodSymbol("out_string", TypeSymbolConstants.SELF_TYPE_STR, TypeSymbolConstants.IO);
        outString.setReturnTypeSymbol(TypeSymbolConstants.SELF_TYPE);
        outString.setResolved(true);

        IdSymbol xString = new IdSymbol("x", TypeSymbolConstants.STRING_STR);
        xString.setTypeSymbol(TypeSymbolConstants.STRING);
        xString.setResolved(true);

        outString.add(xString);

        TypeSymbolConstants.IO.add(outString);

        // out_int definition

        MethodSymbol outInt = new MethodSymbol("out_int", TypeSymbolConstants.SELF_TYPE_STR, TypeSymbolConstants.IO);
        outInt.setReturnTypeSymbol(TypeSymbolConstants.SELF_TYPE);
        outInt.setResolved(true);

        IdSymbol xInt = new IdSymbol("x", TypeSymbolConstants.INT_STR);
        xInt.setTypeSymbol(TypeSymbolConstants.INT);
        xInt.setResolved(true);

        outInt.add(xInt);

        TypeSymbolConstants.IO.add(outInt);

        // in_string

        MethodSymbol inString = new MethodSymbol("in_string", TypeSymbolConstants.STRING_STR, TypeSymbolConstants.IO);
        inString.setReturnTypeSymbol(TypeSymbolConstants.STRING);
        inString.setResolved(true);

        TypeSymbolConstants.IO.add(inString);

        // in_int

        MethodSymbol inInt = new MethodSymbol("in_int", TypeSymbolConstants.INT_STR, TypeSymbolConstants.IO);
        inInt.setReturnTypeSymbol(TypeSymbolConstants.INT);
        inInt.setResolved(true);

        TypeSymbolConstants.IO.add(inInt);
    }

    private static void implicitDefinitionOfObjectMethods() {
        MethodSymbol abort = new MethodSymbol("abort", TypeSymbolConstants.OBJECT_STR, TypeSymbolConstants.OBJECT);
        abort.setReturnTypeSymbol(TypeSymbolConstants.OBJECT);
        abort.setResolved(true);

        TypeSymbolConstants.OBJECT.add(abort);

        MethodSymbol typeName = new MethodSymbol("type_name", TypeSymbolConstants.STRING_STR, TypeSymbolConstants.OBJECT);
        typeName.setReturnTypeSymbol(TypeSymbolConstants.STRING);
        typeName.setResolved(true);

        TypeSymbolConstants.OBJECT.add(typeName);

        MethodSymbol copy = new MethodSymbol("copy", TypeSymbolConstants.SELF_TYPE_STR, TypeSymbolConstants.OBJECT);
        copy.setReturnTypeSymbol(TypeSymbolConstants.SELF_TYPE);
        copy.setResolved(true);

        TypeSymbolConstants.OBJECT.add(copy);
    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
