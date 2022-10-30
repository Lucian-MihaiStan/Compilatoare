package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfLTExpression extends RfRelationalExpression {

    public RfLTExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
