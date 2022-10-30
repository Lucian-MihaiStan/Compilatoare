package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfDivideExpression extends RfArithmeticExpression {
    public RfDivideExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }



}
