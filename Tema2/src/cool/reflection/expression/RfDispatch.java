package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfDispatch extends RfExpression {

    private final RfExpression exprStart;
    private final String type;
    private final Token id;
    private final List<RfExpression> components;

    public RfDispatch(CoolParser.DispatchContext ctx, RfExpression exprStart, String type, Token id, List<RfExpression> components, Token token) {
        super(ctx, token);
        this.exprStart = exprStart;
        this.type = type;
        this.id = id;
        this.components = components;
    }

    public RfExpression getExprStart() {
        return exprStart;
    }

    public String getType() {
        return type;
    }

    public Token getId() {
        return id;
    }

    public List<RfExpression> getComponents() {
        return components;
    }

    public String getSymbol() {
        return token.getText();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
