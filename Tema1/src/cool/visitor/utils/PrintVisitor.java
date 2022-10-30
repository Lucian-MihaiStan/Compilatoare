package cool.visitor.utils;

import cool.tree.*;

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
            public Void visit(RfArithmeticExpression rfArithmeticExpression) {
               printIndent(rfArithmeticExpression.getArithmeticSymbol());
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
            public Void visit(RfBitNegExpression rfBitNegExpression) {
                printIndent("~");
                indent++;
                rfBitNegExpression.getExpression().accept(this);
                indent--;
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
    }

}
