package cool.reflection.feature.features;

import cool.parser.CoolParser;
import cool.reflection.RfArgument;
import cool.reflection.expression.RfExpression;
import cool.reflection.feature.RfFeature;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfMethod extends RfFeature {

    private final String name;
    private final String returnType;
    private final List<RfArgument> args;
    private final RfExpression rfMethodBody;

    public RfMethod(CoolParser.MethodContext ctx, String name, String returnType, List<RfArgument> args, RfExpression rfMethodBody, Token token) {
        super(ctx, token);
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
