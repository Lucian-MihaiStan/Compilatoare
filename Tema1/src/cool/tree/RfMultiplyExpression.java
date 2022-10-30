package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfMultiplyExpression extends RfArithmeticExpression {

    public RfMultiplyExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
