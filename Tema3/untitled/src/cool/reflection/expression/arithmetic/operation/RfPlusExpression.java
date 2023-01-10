package cool.reflection.expression.arithmetic.operation;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.reflection.expression.arithmetic.RfArithmeticExpression;
import org.antlr.v4.runtime.Token;

public class RfPlusExpression extends RfArithmeticExpression {

    public RfPlusExpression(CoolParser.PlusContext ctx, RfExpression lhValue, RfExpression rhValue, Token token) {
        super(ctx, lhValue, rhValue, token);
    }

}
