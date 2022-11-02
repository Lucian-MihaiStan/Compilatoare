package cool.reflection.expression;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfDispatch extends RfExpression {

    private final RfExpression exprStart;
    private final String type;
    private final RfId rfId;
    private final List<RfExpression> components;

    public RfDispatch(RfExpression exprStart, String type, RfId rfId, List<RfExpression> components, Token token) {
        super(token);
        this.exprStart = exprStart;
        this.type = type;
        this.rfId = rfId;
        this.components = components;
    }

    public RfExpression getExprStart() {
        return exprStart;
    }

    public String getType() {
        return type;
    }

    public RfId getRfId() {
        return rfId;
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
