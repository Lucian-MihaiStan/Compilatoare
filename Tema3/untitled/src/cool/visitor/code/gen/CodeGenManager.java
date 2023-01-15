package cool.visitor.code.gen;

import cool.compiler.Compiler;
import cool.parser.CoolParser;
import cool.structures.DefaultScope;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.utils.Pair;
import cool.utils.ParserPath;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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
    private int methodId;

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

        initMethods = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        customMethods = templates.getInstanceOf(CodeGenVisitorConstants.SEQUENCE_PATTERN);
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
                                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.BOOL.getTag())
                                .add(CodeGenVisitorConstants.DEFAULT_VALUE, 0)
                )
                .add(CodeGenVisitorConstants.E,
                        templates.getInstanceOf(CodeGenVisitorConstants.BOOL_CONST_PATTERN)
                                .add(CodeGenVisitorConstants.COUNT, 1)
                                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.BOOL.getTag())
                                .add(CodeGenVisitorConstants.DEFAULT_VALUE,1)
                );

        return this;
    }

    public ST getHeapStartTab() {
        return heapStartTab;
    }

    private void addSymbolByName(String className) {
        Integer classCount = addStringConstant(className);
        if (classCount == null)
            return;

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
            genAttributesCode(classPrototypeObject, (ClassTypeSymbol) classSymbol);
            break;
        }

        classPrototypeObject.add(CodeGenVisitorConstants.SIZE, CodeGenVisitorConstants.DEFAULT_SIZE_PROTOTYPE + noFields);

        classPrototypesTab.add(CodeGenVisitorConstants.E, classPrototypeObject);

        // TODO Lucian here you should check that methods are not overridden
        List<Pair<ClassTypeSymbol, MethodSymbol>> methods = ((ClassTypeSymbol) classSymbol).gatherMethods();

        ST classDispatchTable = templates.getInstanceOf(CodeGenVisitorConstants.CLASS_DISPATCH_TABLE)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        methods.forEach(pair -> {
            ST methodDeclaration = templates.getInstanceOf(CodeGenVisitorConstants.METHOD_DECLARATION_DISPATCH_TABLE)
                    .add(CodeGenVisitorConstants.CLASS_NAME, pair.getKey())
                    .add(CodeGenVisitorConstants.METHOD_NAME, pair.getValue());
            classDispatchTable.add(CodeGenVisitorConstants.METHODS, methodDeclaration);
        });

        dispatchTables.add(CodeGenVisitorConstants.E, classDispatchTable);

        ST objectDeclarationTab = templates.getInstanceOf(CodeGenVisitorConstants.OBJECT_DECLARATION_TAB_PATTERN)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        classObjectsTab.add(CodeGenVisitorConstants.E, objectDeclarationTab);
    }

    private void genAttributesCode(ST classPrototypeObject, ClassTypeSymbol classSymbol) {

        StringBuilder sb = new StringBuilder();

        List<Symbol> allFields = classSymbol.getAllFields();

        Collections.reverse(allFields);
        allFields.forEach(field -> {
            if (!(field instanceof IdSymbol))
                throw new IllegalStateException("Unknown field type " + field);

            ClassTypeSymbol typeSymbol = ((IdSymbol) field).getTypeSymbol();
            if (typeSymbol == TypeSymbolConstants.STRING)
                sb.append("\t.word\tstr_const0\n");
            else if (typeSymbol == TypeSymbolConstants.INT)
                sb.append("\t.word\tint_const0\n");
            else if (typeSymbol == TypeSymbolConstants.BOOL)
                sb.append("\t.word\tbool_const0\n");
            else
                sb.append("\t.word\t0\n");
        });

        classPrototypeObject.add(CodeGenVisitorConstants.ATTRIB, sb.toString());
    }

    public Integer addStringConstant(String className) {
        if (stringMIPSConstants.containsKey(className))
            return stringMIPSConstants.get(className);

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

        generateStringConstant(className, classCount, intConstId);
        return classCount;
    }

    private void generateStringConstant(String className, int classCount, int intConstId) {
        ST strConst = templates.getInstanceOf(CodeGenVisitorConstants.STR_CONST_PATTERN)
                .add(CodeGenVisitorConstants.COUNT, classCount)
                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.STRING.getTag())
                .add(CodeGenVisitorConstants.SIZE, (className.length() + 1) / 4 + 5) // TODO Lucian replace this formula here 'cause it's wrong
                .add(CodeGenVisitorConstants.CLASS_NAME_LENGTH, CodeGenVisitorConstants.INT_CONST + intConstId)
                .add(CodeGenVisitorConstants.CLASS_NAME, className);

        strConstants.add(CodeGenVisitorConstants.E, strConst);
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
            return "int_const" + intMIPSConstants.get(value);

        intMIPSConstants.put(value, value);

        ST intConst = templates.getInstanceOf(CodeGenVisitorConstants.INT_CONST_PATTERN)
                .add(CodeGenVisitorConstants.COUNT, value)
                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.INT.getTag())
                .add(CodeGenVisitorConstants.DEFAULT_VALUE, value);
        intConstants.add(CodeGenVisitorConstants.E, intConst);

        return "int_const" + value;
    }

    public int getMethodId() {
        return methodId++;
    }

    public ParserPath computeParserPathContext(Token dispatchToken, ParserRuleContext context) {
        if (context == null)
            throw new IllegalStateException("Null context passed");

        ParserRuleContext currentContext = context;

        while (!(currentContext.getParent() instanceof CoolParser.ProgramContext))
            currentContext = currentContext.getParent();

        String filePath = Compiler.fileNames.get(currentContext);
        if (filePath == null)
            throw new IllegalStateException("Unable to compute the file path for dispatch " + dispatchToken.getText() + " in context " + context);
        String[] segmentsPath = filePath.split("/");
        String parserPath = segmentsPath[segmentsPath.length - 1];

        Integer stringConstId = addStringConstant(parserPath);

        return new ParserPath(parserPath, dispatchToken.getLine(), stringConstId);
    }

}
