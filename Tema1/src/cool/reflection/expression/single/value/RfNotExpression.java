package cool.reflection.expression.single.value;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.single.RfSingleValueExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfNotExpression extends RfSingleValueExpression {
    public RfNotExpression(RfExpression expression, Token token) {
        super(expression, token);
    }
}
