package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfEQExpression extends RfRelationalExpression {
    public RfEQExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }
}
