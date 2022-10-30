package cool.tree;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class RfClass extends ASTNode {

    private final List<TerminalNode> types;
    private final List<RfFeature> rfFeatures;

    public RfClass(List<TerminalNode> type, List<RfFeature> rfFeatures, Token token) {
        super(token);
        this.types = type;
        this.rfFeatures = rfFeatures;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<TerminalNode> getTypes() {
        return types;
    }

    public List<RfFeature> getRfFeatures() {
        return rfFeatures;
    }
}
