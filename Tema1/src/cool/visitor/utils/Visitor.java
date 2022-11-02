package cool.visitor.utils;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;
import cool.reflection.*;
import cool.reflection.expression.*;
import cool.reflection.expression.arithmetic.operation.RfDivideExpression;
import cool.reflection.expression.arithmetic.operation.RfMinusExpression;
import cool.reflection.expression.arithmetic.operation.RfMultiplyExpression;
import cool.reflection.expression.arithmetic.operation.RfPlusExpression;
import cool.reflection.expression.instructions.RfIf;
import cool.reflection.expression.instructions.RfLet;
import cool.reflection.expression.instructions.RfWhile;
import cool.reflection.expression.relational.operation.RfEQExpression;
import cool.reflection.expression.relational.operation.RfLEExpression;
import cool.reflection.expression.relational.operation.RfLTExpression;
import cool.reflection.expression.single.value.RfBitNegExpression;
import cool.reflection.expression.single.value.RfIsVoidExpression;
import cool.reflection.expression.single.value.RfNotExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.feature.RfFeature;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.tree.ASTNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public interface Visitor {

    static CoolParserBaseVisitor<ASTNode> createBaseVisitor() {
        return new CoolParserBaseVisitor<>() {

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
            public ASTNode visitId_assign_expr(CoolParser.Id_assign_exprContext ctx) {
                return new RfAssignment(new RfId(ctx.ID().getSymbol().getText(), ctx.ID().getSymbol()), (RfExpression) visit(ctx.expr()), ctx.ASSIGN().getSymbol());
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

            @Override
            public ASTNode visitIsvoidcheck(CoolParser.IsvoidcheckContext ctx) {
                return new RfIsVoidExpression((RfExpression) visit(ctx.expr()), ctx.ISVOID().getSymbol());
            }

            @Override
            public ASTNode visitNew(CoolParser.NewContext ctx) {
                return new RfNewExpression(ctx.TYPE().getSymbol().getText(), ctx.start);
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

            /**
             * WHILE expr LOOP expr POOL
             */
            @Override
            public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                return new RfWhile((RfExpression) visit(ctx.expr(0)), (RfExpression) visit(ctx.expr(1)), ctx.WHILE().getSymbol());
            }

            /**
             * declareVar : ID COLON TYPE (ASSIGN expr)? ;
             * LET declareVar (COMMA declareVar)* IN expr
             */
            @Override
            public ASTNode visitLet(CoolParser.LetContext ctx) {
                List<CoolParser.DeclareVarContext> declareVarContexts = ctx.declareVar();

                List<RfLet.RfDeclareVariable> vars = new ArrayList<>();
                declareVarContexts.forEach(declareVarContext -> vars.add(new RfLet.RfDeclareVariable(
                        new RfId(declareVarContext.ID().getSymbol().getText(), declareVarContext.ID().getSymbol()),
                        declareVarContext.TYPE().getSymbol().getText(),
                        (RfExpression) visit(declareVarContext.expr()),
                        declareVarContext.COLON().getSymbol()
                )));

                return new RfLet(vars, (RfExpression) visit(ctx.expr()), ctx.LET().getSymbol());
            }

            @Override
            public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
                List<CoolParser.ExprContext> expressions = ctx.expr();
                if (expressions == null || expressions.isEmpty()) {
                    throw new IllegalStateException("Unable to compute expression in dispatch rule");
                }

                RfExpression exprStart = (RfExpression) visit(expressions.get(0));

                String type = ctx.TYPE() != null ? ctx.TYPE().getText() : null;
                RfId rfId = new RfId(ctx.ID().getSymbol().getText(), ctx.ID().getSymbol());

                List<RfExpression> components = new ArrayList<>();

                for (int i = 1; i < expressions.size(); i++)
                    components.add((RfExpression) visit(expressions.get(i)));

                return new RfDispatch(exprStart, type, rfId, components, ctx.DOT().getSymbol());
            }

            /**
             * ID LPAREN (expr (COMMA expr)*)? RPAREN
             */
            @Override
            public ASTNode visitImplicit_dispatch(CoolParser.Implicit_dispatchContext ctx) {
                RfId rfId = new RfId(ctx.ID().getSymbol().getText(), ctx.ID().getSymbol());

                List<CoolParser.ExprContext> expressions = ctx.expr();
                List<RfExpression> rfExpressions = new ArrayList<>();

                for (CoolParser.ExprContext expr : expressions)
                    rfExpressions.add((RfExpression) visit(expr));
                return new RfImplicitDispatch(rfId, rfExpressions, ctx.ID().getSymbol());
            }

            /**
             * ID LPAREN (expr (COMMA expr)*)? RPAREN
             */
            @Override
            public ASTNode visitIf(CoolParser.IfContext ctx) {
                CoolParser.ExprContext cond = ctx.expr(0);
                CoolParser.ExprContext ifBranch = ctx.expr(1);
                CoolParser.ExprContext elseBranch = ctx.expr(2);

                return new RfIf((RfExpression) visit(cond), (RfExpression) visit(ifBranch), (RfExpression) visit(elseBranch), ctx.IF().getSymbol());
            }
        };
    }

}
