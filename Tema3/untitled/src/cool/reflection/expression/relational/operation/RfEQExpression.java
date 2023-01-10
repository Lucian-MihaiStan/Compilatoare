package cool.reflection.expression.relational.operation;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.reflection.expression.relational.RfRelationalExpression;
import org.antlr.v4.runtime.Token;

public class RfEQExpression extends RfRelationalExpression {
    public RfEQExpression(CoolParser.EqContext ctx, RfExpression lhValue, RfExpression rhValue, Token token) {
        super(ctx, lhValue, rhValue, token);
    }
}
