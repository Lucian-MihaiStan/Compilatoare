package cool.reflection.expression.single;

import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public abstract class RfSingleValueExpression extends RfExpression  {

    private final RfExpression expression;

    protected RfSingleValueExpression(RfExpression expression, Token token) {
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

    public String getSymbol() {
        return token.getText();
    }
}
