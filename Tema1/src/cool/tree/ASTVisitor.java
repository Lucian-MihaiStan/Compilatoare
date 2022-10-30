package cool.tree;

public interface ASTVisitor<T> {

    T visit(RfProgram rfProgram);

    T visit(RfClass rfClass);

    T visit(RfField rfField);

    T visit(RfInt rfInt);

    T visit(RfArgument rfArgument);

    T visit(RfMethod rfMethod);

    T visit(RfString rfString);

    T visit(RfBool rfBool);

    T visit(RfId rfId);

    T visit(RfArithmeticExpression rfArithmeticExpression);

    T visit(RfParenExpression rfParenExpression);

    T visit(RfBitNegExpression rfBitNegExpression);
}
