package cool.visitor.construction;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;
import cool.reflection.*;
import cool.reflection.expression.*;
import cool.reflection.expression.arithmetic.operation.RfDivideExpression;
import cool.reflection.expression.arithmetic.operation.RfMinusExpression;
import cool.reflection.expression.arithmetic.operation.RfMultiplyExpression;
import cool.reflection.expression.arithmetic.operation.RfPlusExpression;
import cool.reflection.expression.instructions.RfCase;
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

import java.util.List;
import java.util.stream.Collectors;

public interface Visitor {

    static CoolParserBaseVisitor<ASTNode> createBaseVisitor() {
        return new CoolParserBaseVisitor<>() {

            @Override
            public ASTNode visitInt(CoolParser.IntContext ctx) {
                return new RfInt(ctx, ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitString(CoolParser.StringContext ctx) {
                return new RfString(ctx, ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitBoolExpr(CoolParser.BoolExprContext ctx) {
                return new RfBool(ctx, ctx.getText(), ctx.start);
            }

            @Override
            public ASTNode visitBody(CoolParser.BodyContext ctx) {
                return new RfBody(ctx, ctx.statements.stream().map(expr -> (RfExpression) visit(expr)).collect(Collectors.toList()), ctx.start);
            }

            @Override
            public ASTNode visitPlus(CoolParser.PlusContext ctx) {
                return new RfPlusExpression(ctx, (RfExpression)visit(ctx.lhValue), (RfExpression)visit(ctx.rhValue), ctx.PLUS().getSymbol());
            }

            @Override
            public ASTNode visitMinus(CoolParser.MinusContext ctx) {
                return new RfMinusExpression(ctx, (RfExpression)visit(ctx.lhValue), (RfExpression)visit(ctx.rhValue), ctx.MINUS().getSymbol());
            }

            @Override
            public ASTNode visitMultiply(CoolParser.MultiplyContext ctx) {
                return new RfMultiplyExpression(ctx, (RfExpression)visit(ctx.lhValue), (RfExpression)visit(ctx.rhValue), ctx.MULTIPLY().getSymbol());
            }

            @Override
            public ASTNode visitParenExpr(CoolParser.ParenExprContext ctx) {
                return new RfParenExpression(ctx, (RfExpression) visit(ctx.expr()), ctx.start);
            }

            @Override
            public ASTNode visitDivide(CoolParser.DivideContext ctx) {
                return new RfDivideExpression(ctx, (RfExpression)visit(ctx.lhValue), (RfExpression)visit(ctx.rhValue), ctx.DIVIDE().getSymbol());
            }

            @Override
            public ASTNode visitAssign(CoolParser.AssignContext ctx) {
                return new RfAssignment(ctx, ctx.ID().getSymbol(), ctx.value, (RfExpression) visit(ctx.value), ctx.ASSIGN().getSymbol());
            }

            @Override
            public ASTNode visitNegation(CoolParser.NegationContext ctx) {
                return new RfBitNegExpression(ctx, (RfExpression) visit(ctx.value), ctx.TILDA().getSymbol());
            }

            @Override
            public ASTNode visitNot(CoolParser.NotContext ctx) {
                return new RfNotExpression(ctx, (RfExpression) visit(ctx.value), ctx.NOT().getSymbol());
            }

            @Override
            public ASTNode visitLe(CoolParser.LeContext ctx) {
                return new RfLEExpression(ctx, (RfExpression) visit(ctx.lhValue), (RfExpression) visit(ctx.rhValue), ctx.LE().getSymbol());
            }

            @Override
            public ASTNode visitLt(CoolParser.LtContext ctx) {
                return new RfLTExpression(ctx, (RfExpression) visit(ctx.lhValue), (RfExpression) visit(ctx.rhValue), ctx.LT().getSymbol());
            }

            @Override
            public ASTNode visitEq(CoolParser.EqContext ctx) {
                return new RfEQExpression(ctx, (RfExpression) visit(ctx.lhValue), (RfExpression) visit(ctx.rhValue), ctx.EQ().getSymbol());
            }

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new RfId(ctx, ctx.ID().getSymbol(), ctx.ID().getSymbol());
            }

            /**
             * formal : ID COLON TYPE;
             */
            @Override
            public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                return new RfArgument(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.start);
            }

            /**
             * ID LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPE LBRACE expr RBRACE
             */
            @Override
            public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                return new RfMethod(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.formalParams.stream().map(arg -> (RfArgument) visit(arg)).collect(Collectors.toList()), (RfExpression) visit(ctx.methodBody), ctx.ID().getSymbol());
            }

            /**
             * ID COLON TYPE (ASSIGN expr)?
             */
            @Override
            public ASTNode visitField(CoolParser.FieldContext ctx) {
                return new RfField(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.expr() != null ? (RfExpression) visit(ctx.expr()) : null, ctx.ID().getSymbol());
            }

            /**
             * class : CLASS TYPE (INHERITS TYPE)? LBRACE (feature SEMI)* RBRACE ;
             */
            @Override
            public ASTNode visitClass(CoolParser.ClassContext ctx) {
                return new RfClass(ctx, ctx.TYPE(), ctx.feature().stream().map(feature -> (RfFeature) visit(feature)).collect(Collectors.toList()), ctx.TYPE(0).getSymbol());
            }

            @Override
            public ASTNode visitIsVoid(CoolParser.IsVoidContext ctx) {
                return new RfIsVoidExpression(ctx, (RfExpression) visit(ctx.value), ctx.ISVOID().getSymbol());
            }

            @Override
            public ASTNode visitNew(CoolParser.NewContext ctx) {
                return new RfNewExpression(ctx, ctx.TYPE().getSymbol(), ctx.start);
            }

            /**
             * program : (class SEMI)+ EOF ;
             */
            @Override
            public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                return new RfProgram(ctx, ctx.class_().stream().map(c -> (RfClass) visit(c)).collect(Collectors.toList()), ctx.start);
            }

            /**
             * WHILE expr LOOP expr POOL
             */
            @Override
            public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                return new RfWhile(ctx, ctx.cond, (RfExpression) visit(ctx.cond), (RfExpression) visit(ctx.body), ctx.WHILE().getSymbol());
            }

            /**
             * declareVar : ID COLON TYPE (ASSIGN expr)? ;
             * LET declareVar (COMMA declareVar)* IN expr
             */
            @Override
            public ASTNode visitLet(CoolParser.LetContext ctx) {
                return new RfLet(ctx, ctx.decVars.stream().map(declareVarContext -> (RfLet.RfDeclareVariable) visit(declareVarContext)).collect(Collectors.toList()), (RfExpression) visit(ctx.letBody), ctx.LET().getSymbol());
            }

            @Override
            public ASTNode visitDeclareVar(CoolParser.DeclareVarContext ctx) {
                return new RfLet.RfDeclareVariable(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.expr() != null ? (RfExpression) visit(ctx.expr()) : null, ctx.TYPE().getSymbol());
            }

            @Override
            public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
                return new RfDispatch(ctx, (RfExpression) visit(ctx.obj), ctx.TYPE() != null ? ctx.TYPE().getSymbol() : null, ctx.ID().getSymbol(), ctx.params.stream().map(param -> (RfExpression) visit(param)).collect(Collectors.toList()), ctx.DOT().getSymbol());
            }

