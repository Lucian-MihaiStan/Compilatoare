package cool.tree;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public abstract class ASTNode {

    protected final Token token;

    protected ASTNode(Token token) {
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }

}
