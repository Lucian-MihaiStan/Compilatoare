package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.structures.Symbol;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.stream.Collectors;

public class RfImplicitDispatch extends RfExpression {
    private final Token dispatch;
    private final List<RfExpression> parameters;
    private Symbol callerType;

    public RfImplicitDispatch(CoolParser.ImplicitDispatchContext ctx, Token id, List<RfExpression> parameters, Token token) {
        super(ctx, token);
        this.dispatch = id;
        this.parameters = parameters;
    }

    public Token getDispatch() {
        return dispatch;
    }

    public List<RfExpression> getParameters() {
        return parameters;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return (dispatch == null ? "null" : dispatch.getText()) + "(" + parameters.stream().map(Object::toString).collect(Collectors.toList()) + ")";
    }

    public Symbol getCallerType() {
        return callerType;
    }

    public void setCallerType(Symbol callerType) {
        this.callerType = callerType;
    }
}
