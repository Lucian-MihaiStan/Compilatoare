package cool.reflection.expression;

import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfNewExpression extends RfExpression{

    private final String type;

    public RfNewExpression(String type, Token token) {
        super(token);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }
}
