package cool.reflection.type;

import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfBool extends RfExpression {

    private final Boolean value;

    public RfBool(String value, Token token) {
        super(token);
        this.value = Boolean.parseBoolean(value);
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
