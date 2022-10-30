package cool.tree;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class RfProgram extends ASTNode {

    private final List<RfClass> rfClasses;

    public RfProgram(List<RfClass> rfClasses, Token token) {
        super(token);
        this.rfClasses = rfClasses;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<RfClass> getRfClasses() {
        return rfClasses;
    }
}
