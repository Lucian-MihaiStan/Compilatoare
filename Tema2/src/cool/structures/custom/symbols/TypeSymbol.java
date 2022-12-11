package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;

public class TypeSymbol extends Symbol implements Scope {
    private final String parentScopeName;
    private Scope parentScope;

    public TypeSymbol(String symbolName, String parentSymbolName) {
        super(symbolName);
        this.parentScopeName = parentSymbolName;
    }

    @Override
    public boolean add(Symbol sym) {
        return false;
    }

    @Override
    public Symbol lookup(String str) {
        return null;
    }

    @Override
    public Scope getParent() {
        return parentScope;
    }

    public String getParentScopeName() {
        return parentScopeName;
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }
}
