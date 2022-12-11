package cool.reflection.feature.features;

import cool.parser.CoolParser;
import cool.reflection.RfArgument;
import cool.reflection.expression.RfExpression;
import cool.reflection.feature.RfFeature;
import cool.structures.custom.symbols.constants.MethodSymbol;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.stream.Collectors;

public class RfMethod extends RfFeature {

    private final Token name;
    private final Token returnType;
    private final List<RfArgument> args;
    private final RfExpression rfMethodBody;
    private MethodSymbol methodSymbol;

    public RfMethod(CoolParser.MethodContext ctx, Token name, Token returnType, List<RfArgument> args, RfExpression rfMethodBody, Token token) {
        super(ctx, token);
        this.name = name;
        this.returnType = returnType;
        this.args = args;
        this.rfMethodBody = rfMethodBody;
    }

    public Token getName() {
        return name;
    }

    public Token getReturnType() {
        return returnType;
    }

    public List<RfArgument> getArgs() {
        return args;
    }

    public RfExpression getRfMethodBody() {
        return rfMethodBody;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setMethodSymbol(MethodSymbol methodSymbol) {
        this.methodSymbol = methodSymbol;
    }

    public MethodSymbol getMethodSymbol() {
        return methodSymbol;
    }

    @Override
    public String toString() {
        return (name == null ? "null" : name.getText()) + "(" + args.stream().map(rfArgument -> rfArgument.toString()).collect(Collectors.toList()) + ") : " + (returnType == null ? "null" : returnType.getText());
    }
}
