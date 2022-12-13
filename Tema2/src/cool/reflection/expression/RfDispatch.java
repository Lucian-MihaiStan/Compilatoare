package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.stream.Collectors;

public class RfDispatch extends RfExpression {

    private final RfExpression objectToCall;
    private final Token atType;
    private final Token dispatchName;
    private final List<RfExpression> parameters;

    public RfDispatch(CoolParser.DispatchContext ctx, RfExpression objectToCall, Token atType, Token dispatchName, List<RfExpression> parameters, Token token) {
        super(ctx, token);
        this.objectToCall = objectToCall;
        this.atType = atType;
        this.dispatchName = dispatchName;
        this.parameters = parameters;
    }

    public RfExpression getObjectToCall() {
        return objectToCall;
    }

    public Token getAtType() {
        return atType;
    }

    public Token getDispatch() {
        return dispatchName;
    }

    public List<RfExpression> getParameters() {
        return parameters;
    }

    public String getSymbol() {
        return token.getText();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return objectToCall.toString() + (atType == null ? "" : ("@" + atType.getText())) + "." + (dispatchName == null ? "null" : dispatchName.getText()) + "(" + parameters.stream().map(Object::toString).collect(Collectors.toList()) + ")";
    }
}
