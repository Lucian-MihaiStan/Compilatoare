package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.stream.Collectors;

public class RfImplicitDispatch extends RfExpression {
    private final Token id;
    private final List<RfExpression> parameters;

    public RfImplicitDispatch(CoolParser.ImplicitDispatchContext ctx, Token id, List<RfExpression> parameters, Token token) {
        super(ctx, token);
        this.id = id;
        this.parameters = parameters;
    }

    public Token getId() {
        return id;
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
        return (id == null ? "null" : id.getText()) + "(" + parameters.stream().map(Object::toString).collect(Collectors.toList()) + ")";
    }
}
