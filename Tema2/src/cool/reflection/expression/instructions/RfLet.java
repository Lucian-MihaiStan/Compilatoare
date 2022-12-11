package cool.reflection.expression.instructions;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfLet extends RfExpression {

    private final List<RfDeclareVariable> vars;
    private final RfExpression body;

    public static class RfDeclareVariable extends RfExpression {

        private final Token id;
        private final Token type;
        private final RfExpression value;

        public RfDeclareVariable(CoolParser.DeclareVarContext ctx, Token id, Token type, RfExpression value, Token token) {
            super(ctx, token);
            this.id = id;
            this.type = type;
            this.value = value;
        }

        public Token getRfId() {
            return id;
        }

        public Token getType() {
            return type;
        }

        public RfExpression getValue() {
            return value;
        }

        @Override
        public <T> T accept(ASTVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public String getSymbol() {
            return "local";
        }
    }

    public RfLet(CoolParser.LetContext ctx, List<RfDeclareVariable> vars, RfExpression body, Token token) {
        super(ctx, token);
        this.vars = vars;
        this.body = body;
    }

    public List<RfDeclareVariable> getVars() {
        return vars;
    }

    public RfExpression getBody() {
        return body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getSymbol() {
        return token.getText();
    }

}
