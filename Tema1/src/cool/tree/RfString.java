package cool.tree;

import org.antlr.v4.runtime.Token;

public class RfString extends RfExpression {

    private final String value;

    public RfString(String value, Token token) {
        super(token);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
