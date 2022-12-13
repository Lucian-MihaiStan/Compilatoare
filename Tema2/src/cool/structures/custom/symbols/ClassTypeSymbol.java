package cool.structures.custom.symbols;

import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassTypeSymbol extends Symbol implements Scope {
    private Scope parentScope;

    private final Map<String, IdSymbol> symbols = new LinkedHashMap<>();
    private final Map<String, MethodSymbol> methodsSymbols = new LinkedHashMap<>();

    public ClassTypeSymbol(String symbolName, Scope parentClass) {
        super(symbolName);
        this.parentScope = parentClass;
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

    @Override
    public Scope getParentWithClassType(Class<?> clazz) {
        return this;
    }

    public String getParentScopeName() {
        return parentScope instanceof ClassTypeSymbol ? ((ClassTypeSymbol) parentScope).getName() : null;
    }

    public void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }
}
