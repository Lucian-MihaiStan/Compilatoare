package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassTypeSymbol extends Symbol implements Scope {
    private final String parentScopeName;
    private Scope parentScope;

    private final Map<String, IdSymbol> symbols = new LinkedHashMap<>();
    private final Map<String, MethodSymbol> methodsSymbols = new LinkedHashMap<>();

    public ClassTypeSymbol(String symbolName, String parentSymbolName) {
        super(symbolName);
        this.parentScopeName = parentSymbolName;
    }

    @Override
    public boolean add(Symbol sym) {
        if (sym instanceof IdSymbol) {
            if (symbols.containsKey(sym.getName()))
                return false;

            symbols.put(sym.getName(), (IdSymbol) sym);
            return true;
        }

        if (sym instanceof MethodSymbol) {
            if (methodsSymbols.containsKey(sym.getName()))
                return false;

            methodsSymbols.put(sym.getName(), (MethodSymbol) sym);
            return true;
        }

        return false;
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

    public MethodSymbol lookUpMethod(String methodName) {
        Symbol sym = methodsSymbols.get(methodName);

        if (sym != null)
            return (MethodSymbol) sym;

        if (parentScope instanceof ClassTypeSymbol)
            return ((ClassTypeSymbol) parentScope).lookUpMethod(methodName);

        return null;
    }

    @Override
    public Scope getParent() {
        return parentScope;
    }

    public String getParentScopeName() {
        return parentScopeName;
    }

    public void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }
}
