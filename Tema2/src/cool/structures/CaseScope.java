package cool.structures;

import cool.structures.custom.symbols.IdSymbol;

public class CaseScope implements Scope {
    private final IdSymbol variableDeclared;
    private final Scope parentScope;

    public CaseScope(IdSymbol variableDeclared, Scope parentScope) {
        this.variableDeclared = variableDeclared;
        this.parentScope = parentScope;
    }

    @Override
    public boolean add(Symbol sym) {
        // do nothing. Not used
        return false;
    }

    @Override
    public Symbol lookup(String symbolName) {
        if (symbolName.equals(variableDeclared.getName()))
            return variableDeclared;

        return parentScope.lookup(symbolName);
    }

    @Override
    public Scope getParent() {
        return parentScope;
    }
}
