package cool.tree;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class RfClass extends ASTNode {

    private final List<TerminalNode> types;

    public RfClass(List<TerminalNode> type, Token token) {
        this.types = type;
        this.token = token;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<TerminalNode> getTypes() {
        return types;
    }
}
