package cool.visitor.code.gen;

import cool.structures.DefaultScope;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.utils.Pair;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class CodeGenManager {

    private final STGroupFile templates;

    private final ST strConstants;
    private final ST classNameTab;
    private final ST classPrototypesTab;
    private final ST classObjectsTab;
    private final ST dispatchTables;
    private final ST initMethods;
    private final ST customMethods;
    private final ST intConstants;
    private final ST heapStartTab;
    private final ST boolConstants;

    private int strConstNumber;

    private int intConstNumber;

    private int classPrototypeObjectNumber;

    private final Map<Integer, Integer> intMIPSConstants = new HashMap<>();

    private final Map<String, Integer> stringMIPSConstants = new HashMap<>();

    public CodeGenManager(String codeGenStgPath) {
        templates = new STGroupFile(codeGenStgPath);
        strConstants = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        classNameTab = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        classPrototypesTab = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        dispatchTables = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        intConstants = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        boolConstants = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        classObjectsTab = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        heapStartTab = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);

        initMethods = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_SPACED_PATTERN);
        customMethods = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_SPACED_PATTERN);
    }

    public STGroupFile getTemplates() {
        return templates;
    }

    public ST getStrConstants() {
        return strConstants;
    }

    public ST getClassNameTab() {
        return classNameTab;
    }

    public ST getClassPrototypesTab() {
        return classPrototypesTab;
    }

    public ST getDispatchTables() {
        return dispatchTables;
    }

    public ST getCustomMethods() {
        return customMethods;
    }

    public ST getBoolConstants() {
        return boolConstants;
    }

    public ST getInitMethods() {
        return initMethods;
    }

    public CodeGenManager visitSymbols() {

        addSymbolByName(CodeGenVisitorConstants.EMPTY_STRING);

        Scope globals = SymbolTable.globals;
        if (!(globals instanceof DefaultScope))
            throw new IllegalStateException("Unable to compute the global scope");

        Map<String, Symbol> symbolsName = ((DefaultScope) globals).getSymbols();
        if (symbolsName == null || symbolsName.isEmpty())
            throw new IllegalStateException("There should be at least Main, Object, Int, String, IO, Bool symbols in global scope");

        Collection<Symbol> symbols = symbolsName.values();
        List<Symbol> sortedSymbols = symbols.stream().sorted(Comparator.comparingInt(s -> ((ClassTypeSymbol) s).getTag())).collect(Collectors.toList());

        for (Symbol symbol : sortedSymbols) {
            if (symbol == TypeSymbolConstants.SELF_TYPE)
                continue;

            String className = symbol.getName();
            addSymbolByName(className);

        }

        ST objectInits = templates.getInstanceOf(CodeGenVisitorConstants.HEAP_START_PATTERN);

        objectInits
                .add(CodeGenVisitorConstants.OBJECT_INITS,
                templates.getInstanceOf(CodeGenVisitorConstants.GLOBAL_OBJECT_INIT_PATTERN)
                    .add(CodeGenVisitorConstants.CLASS_NAME, TypeSymbolConstants.INT_STR))
                .add(CodeGenVisitorConstants.OBJECT_INITS,
                        templates.getInstanceOf(CodeGenVisitorConstants.GLOBAL_OBJECT_INIT_PATTERN)
                                .add(CodeGenVisitorConstants.CLASS_NAME, TypeSymbolConstants.STRING_STR))
                .add(CodeGenVisitorConstants.OBJECT_INITS,
                        templates.getInstanceOf(CodeGenVisitorConstants.GLOBAL_OBJECT_INIT_PATTERN)
                                .add(CodeGenVisitorConstants.CLASS_NAME, TypeSymbolConstants.BOOL_STR))
                .add(CodeGenVisitorConstants.OBJECT_INITS,
                        templates.getInstanceOf(CodeGenVisitorConstants.GLOBAL_OBJECT_INIT_PATTERN)
                                .add(CodeGenVisitorConstants.CLASS_NAME, TypeSymbolConstants.MAIN_STR));

        heapStartTab
                .add(CodeGenVisitorConstants.E, objectInits)
                .add(CodeGenVisitorConstants.E, CodeGenVisitorConstants.MAIN_DOT_MAIN);


        boolConstants
                .add(CodeGenVisitorConstants.E,
                        templates.getInstanceOf(CodeGenVisitorConstants.BOOL_CONST_PATTERN)
                                .add(CodeGenVisitorConstants.COUNT, 0)
                                .add(CodeGenVisitorConstants.TAG_ID, stringMIPSConstants.get(TypeSymbolConstants.BOOL_STR))
                                .add(CodeGenVisitorConstants.DEFAULT_VALUE, 0)
                )
                .add(CodeGenVisitorConstants.E,
                        templates.getInstanceOf(CodeGenVisitorConstants.BOOL_CONST_PATTERN)
                                .add(CodeGenVisitorConstants.COUNT, 1)
                                .add(CodeGenVisitorConstants.TAG_ID, stringMIPSConstants.get(TypeSymbolConstants.BOOL_STR))
                                .add(CodeGenVisitorConstants.DEFAULT_VALUE,1)
                );

        return this;
    }

    public ST getHeapStartTab() {
        return heapStartTab;
    }

    private void addSymbolByName(String className) {
        if (stringMIPSConstants.containsKey(className))
            return;

        int classCount = strConstNumber++;

        stringMIPSConstants.put(className, classCount);

        int intConstId;

        if (!intMIPSConstants.containsKey(className.length())) {
            ST intConst = templates.getInstanceOf(CodeGenVisitorConstants.INT_CONST_PATTERN)
                    .add(CodeGenVisitorConstants.COUNT,  intMIPSConstants.size())
                    .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.INT.getTag())
                    .add(CodeGenVisitorConstants.DEFAULT_VALUE, className.length());

            intConstId = intMIPSConstants.size();
            intConstants.add(CodeGenVisitorConstants.E, intConst);
            intMIPSConstants.put(className.length(), intConstId);
        } else {
            intConstId = intMIPSConstants.get(className.length());
        }

        ST strConst = templates.getInstanceOf(CodeGenVisitorConstants.STR_CONST_PATTERN)
                .add(CodeGenVisitorConstants.COUNT, classCount)
                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.STRING.getTag())
                .add(CodeGenVisitorConstants.SIZE, (className.length() + 1) / 4 + 5) // TODO Lucian replace this formula here 'cause it's wrong
                .add(CodeGenVisitorConstants.CLASS_NAME_LENGTH, CodeGenVisitorConstants.INT_CONST + intConstId)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        strConstants.add(CodeGenVisitorConstants.E, strConst);

        if (CodeGenVisitorConstants.EMPTY_STRING.equals(className))
            return;

        classNameTab.add(CodeGenVisitorConstants.E, String.format(CodeGenVisitorConstants.CLASS_NAME_TAB_FORMAT, classCount));

        Scope globals = SymbolTable.globals;
        if (!(globals instanceof DefaultScope))
            throw new IllegalStateException("Unknown global scope definition");

        DefaultScope globalScope = (DefaultScope) globals;
        Symbol classSymbol = globalScope.getClassSymbol(className);
        if (!(classSymbol instanceof ClassTypeSymbol))
            throw new IllegalStateException("Unable to locate class type symbol with name " + className);

        int noFields = ((ClassTypeSymbol) classSymbol).getNoFields();
        List<Symbol> allFields = ((ClassTypeSymbol) classSymbol).getAllFields();

        if (noFields != allFields.size())
            throw new IllegalStateException("Number of fields are different by the number of fields gather from depth traversal");

        ST classPrototypeObject = templates.getInstanceOf(CodeGenVisitorConstants.CLASS_PROTOTYPE_OBJ_PATTERN)
                .add(CodeGenVisitorConstants.CLASS_NAME, className)
                .add(CodeGenVisitorConstants.TAG_ID, classPrototypeObjectNumber++);

        switch (className) {
        case TypeSymbolConstants.INT_STR:
            classPrototypeObject.add(CodeGenVisitorConstants.ATTRIB, CodeGenVisitorConstants.DEFAULT_INT_PROTOTYPE_VALUE);
            noFields += 1;
            break;
        case TypeSymbolConstants.STRING_STR:
            classPrototypeObject.add(CodeGenVisitorConstants.ATTRIB, CodeGenVisitorConstants.DEFAULT_STRING_PROTOTYPE_VALUE);
            noFields += 2;
            break;
        case TypeSymbolConstants.BOOL_STR:
            classPrototypeObject.add(CodeGenVisitorConstants.ATTRIB, CodeGenVisitorConstants.DEFAULT_BOOL_PROTOTYPE_VALUE);
            noFields += 1;
            break;
        default:
        }

        classPrototypeObject.add(CodeGenVisitorConstants.SIZE, CodeGenVisitorConstants.DEFAULT_SIZE_PROTOTYPE + noFields);

        classPrototypesTab.add(CodeGenVisitorConstants.E, classPrototypeObject);

        List<Pair<ClassTypeSymbol, MethodSymbol>> methods = ((ClassTypeSymbol) classSymbol).gatherMethods();

        ST classDispatchTable = templates.getInstanceOf(CodeGenVisitorConstants.CLASS_DISPATCH_TABLE)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        methods.forEach(pair -> {
            ST methodDeclaration = templates.getInstanceOf(CodeGenVisitorConstants.METHOD_DECLARATION_DISPATCH_TABLE)
                    .add(CodeGenVisitorConstants.CLASS_NAME, pair.getKey())
                    .add(CodeGenVisitorConstants.METHOD_NAME, pair.getValue());
            classDispatchTable.add(CodeGenVisitorConstants.METHODS, methodDeclaration);
        });

        if (CodeGenVisitorConstants.MAIN.equals(className))
            classDispatchTable.add(CodeGenVisitorConstants.METHODS, CodeGenVisitorConstants.HEAP_START);

        dispatchTables.add(CodeGenVisitorConstants.E, classDispatchTable);

        ST objectDeclarationTab = templates.getInstanceOf(CodeGenVisitorConstants.OBJECT_DECLARATION_TAB_PATTERN)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        classObjectsTab.add(CodeGenVisitorConstants.E, objectDeclarationTab);
    }

    public ST getClassObjectsTab() {
        return classObjectsTab;
    }

    public ST getTemplate(String templateName) {
        return templates.getInstanceOf(templateName);
    }

    public ST getIntConstants() {
        return intConstants;
    }

    public String getIntConstantCount(Integer value) {
        if (intMIPSConstants.containsKey(value))
            return String.valueOf(intMIPSConstants.get(value));

        intMIPSConstants.put(value, value);

        ST intConst = templates.getInstanceOf(CodeGenVisitorConstants.INT_CONST_PATTERN)
                .add(CodeGenVisitorConstants.COUNT,  intMIPSConstants.size())
                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.INT.getTag())
                .add(CodeGenVisitorConstants.DEFAULT_VALUE, value);
        intConstants.add(CodeGenVisitorConstants.E, intConst);

        return String.valueOf(value);
    }
}
