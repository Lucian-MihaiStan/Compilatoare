package cool.reflection.expression.instructions;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfCase extends RfExpression  {

    public static class RfCaseBranch extends RfExpression {

        private final Token id;
        private final Token type;
        private final RfExpression expression;

        public RfCaseBranch(CoolParser.CaseBranchContext ctx, Token id, Token type, RfExpression expression, Token token) {
            super(ctx, token);
            this.id = id;
            this.type = type;
            this.expression = expression;
        }

        public Token getType() {
            return type;
        }

        public Token getId() {
            return id;
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

    public RfCase(CoolParser.CaseContext ctx, RfExpression toEvaluate, List<RfCaseBranch> branches, Token token) {
        super(ctx, token);
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
