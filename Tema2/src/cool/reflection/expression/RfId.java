package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfId extends RfExpression {

    private final String value;

    public RfId(CoolParser.IdContext ctx, String value, Token token) {
        super(ctx, token);
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
