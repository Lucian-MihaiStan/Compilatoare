package cool.reflection.expression;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfImplicitDispatch extends RfExpression {
    private final RfId rfId;
    private final List<RfExpression> rfExpressions;

    public RfImplicitDispatch(RfId rfId, List<RfExpression> rfExpressions, Token token) {
        super(token);
        this.rfId = rfId;
        this.rfExpressions = rfExpressions;
    }

    public RfId getRfId() {
        return rfId;
    }

    public List<RfExpression> getRfExpressions() {
        return rfExpressions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
