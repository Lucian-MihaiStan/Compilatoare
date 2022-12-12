package cool.visitor.construction;

import cool.reflection.*;
import cool.reflection.expression.*;
import cool.reflection.expression.arithmetic.RfArithmeticExpression;
import cool.reflection.expression.instructions.RfCase;
import cool.reflection.expression.instructions.RfIf;
import cool.reflection.expression.instructions.RfLet;
import cool.reflection.expression.instructions.RfWhile;
import cool.reflection.expression.relational.RfRelationalExpression;
import cool.reflection.expression.single.RfSingleValueExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.visitor.ASTVisitor;

import java.util.List;

public interface PrintVisitor {

    static ASTVisitor<Void> getPrintVisitor() {
        return new ASTVisitor<Void>() {

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
                rfClass.getTypes().forEach(rfType -> {
                    printIndent(rfType.getSymbol().getText());
                });
                rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfField rfField) {
                printIndent("attribute");
                indent++;
                printIndent(rfField.getFieldName().getText());
                printIndent(rfField.getFieldType().getText());
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
                printIndent(rfId.getValue().getText());
                return null;
            }

            @Override
            public Void visit(RfArithmeticExpression rfArithmeticExpression) {
               printIndent(rfArithmeticExpression.getArithmeticSymbol().getText());
               indent++;
               rfArithmeticExpression.getLhValue().accept(this);
               rfArithmeticExpression.getRhValue().accept(this);
               indent--;
               return null;
            }

            @Override
            public Void visit(RfParenExpression rfParenExpression) {
                rfParenExpression.getExpression().accept(this);
                return null;
            }

            @Override
            public Void visit(RfRelationalExpression rfRelationalExpression) {
                printIndent(rfRelationalExpression.getRelationalSymbol().getText());
                indent++;
                rfRelationalExpression.getLhValue().accept(this);
                rfRelationalExpression.getRhValue().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfSingleValueExpression rfSingleValueExpression) {
                printIndent(rfSingleValueExpression.getSymbol());
                indent++;
                rfSingleValueExpression.getExpression().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfAssignment rfAssignment) {
                printIndent(rfAssignment.getSymbol());
                indent++;
                printIndent(rfAssignment.getId().getText());
                rfAssignment.getExpr().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfNewExpression rfNewExpression) {
                printIndent(rfNewExpression.getSymbol());
                indent++;
                printIndent(rfNewExpression.getType());
                indent--;
                return null;
            }

            @Override
            public Void visit(RfDispatch rfDispatch) {
                printIndent(rfDispatch.getSymbol());
                indent++;
                rfDispatch.getExprStart().accept(this);
                String type = rfDispatch.getType();
                if (type != null)
                   printIndent(type);
                printIndent(rfDispatch.getId().getText());
                rfDispatch.getComponents().forEach(rfExpression -> rfExpression.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfImplicitDispatch rfImplicitDispatch) {
                printIndent("implicit dispatch");
                indent++;
                printIndent(rfImplicitDispatch.getId().getText());
                rfImplicitDispatch.getRfExpressions().forEach(rfExpression -> rfExpression.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfIf rfIf) {
                printIndent(rfIf.getSymbol());
                indent++;
                rfIf.getCond().accept(this);
                rfIf.getIfBranch().accept(this);
                rfIf.getElseBranch().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfWhile rfWhile) {
                printIndent(rfWhile.getSymbol());
                indent++;
                rfWhile.getCond().accept(this);
                rfWhile.getBody().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfLet rfLet) {
                printIndent(rfLet.getSymbol());
                indent++;
                rfLet.getVars().forEach(var -> var.accept(this));
                rfLet.getBody().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfLet.RfDeclareVariable rfDeclareVariable) {
                printIndent(rfDeclareVariable.getSymbol());
                indent++;
                printIndent(rfDeclareVariable.getName().getText());
                printIndent(rfDeclareVariable.getType().getText());
                if (rfDeclareVariable.getValue() != null)
                    rfDeclareVariable.getValue().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfCase rfCase) {
                printIndent(rfCase.getSymbol());
                indent++;
                rfCase.getToEvaluate().accept(this);
                rfCase.getBranches().forEach(rfBranch -> rfBranch.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfCase.RfCaseBranch rfCaseBranch) {
                printIndent(rfCaseBranch.getSymbol());
                indent++;
                printIndent(rfCaseBranch.getId().getText());
                printIndent(rfCaseBranch.getType().getText());
                rfCaseBranch.getExpression().accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(RfBody rfBody) {
                printIndent(rfBody.getSymbol());
                indent++;
                rfBody.getExpressions().forEach(expr -> expr.accept(this));
                indent--;
                return null;
            }

            @Override
            public Void visit(RfArgument rfArgument) {
                printIndent("formal");
                indent++;
                printIndent(rfArgument.getName().getText());
                printIndent(rfArgument.getType().getText());
                indent--;
                return null;
            }

            @Override
            public Void visit(RfMethod rfMethod) {
                printIndent("method");
                indent++;
                printIndent(rfMethod.getName().getText());

                List<RfArgument> rfArgs = rfMethod.getArgs();
                rfArgs.forEach(arg -> arg.accept(this));

                printIndent(rfMethod.getReturnType().getText());

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
    }

}
