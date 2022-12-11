package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfBody extends RfExpression {

    private final List<RfExpression> expressions;

    public RfBody(CoolParser.BodyContext ctx, List<RfExpression> expressions, Token token) {
        super(ctx, token);
        this.expressions = expressions;
    }

    public List<RfExpression> getExpressions() {
        return expressions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return "block";
    }
}
