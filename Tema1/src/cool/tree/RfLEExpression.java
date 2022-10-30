package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfLEExpression extends RfRelationalExpression {

    public RfLEExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