            @Override
            public ASTNode visitCaseBranch(CoolParser.CaseBranchContext ctx) {
                return new RfCase.RfCaseBranch(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), (RfExpression) visit(ctx.branchBody), ctx.RESULTS_CASE().getSymbol());
            }

            @Override
            public ASTNode visitCase(CoolParser.CaseContext ctx) {
                return new RfCase(ctx, (RfExpression) visit(ctx.cond), ctx.branches.stream().map(branch -> (RfCase.RfCaseBranch) visit(branch)).collect(Collectors.toList()), ctx.CASE().getSymbol());
            }

            /**
             * ID LPAREN (expr (COMMA expr)*)? RPAREN
             */
            @Override
            public ASTNode visitImplicitDispatch(CoolParser.ImplicitDispatchContext ctx) {
                return new RfImplicitDispatch(ctx, ctx.ID().getSymbol(), ctx.params.stream().map(expr -> (RfExpression) visit(expr)).collect(Collectors.toList()), ctx.ID().getSymbol());
            }

            @Override
            public ASTNode visitIf(CoolParser.IfContext ctx) {
                return new RfIf(ctx, ctx.cond, (RfExpression) visit(ctx.cond), (RfExpression) visit(ctx.thenBranch), (RfExpression) visit(ctx.elseBranch), ctx.IF().getSymbol());
            }
        };
    }

}
