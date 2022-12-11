package cool.reflection.type;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfString extends RfExpression {

    private final String value;

    public RfString(CoolParser.StringContext ctx, String value, Token token) {
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
