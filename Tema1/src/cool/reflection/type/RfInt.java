package cool.reflection.type;

import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfInt extends RfExpression {

    private final Integer value;

    public RfInt(String value, Token token) {
        super(token);
        this.value = Integer.parseInt(value);
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
