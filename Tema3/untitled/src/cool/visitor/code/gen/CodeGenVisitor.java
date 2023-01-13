package cool.visitor.code.gen;

import cool.reflection.RfArgument;
import cool.reflection.RfClass;
import cool.reflection.RfProgram;
import cool.reflection.expression.*;
import cool.reflection.expression.arithmetic.RfArithmeticExpression;
import cool.reflection.expression.instructions.RfCase;
import cool.reflection.expression.instructions.RfIf;
import cool.reflection.expression.instructions.RfLet;
import cool.reflection.expression.instructions.RfWhile;
import cool.reflection.expression.relational.RfRelationalExpression;
import cool.reflection.expression.single.RfSingleValueExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.feature.RfFeature;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.structures.Scope;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.stringtemplate.v4.ST;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGenVisitor implements ASTVisitor<ST> {

    private final CodeGenManager manager;
    private int i;
    private int offset;

    private Scope scope;

    public CodeGenVisitor(CodeGenManager manager) {
        this.manager = manager;
    }

    @Override
    public ST visit(RfProgram rfProgram) {
        addDefaultClass(TypeSymbolConstants.OBJECT);
        addDefaultClass(TypeSymbolConstants.IO);
        addDefaultClass(TypeSymbolConstants.INT);
        addDefaultClass(TypeSymbolConstants.STRING);
        addDefaultClass(TypeSymbolConstants.BOOL);

        rfProgram.getRfClasses().forEach(clazz -> clazz.accept(this));

        return manager.getTemplate(CodeGenVisitorConstants.PROGRAM_PATTERN)
                .add(CodeGenVisitorConstants.INT_TAG,
                        manager.getTemplate(CodeGenVisitorConstants.DEFAULT_TAG_PATTERN)
                                .add(CodeGenVisitorConstants.NAME_TAG, TypeSymbolConstants.INT_STR.toLowerCase())
                                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.INT.getTag())
                )
                .add(CodeGenVisitorConstants.BOOL_TAG,
                        manager.getTemplate(CodeGenVisitorConstants.DEFAULT_TAG_PATTERN)
                                .add(CodeGenVisitorConstants.NAME_TAG, TypeSymbolConstants.BOOL_STR.toLowerCase())
                                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.BOOL.getTag())
                )
                .add(CodeGenVisitorConstants.STRING_TAG,
                        manager.getTemplate(CodeGenVisitorConstants.DEFAULT_TAG_PATTERN)
                                .add(CodeGenVisitorConstants.NAME_TAG, TypeSymbolConstants.STRING_STR.toLowerCase())
                                .add(CodeGenVisitorConstants.TAG_ID, TypeSymbolConstants.STRING.getTag())
                )
                .add(CodeGenVisitorConstants.STR_CONSTANTS, manager.getStrConstants())
                .add(CodeGenVisitorConstants.INT_CONSTANTS, manager.getIntConstants())
                .add(CodeGenVisitorConstants.BOOL_CONSTANTS, manager.getBoolConstants())
                .add(CodeGenVisitorConstants.CLASS_NAME_TAB, manager.getClassNameTab())
                .add(CodeGenVisitorConstants.CLASS_OBJECTS_TAB, manager.getClassObjectsTab())
                .add(CodeGenVisitorConstants.CLASS_PROTOTYPES_TAB, manager.getClassPrototypesTab())
                .add(CodeGenVisitorConstants.DISPATCH_TABLES, manager.getDispatchTables())
                .add(CodeGenVisitorConstants.HEAP_START_TAB, manager.getHeapStartTab())
                .add(CodeGenVisitorConstants.INIT_CONSTRUCT_TAB, manager.getInitMethods())
                .add(CodeGenVisitorConstants.CUSTOM_METHODS_TAB, manager.getCustomMethods());
    }

    void addDefaultClass(ClassTypeSymbol classTypeSymbol) {
        ST initConstructor = manager.getTemplate(CodeGenVisitorConstants.INIT_CONSTRUCTOR_PATTERN);
        if (initConstructor == null)
            throw new IllegalStateException("Unable to locate init constructor template");

        initConstructor.add(CodeGenVisitorConstants.CLASS_NAME, classTypeSymbol.getName());
        if (classTypeSymbol.getParent() instanceof ClassTypeSymbol)
            initConstructor.add(CodeGenVisitorConstants.PARENT_CLASS_NAME, ((ClassTypeSymbol) classTypeSymbol.getParent()).getName());

        manager.getInitMethods().add("e", initConstructor);
    }

    @Override
    public ST visit(RfClass rfClass) {
        scope = rfClass.getTypeSymbol();

        ST initConstructor = manager.getTemplate(CodeGenVisitorConstants.INIT_CONSTRUCTOR_PATTERN);
        if (initConstructor == null)
            throw new IllegalStateException("Unable to locate init constructor template");

        List<RfFeature> fields = rfClass.getRfFeatures().stream().filter(rfFeature -> rfFeature instanceof RfField).collect(Collectors.toList());

        StringBuilder fieldCodeGen = new StringBuilder();
        for (RfFeature field : fields) {
            ST codeGen = field.accept(this);
            if (codeGen != null)
                fieldCodeGen.append(codeGen.render());
        }

        initConstructor
                .add(CodeGenVisitorConstants.CLASS_NAME, ((ClassTypeSymbol) scope).getName())
                .add(CodeGenVisitorConstants.PARENT_CLASS_NAME, ((ClassTypeSymbol) scope).getParentScopeName())
                .add(CodeGenVisitorConstants.ATTRIB, fieldCodeGen);

        manager.getInitMethods().add("e", initConstructor);

        rfClass.getRfFeatures().forEach(method -> {
            if (method instanceof RfMethod)
                manager.getCustomMethods().add("e", method.accept(this)
                        .add(CodeGenVisitorConstants.CLASS_NAME, ((ClassTypeSymbol) scope).getName())
                        .add(CodeGenVisitorConstants.METHOD_NAME, ((RfMethod) method).getName().getText()));
        });

        return null;
    }

    @Override
    public ST visit(RfField rfField) {
        return null;
    }

    @Override
    public ST visit(RfInt rfInt) {
        return manager.getTemplate(CodeGenVisitorConstants.LITERAL_PATTERN).add(CodeGenVisitorConstants.ADDRESS_CONSTANT, manager.getIntConstantCount(rfInt.getValue()));
    }

    @Override
    public ST visit(RfArgument rfArgument) {
        return null;
    }

    @Override
    public ST visit(RfMethod rfMethod) {
        Scope currentScope = scope;

        scope = rfMethod.getMethodSymbol();

        ST customMethodTemplate = manager.getTemplate(CodeGenVisitorConstants.CUSTOM_METHOD_PATTERN);
        if (customMethodTemplate == null)
            throw new IllegalStateException("Unable to locate custom method template");

        ST bodySolve = rfMethod.getRfMethodBody().accept(this);
        customMethodTemplate
                .add(CodeGenVisitorConstants.METHOD_BODY, bodySolve)
                .add(CodeGenVisitorConstants.INCREMENT_STACK_NUMBER, rfMethod.getArgs().size() * 4 + 12);

        // return from the previous scope
        scope = currentScope;
        return customMethodTemplate;
    }

    @Override
    public ST visit(RfString rfString) {
        return null;
    }

    @Override
    public ST visit(RfBool rfBool) {
        return null;
    }

    @Override
    public ST visit(RfId rfId) {
        return null;
    }

    @Override
    public ST visit(RfArithmeticExpression rfArithmeticExpression) {
        return null;
    }

    @Override
    public ST visit(RfParenExpression rfParenExpression) {
        return null;
    }

    @Override
    public ST visit(RfRelationalExpression rfRelationalExpression) {
        return null;
    }

    @Override
    public ST visit(RfSingleValueExpression rfSingleValueExpression) {
        return null;
    }

    @Override
    public ST visit(RfAssignment rfAssignment) {
        return null;
    }

    @Override
    public ST visit(RfNewExpression rfNewExpression) {
        return null;
    }

    @Override
    public ST visit(RfDispatch rfDispatch) {
        return null;
    }

    @Override
    public ST visit(RfImplicitDispatch rfImplicitDispatch) {
        return null;
    }

    @Override
    public ST visit(RfIf rfIf) {
        return null;
    }

    @Override
    public ST visit(RfWhile rfWhile) {
        return null;
    }

    @Override
    public ST visit(RfLet rfLet) {
        return null;
    }

    @Override
    public ST visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        return null;
    }

    @Override
    public ST visit(RfCase rfCase) {
        return null;
    }

    @Override
    public ST visit(RfCase.RfCaseBranch rfCaseBranch) {
        return null;
    }

    @Override
    public ST visit(RfBody rfBody) {
        return null;
    }
}
