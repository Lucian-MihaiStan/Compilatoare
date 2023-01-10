package cool.structures;

import cool.reflection.RfClass;
import cool.structures.custom.symbols.ClassTypeSymbol;

public interface Scope {
    public boolean add(Symbol sym);
    
    public Symbol lookup(String str);
    
    public Scope getParent();

    default void addClass(RfClass rfClass) {
        // do nothing
    }

    default RfClass getGlobalClassWithName(String name) {
        return null;
    }

    Scope getParentWithClassType(Class<?> clazz);
}
