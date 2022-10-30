package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfPlusExpression extends RfArithmeticExpression {

    public RfPlusExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
