package cool.structures.custom.symbols;

import com.sun.jdi.Method;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.utils.Pair;
import cool.visitor.code.gen.CodeGenVisitorConstants;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ClassTypeSymbol extends Symbol implements Scope {
    private Scope parentScope;

    private final Map<String, IdSymbol> symbols = new LinkedHashMap<>();
    private final Map<String, MethodSymbol> methodsSymbols = new LinkedHashMap<>();

    private final List<ClassTypeSymbol> childrenClasses = new ArrayList<>();

    private int tag;

    public static int maxTag;

    public ClassTypeSymbol(String symbolName, Scope parentClass) {
        super(symbolName);
        this.parentScope = parentClass;

        if (this.parentScope instanceof ClassTypeSymbol && this.parentScope != TypeSymbolConstants.SELF_TYPE)
            ((ClassTypeSymbol) this.parentScope).addChildren(this);

    }

    private void addChildren(ClassTypeSymbol childSymbol) {
        childrenClasses.add(childSymbol);
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
        MethodSymbol sym = methodsSymbols.get(methodName);

        if (sym != null)
            return sym;

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

    public void visitTag() {
        this.tag = maxTag;

        childrenClasses.forEach(child -> {
            if (child != TypeSymbolConstants.SELF_TYPE) {
                ++maxTag;
                child.visitTag();
            }
        });
    }

    public int getTag() {
        return tag;
    }

    public int getNoFields() {
        int count = 0;
        ClassTypeSymbol currentSymbol = this;

        while (currentSymbol != null) {
            count += currentSymbol.getSymbols().size();
            currentSymbol = (ClassTypeSymbol) currentSymbol.getParent();
        }

        return count;
    }

    public Map<String, IdSymbol> getSymbols() {
        return symbols;
    }

    public List<Symbol> getAllFields() {
        List<Symbol> allFields = new ArrayList<>();

        ClassTypeSymbol currentSymbol = this;

        while (currentSymbol != null) {
            allFields.addAll(currentSymbol.getSymbols().values());
            currentSymbol = (ClassTypeSymbol) currentSymbol.getParent();
        }

        return allFields;
    }

    public List<Pair<ClassTypeSymbol, MethodSymbol>> gatherMethods() {
        List<Pair<ClassTypeSymbol, MethodSymbol>> methods = new ArrayList<>();

        ClassTypeSymbol currentSymbol = this;

        while (currentSymbol != null) {
            final ClassTypeSymbol thisSymbol = currentSymbol;

            thisSymbol.getMethodsSymbols().forEach((methodName, methodSymbol) -> methods.add(new Pair<>(thisSymbol, methodSymbol)));

            currentSymbol = (ClassTypeSymbol) currentSymbol.getParent();
        }

        Collections.reverse(methods);
        return methods;
    }

    public Map<String, MethodSymbol> getMethodsSymbols() {
        return methodsSymbols;
    }

    public int getParentLastOffset() {
        int currentTotalAttributes = symbols.size() * 4;
        Scope currentScopeEval = parentScope;
        while (currentScopeEval instanceof ClassTypeSymbol) {
            Map<String, IdSymbol> parentsSymbols = ((ClassTypeSymbol) currentScopeEval).getSymbols();
            currentTotalAttributes += parentsSymbols.size() * 4;
            currentScopeEval = currentScopeEval.getParent();
        }

        return currentTotalAttributes;
    }
}
