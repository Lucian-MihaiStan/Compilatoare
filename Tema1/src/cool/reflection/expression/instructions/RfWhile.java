package cool.reflection.expression.instructions;

import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfWhile extends RfExpression {

    private final RfExpression cond;
    private final RfExpression body;

    public RfWhile(RfExpression cond, RfExpression body, Token token) {
        super(token);
        this.cond = cond;
        this.body = body;
    }

    public RfExpression getCond() {
        return cond;
    }

    public RfExpression getBody() {
        return body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }
}
