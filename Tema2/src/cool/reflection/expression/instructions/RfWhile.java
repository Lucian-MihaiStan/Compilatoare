package cool.reflection.expression.instructions;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfWhile extends RfExpression {

    private final RfExpression cond;
    private final RfExpression body;
    private final CoolParser.ExprContext condContext;

    public RfWhile(CoolParser.WhileContext ctx, CoolParser.ExprContext condContext, RfExpression cond, RfExpression body, Token token) {
        super(ctx, token);
        this.cond = cond;
        this.condContext = condContext;
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

    public CoolParser.ExprContext getCondContext() {
        return condContext;
    }
}
