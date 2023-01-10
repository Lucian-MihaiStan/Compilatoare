package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfAssignment extends RfExpression {

    private final Token id;

    private final RfExpression expr;
    private final CoolParser.ExprContext value;

    public RfAssignment(CoolParser.AssignContext ctx, Token id, CoolParser.ExprContext value, RfExpression expr, Token token) {
        super(ctx, token);
        this.id = id;
        this.value = value;
        this.expr = expr;
    }

    public Token getId() {
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

    @Override
    public String toString() {
        return (id == null ? "null" : id.getText()) + " <- " + (expr != null ? expr.toString() : "");
    }

    public CoolParser.ExprContext getValue() {
        return value;
    }
}
