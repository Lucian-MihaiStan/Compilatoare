package cool.reflection.expression.single.value;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.single.RfSingleValueExpression;
import org.antlr.v4.runtime.Token;

public class RfIsVoidExpression extends RfSingleValueExpression {

    public RfIsVoidExpression(RfExpression expression, Token token) {
        super(expression, token);
    }

}
