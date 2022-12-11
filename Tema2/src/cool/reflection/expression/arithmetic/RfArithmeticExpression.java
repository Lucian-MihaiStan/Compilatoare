package cool.reflection.expression.arithmetic;

import cool.visitor.ASTVisitor;
import cool.reflection.expression.RfExpression;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class RfArithmeticExpression extends RfExpression {

    private final RfExpression lhValue;
    private final RfExpression rhValue;

    private final String arithmeticSymbol;

    protected RfArithmeticExpression(ParserRuleContext ctx, RfExpression lhValue, RfExpression rhValue, Token token) {
        super(ctx, token);
        this.lhValue = lhValue;
        this.rhValue = rhValue;
        this.arithmeticSymbol = token.getText();
    }

    public RfExpression getLhValue() {
        return lhValue;
    }

    public RfExpression getRhValue() {
        return rhValue;
    }

    public String getArithmeticSymbol() {
        return arithmeticSymbol;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
