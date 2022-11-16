import java.util.*;

// O functie este atât simbol, cât și domeniu de vizibilitate pentru parametrii
// săi formali.

/*
 TODO 1: Implementați clasa FunctionSymbol, suprascriind metodele din interfață
        și adăugându-i un nume.
 */
public class FunctionSymbol extends IdSymbol implements Scope {

    private final Scope parent;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();

    public FunctionSymbol(String name, Scope parentScope) {
        super(name);
        this.parent = parentScope;
    }

    @Override
    public boolean add(Symbol s) {
        if (symbols.containsKey(s.getName()))
            return false;

        symbols.put(s.getName(), s);
        return true;
    }

    @Override
    public Symbol lookup(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null)
            return symbol;

        if (parent != null)
            return parent.lookup(name);

        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
}