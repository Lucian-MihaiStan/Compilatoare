package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class LetSymbol extends Symbol implements Scope {

    private final Scope enclosingScope;

    private final Map<String, Symbol> symbols = new LinkedHashMap<>();

    public LetSymbol(String name, Scope parentScope) {
        super(name);
        this.enclosingScope = parentScope;
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

        if (enclosingScope != null)
            return enclosingScope.lookup(symbolName);

        return null;
    }

    @Override
    public Scope getParent() {
        return enclosingScope;
    }

}
