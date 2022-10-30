package cool.visitor.utils;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;
import cool.tree.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public interface Visitor {

    static CoolParserBaseVisitor<ASTNode> createBaseVisitor() {
        return new CoolParserBaseVisitor<ASTNode>() {

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
            public ASTNode visitPlus(CoolParser.PlusContext ctx) {
                return new RfPlusExpression((RfExpression)visit(ctx.expr(0)), (RfExpression)visit(ctx.expr(1)), ctx.PLUS().getSymbol());
            }

            @Override
            public ASTNode visitMinus(CoolParser.MinusContext ctx) {
                return new RfMinusExpression((RfExpression)visit(ctx.expr(0)), (RfExpression)visit(ctx.expr(1)), ctx.MINUS().getSymbol());
            }

            @Override
            public ASTNode visitMultiply(CoolParser.MultiplyContext ctx) {
                return new RfMultiplyExpression((RfExpression)visit(ctx.expr(0)), (RfExpression)visit(ctx.expr(1)), ctx.MULTIPLY().getSymbol());
            }

            @Override
            public ASTNode visitParen_expr(CoolParser.Paren_exprContext ctx) {
                return new RfParenExpression((RfExpression) visit(ctx.expr()), ctx.start);
            }

            @Override
            public ASTNode visitDivide(CoolParser.DivideContext ctx) {
                return new RfDivideExpression((RfExpression)visit(ctx.expr(0)), (RfExpression)visit(ctx.expr(1)), ctx.DIVIDE().getSymbol());
            }

            @Override
            public ASTNode visitBit_neg(CoolParser.Bit_negContext ctx) {
                return new RfBitNegExpression((RfExpression) visit(ctx.expr()), ctx.TILDA().getSymbol());
            }

            @Override
            public ASTNode visitNot(CoolParser.NotContext ctx) {
                return new RfNotExpression((RfExpression) visit(ctx.expr()), ctx.NOT().getSymbol());
            }

            @Override
            public ASTNode visitLe(CoolParser.LeContext ctx) {
                return new RfLEExpression((RfExpression) visit(ctx.expr(0)), (RfExpression) visit(ctx.expr(1)), ctx.LE().getSymbol());
            }

            @Override
            public ASTNode visitLt(CoolParser.LtContext ctx) {
                return new RfLTExpression((RfExpression) visit(ctx.expr(0)), (RfExpression) visit(ctx.expr(1)), ctx.LT().getSymbol());
            }

            @Override
            public ASTNode visitEq(CoolParser.EqContext ctx) {
                return new RfEQExpression((RfExpression) visit(ctx.expr(0)), (RfExpression) visit(ctx.expr(1)), ctx.EQ().getSymbol());
            }

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new RfId(ctx.getText(), ctx.start);
            }

            /**
             * formal : ID COLON TYPE;
             */
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
    }

}
