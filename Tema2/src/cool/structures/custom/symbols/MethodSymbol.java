package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodSymbol extends Symbol implements Scope {

    private final Scope definitionScope;
    private final Map<String, Symbol> parameters = new LinkedHashMap<>();
    private final String returnTypeName;

    private boolean isResolved;

    private Symbol returnTypeSymbol;

    public MethodSymbol(String name, String returnTypeName, Scope currentScope) {
        super(name);
        this.returnTypeName = returnTypeName;
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

    @Override
    public Scope getParentWithClassType(Class<?> clazz) {
        if (definitionScope == null)
            return null;

        Scope currentScope = definitionScope;
        while (!clazz.equals(currentScope.getClass())) {
            currentScope = currentScope.getParent();
            if (currentScope == null)
                return null;
        }

        return currentScope;
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

    public boolean isResolved() {
        return isResolved;
    }

    public void resolve() {
        isResolved = true;
        returnTypeSymbol = SymbolTable.globals.lookup(returnTypeName);
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}
