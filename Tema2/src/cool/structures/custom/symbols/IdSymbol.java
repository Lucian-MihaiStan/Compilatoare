package cool.structures.custom.symbols;

import cool.structures.Symbol;

public class IdSymbol extends Symbol {

    private ClassTypeSymbol classTypeSymbol;

    public IdSymbol(String name) {
        super(name);
    }

    public void setTypeSymbol(ClassTypeSymbol classTypeSymbol) {
        this.classTypeSymbol = classTypeSymbol;
    }

    public ClassTypeSymbol getTypeSymbol() {
        return classTypeSymbol;
    }
}
