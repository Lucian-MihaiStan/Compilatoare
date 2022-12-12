package cool.structures;

import cool.reflection.RfClass;
import org.antlr.v4.runtime.Token;

import java.util.*;

public class DefaultScope implements Scope {
    
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private final Map<String, RfClass> globalClasses = new LinkedHashMap<>();
    
    private final Scope parent;
    
    public DefaultScope(Scope parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        // Reject duplicates in the same scope.
        if (symbols.containsKey(sym.getName()))
            return false;
        
        symbols.put(sym.getName(), sym);
        
        return true;
    }

    @Override
    public Symbol lookup(String name) {
        var sym = symbols.get(name);
        
        if (sym != null)
            return sym;
        
        if (parent != null)
            return parent.lookup(name);
        
        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        return symbols.values().toString();
    }

    @Override
    public void addClass(RfClass rfClass) {
        Token actualTokenType = rfClass.getActualTokenType();
        if (actualTokenType == null)
            throw new IllegalStateException("Unable to locate actual type of class " + rfClass);

        if (globalClasses.containsKey(actualTokenType.getText()))
            return;

        globalClasses.put(actualTokenType.getText(), rfClass);
    }

    @Override
    public RfClass getGlobalClassWithName(String name) {
        return globalClasses.get(name);
    }
}
