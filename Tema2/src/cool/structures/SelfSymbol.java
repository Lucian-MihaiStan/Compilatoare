package cool.structures;

import cool.structures.custom.symbols.ClassTypeSymbol;

public class SelfSymbol extends Symbol {

    private final ClassTypeSymbol scope;

    public SelfSymbol(String name, Scope scope) {
        super(name);
        this.scope = (ClassTypeSymbol) scope.getParentWithClassType(ClassTypeSymbol.class);
    }

    public ClassTypeSymbol getScope() {
        return scope;
    }
}
