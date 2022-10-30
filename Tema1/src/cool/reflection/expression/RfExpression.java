package cool.reflection.expression;

import cool.tree.ASTNode;
import org.antlr.v4.runtime.Token;

public abstract class RfExpression extends ASTNode {

    protected RfExpression(Token token) {
        super(token);
    }

}
