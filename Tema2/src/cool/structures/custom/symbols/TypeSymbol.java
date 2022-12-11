package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeSymbol extends Symbol implements Scope {
    private final String parentScopeName;
    private Scope parentScope;

    private final Map<String, Symbol> symbols = new LinkedHashMap<>();

    public TypeSymbol(String symbolName, String parentSymbolName) {
        super(symbolName);
        this.parentScopeName = parentSymbolName;
    }

    @Override
    public boolean add(Symbol sym) {
        if (symbols.containsKey(sym.getName()))
            return false;

        symbols.put(sym.getName(), sym);

        return true;
    }

    @Override
    public Symbol lookup(String symbolName) {
        Symbol sym = symbols.get(symbolName);

        if (sym != null)
            return sym;

        if (parentScope != null)
            return parentScope.lookup(symbolName);

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
