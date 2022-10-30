package cool.reflection.expression.arithmetic.operation;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.arithmetic.RfArithmeticExpression;
import org.antlr.v4.runtime.Token;

public class RfPlusExpression extends RfArithmeticExpression {

    public RfPlusExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
