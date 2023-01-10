package cool.reflection.expression.single.value;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfParenExpression extends RfExpression {
    private final RfExpression expression;

    public RfParenExpression(CoolParser.ParenExprContext ctx, RfExpression expression, Token token) {
        super(ctx, token);
        this.expression = expression;
    }

    public RfExpression getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
