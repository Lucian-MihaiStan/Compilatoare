package cool.tree;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfMethod extends RfFeature {

    private final String name;
    private final String returnType;
    private List<RfArgument> args;
    private RfExpression rfMethodBody;

    public RfMethod(String name, String returnType, List<RfArgument> args, RfExpression rfMethodBody, Token token) {
        super(token);
        this.name = name;
        this.returnType = returnType;
        this.args = args;
        this.rfMethodBody = rfMethodBody;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
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
}
