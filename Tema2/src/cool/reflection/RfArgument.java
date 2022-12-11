package cool.reflection;

import cool.parser.CoolParser;
import cool.structures.Symbol;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.TypeSymbol;
import cool.tree.ASTNode;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfArgument extends ASTNode {

    private final Token name;
    private final Token type;
    private IdSymbol idSymbol;

    public RfArgument(CoolParser.FormalContext ctx, Token name, Token type, Token token) {
        super(ctx, token);
        this.name = name;
        this.type = type;
    }

    public Token getName() {
        return name;
    }

    public Token getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return (type == null ? "null" : type.getText()) + " : " + (name == null ? "null" : name.getText());
    }

    public void setIdSymbol(IdSymbol argSymbol) {
        this.idSymbol = argSymbol;
    }

    public IdSymbol getIdSymbolName() {
        return idSymbol;
    }

    public void setIdSymbolType(Symbol symbolType) {
        if (!(symbolType instanceof TypeSymbol))
            throw new IllegalStateException("Unmatched provided type for type symbol " + symbolType + " in context of argument " + this);

        idSymbol.setTypeSymbol((TypeSymbol) symbolType);
    }
}
