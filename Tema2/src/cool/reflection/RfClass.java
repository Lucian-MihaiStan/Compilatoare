package cool.reflection;

import cool.parser.CoolParser;
import cool.reflection.feature.RfFeature;
import cool.structures.custom.symbols.TypeSymbol;
import cool.tree.ASTNode;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class RfClass extends ASTNode {

    private final List<TerminalNode> types;
    private final List<RfFeature> rfFeatures;

    private TypeSymbol typeSymbol;

    public RfClass(CoolParser.ClassContext ctx, List<TerminalNode> type, List<RfFeature> rfFeatures, Token token) {
        super(ctx, token);
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

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    public void setTypeSymbol(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public Token getActualTokenType() {
        if (types == null || types.isEmpty())
            throw new IllegalStateException("Unable to compute type of current class");

        TerminalNode actualTerminalNode = types.get(0);
        if (actualTerminalNode == null)
            throw new IllegalStateException("Unable to compute type of current class");

        return actualTerminalNode.getSymbol();
    }

    public Token getInheritedTokenType() {
        if (types == null || types.isEmpty())
            throw new IllegalStateException("Unable to compute inherited type of current class");

        if (types.size() == 2)
            return types.get(1).getSymbol();

        return null;
    }

    @Override
    public String toString() {
        return "Class " + getActualTokenType().getText();
    }
}
