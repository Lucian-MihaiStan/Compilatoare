package cool.reflection.expression.single.value;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.reflection.expression.single.RfSingleValueExpression;
import org.antlr.v4.runtime.Token;

public class RfIsVoidExpression extends RfSingleValueExpression {

    public RfIsVoidExpression(CoolParser.IsVoidContext ctx, RfExpression expression, Token token) {
        super(ctx, expression, token);
    }

}
