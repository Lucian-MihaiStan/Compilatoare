package cool.reflection.expression.instructions;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.RfId;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfLet extends RfExpression {

    private final List<RfDeclareVariable> vars;
    private final RfExpression body;

    public static class RfDeclareVariable extends RfExpression {

        private final RfId rfId;
        private final String type;
        private final RfExpression value;

        public RfDeclareVariable(RfId rfId, String type, RfExpression value, Token token) {
            super(token);
            this.rfId = rfId;
            this.type = type;
            this.value = value;
        }

        public RfId getRfId() {
            return rfId;
        }

        public String getType() {
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

    public RfLet(List<RfDeclareVariable> vars, RfExpression body, Token token) {
        super(token);
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
