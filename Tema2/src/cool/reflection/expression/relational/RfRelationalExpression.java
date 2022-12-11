package cool.reflection.expression.relational;

import cool.visitor.ASTVisitor;
import cool.reflection.expression.RfExpression;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class RfRelationalExpression extends RfExpression {

    private final RfExpression lhValue;
    private final RfExpression rhValue;

    public RfRelationalExpression(ParserRuleContext ctx, RfExpression lhValue, RfExpression rhValue, Token token) {
        super(ctx, token);
        this.lhValue = lhValue;
        this.rhValue = rhValue;
    }

    public RfExpression getRhValue() {
        return rhValue;
    }

    public RfExpression getLhValue() {
        return lhValue;
    }

    public String getRelationalSymbol() {
        return token.getText();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
