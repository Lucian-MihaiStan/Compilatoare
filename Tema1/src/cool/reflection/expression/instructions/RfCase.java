package cool.reflection.expression.instructions;

import cool.reflection.expression.RfExpression;
import cool.reflection.expression.RfId;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfCase extends RfExpression  {

    public static class RfCaseBranch extends RfExpression {

        private final RfId rfId;
        private final String type;
        private final RfExpression expression;

        public RfCaseBranch(RfId rfId, String type, RfExpression expression, Token token) {
            super(token);
            this.rfId = rfId;
            this.type = type;
            this.expression = expression;
        }

        public String getType() {
            return type;
        }

        public RfId getRfId() {
            return rfId;
        }

        public RfExpression getExpression() {
            return expression;
        }

        @Override
        public <T> T accept(ASTVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public String getSymbol() {
            return "case branch";
        }
    }

    private final RfExpression toEvaluate;
    private final List<RfCaseBranch> branches;

    public RfCase(RfExpression toEvaluate, List<RfCaseBranch> branches, Token token) {
        super(token);
        this.toEvaluate = toEvaluate;
        this.branches = branches;
    }

    public String getSymbol() {
        return token.getText();
    }

    public RfExpression getToEvaluate() {
        return toEvaluate;
    }

    public List<RfCaseBranch> getBranches() {
        return branches;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
