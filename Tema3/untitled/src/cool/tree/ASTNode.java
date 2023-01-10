package cool.tree;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class ASTNode {

    protected final Token token;
    protected final ParserRuleContext context;

    protected ASTNode(ParserRuleContext context, Token token) {
        this.token = token;
        this.context = context;
    }

    abstract public <T> T accept(ASTVisitor<T> visitor);

    public ParserRuleContext getContext() {
        return context;
    }

    public Token getToken() {
        return token;
    }
}
