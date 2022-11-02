package cool.visitor;

import cool.reflection.*;
import cool.reflection.expression.*;
import cool.reflection.expression.instructions.RfIf;
import cool.reflection.expression.instructions.RfLet;
import cool.reflection.expression.instructions.RfWhile;
import cool.reflection.expression.single.RfSingleValueExpression;
import cool.reflection.expression.single.value.RfBitNegExpression;
import cool.reflection.expression.single.value.RfNotExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.expression.arithmetic.RfArithmeticExpression;
import cool.reflection.expression.relational.RfRelationalExpression;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;

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

    T visit(RfRelationalExpression rfRelationalExpression);

    T visit(RfSingleValueExpression rfSingleValueExpression);

    T visit(RfAssignment rfAssignment);

    T visit(RfNewExpression rfNewExpression);

    T visit(RfDispatch rfDispatch);

    T visit(RfImplicitDispatch rfImplicitDispatch);

    T visit(RfIf rfIf);

    T visit(RfWhile rfWhile);

    T visit(RfLet rfLet);

    T visit(RfLet.RfDeclareVariable rfDeclareVariable);
}
