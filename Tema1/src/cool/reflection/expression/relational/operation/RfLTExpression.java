package cool.reflection.expression.relational.operation;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.relational.RfRelationalExpression;
import org.antlr.v4.runtime.Token;

public class RfLTExpression extends RfRelationalExpression {

    public RfLTExpression(RfExpression lhValue, RfExpression rhValue, Token token) {
        super(lhValue, rhValue, token);
    }

}
