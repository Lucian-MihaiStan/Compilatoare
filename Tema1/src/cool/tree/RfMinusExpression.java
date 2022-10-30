package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfMinusExpression extends RfArithmeticExpression {

    public RfMinusExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
