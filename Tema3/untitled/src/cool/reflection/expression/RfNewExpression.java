package cool.reflection.expression;

import cool.parser.CoolParser;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfNewExpression extends RfExpression{

    private final Token type;

    public RfNewExpression(CoolParser.NewContext ctx, Token type, Token token) {
        super(ctx, token);
        this.type = type;
    }

    public Token getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }

    @Override
    public String toString() {
        return "new " + (type == null ? "null" : type.getText());
    }
}
