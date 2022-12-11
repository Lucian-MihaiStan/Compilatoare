package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfImplicitDispatch extends RfExpression {
    private final Token id;
    private final List<RfExpression> rfExpressions;

    public RfImplicitDispatch(CoolParser.ImplicitDispatchContext ctx, Token id, List<RfExpression> rfExpressions, Token token) {
        super(ctx, token);
        this.id = id;
        this.rfExpressions = rfExpressions;
    }

    public Token getId() {
        return id;
    }

    public List<RfExpression> getRfExpressions() {
        return rfExpressions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
