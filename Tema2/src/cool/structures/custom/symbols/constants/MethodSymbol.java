package cool.structures.custom.symbols.constants;

import cool.structures.Scope;
import cool.structures.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodSymbol extends Symbol implements Scope {

    private final Scope definitionScope;
    private final Map<String, Symbol> parameters = new LinkedHashMap<>();

    private Symbol returnTypeSymbol;

    public MethodSymbol(String name, Scope currentScope) {
        super(name);
        definitionScope = currentScope;
    }

    @Override
    public boolean add(Symbol sym) {
        if (parameters.containsKey(sym.getName()))
            return false;

        parameters.put(sym.getName(), sym);

        return true;
    }

    @Override
    public Symbol lookup(String symbolName) {
        Symbol sym = parameters.get(symbolName);

        if (sym != null)
            return sym;

        if (definitionScope != null)
            return definitionScope.lookup(symbolName);

        return null;
    }

    @Override
    public Scope getParent() {
        return definitionScope;
    }

    public Scope getDefinitionScope() {
        return definitionScope;
    }

    public Map<String, Symbol> getParameters() {
        return parameters;
    }

    public Symbol getReturnTypeSymbol() {
        return returnTypeSymbol;
    }

    public void setReturnTypeSymbol(Symbol returnTypeSymbol) {
        this.returnTypeSymbol = returnTypeSymbol;
    }
}
