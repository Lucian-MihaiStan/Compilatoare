package cool.reflection.expression.instructions;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfIf extends RfExpression {

    private final RfExpression cond;
    private final RfExpression ifBranch;
    private final RfExpression elseBranch;
    private final CoolParser.ExprContext condContext;

    public RfIf(CoolParser.IfContext ctx, CoolParser.ExprContext condContext, RfExpression cond, RfExpression ifBranch, RfExpression elseBranch, Token token) {
        super(ctx, token);
        this.cond = cond;
        this.condContext = condContext;
        this.ifBranch = ifBranch;
        this.elseBranch = elseBranch;
    }

    public RfExpression getCond() {
        return cond;
    }

    public RfExpression getIfBranch() {
        return ifBranch;
    }

    public RfExpression getElseBranch() {
        return elseBranch;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }

    public CoolParser.ExprContext getCondContext() {
        return condContext;
    }
}
