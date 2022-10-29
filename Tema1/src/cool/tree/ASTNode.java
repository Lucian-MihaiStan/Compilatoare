package cool.tree;

import org.antlr.v4.runtime.Token;

public abstract class ASTNode {

    protected Token token;

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }

}
