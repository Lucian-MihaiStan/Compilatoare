package cool.structures.custom.symbols;

import cool.structures.Symbol;

public class IdSymbol extends Symbol {

    private TypeSymbol typeSymbol;

    public IdSymbol(String name) {
        super(name);
    }

    public void setTypeSymbol(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }
}
