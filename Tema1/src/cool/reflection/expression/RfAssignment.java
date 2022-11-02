package cool.reflection.expression;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfAssignment extends RfExpression {

    private final RfId id;
    private final RfExpression expr;

    public RfAssignment(RfId id, RfExpression expr, Token token) {
        super(token);
        this.id = id;
        this.expr = expr;
    }

    public RfId getId() {
        return id;
    }

    public RfExpression getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }
}
