package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfParenExpression extends RfExpression {
    private final RfExpression expression;

    public RfParenExpression(RfExpression expression, Token token) {
        super(token);
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