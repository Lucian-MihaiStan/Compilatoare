package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.tree.ASTNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class RfExpression extends ASTNode {

    protected RfExpression(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

}
