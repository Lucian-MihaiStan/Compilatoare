package cool.reflection;

import cool.tree.ASTNode;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfArgument extends ASTNode {

    private final String name;
    private final String type;

    public RfArgument(String name, String type, Token token) {
        super(token);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
