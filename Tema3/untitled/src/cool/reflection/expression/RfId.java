package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfId extends RfExpression {

    private final Token value;

    public RfId(CoolParser.IdContext ctx, Token value, Token token) {
        super(ctx, token);
        this.value = value;
    }

    public Token getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.getText();
    }
}
