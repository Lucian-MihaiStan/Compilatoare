package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;

public class IdSymbol extends Symbol {

    private final String type;
    private boolean isResolved;
    private ClassTypeSymbol classTypeSymbol;

    private ClassTypeSymbol currentTSelfTypeSymbol;

    public IdSymbol(String name, String type) {
        super(name);
        this.type = type;
    }

    public void setTypeSymbol(ClassTypeSymbol classTypeSymbol) {
        this.classTypeSymbol = classTypeSymbol;
    }

    public ClassTypeSymbol getTypeSymbol() {
        return classTypeSymbol;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void resolve() {
        isResolved = true;
        classTypeSymbol = (ClassTypeSymbol) SymbolTable.globals.lookup(type);
    }

    public void setCurrentSelfTypeSymbol(ClassTypeSymbol currentScope) {
        currentTSelfTypeSymbol = currentScope;
    }

    public ClassTypeSymbol getCurrentTSelfTypeSymbol() {
        return currentTSelfTypeSymbol;
    }
}
