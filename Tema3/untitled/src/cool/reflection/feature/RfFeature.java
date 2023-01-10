package cool.reflection.feature;

import cool.tree.ASTNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class RfFeature extends ASTNode {

    protected RfFeature(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

}
