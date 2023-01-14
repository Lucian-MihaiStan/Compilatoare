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
import cool.structures.Symbol;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.LetSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.utils.ParserPath;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;
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
        RfExpression rfExpression = rfField.getRfExpression();
        if (rfExpression == null)
            return null;

        return manager.getTemplate(CodeGenVisitorConstants.INIT_ATTRIBUTE)
                .add(CodeGenVisitorConstants.DEFAULT_VALUE, rfExpression.accept(this))
                .add(CodeGenVisitorConstants.OFFSET, rfField.getIdSymbolName().getOffset());
    }

    @Override
    public ST visit(RfInt rfInt) {
        return manager
                .getTemplate(CodeGenVisitorConstants.LITERAL_PATTERN)
                .add(CodeGenVisitorConstants.ADDRESS_CONSTANT, manager.getIntConstantCount(rfInt.getValue()));
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
        return manager
                .getTemplate(CodeGenVisitorConstants.LITERAL_PATTERN)
                .add(CodeGenVisitorConstants.ADDRESS_CONSTANT, "str_const" + manager.addStringConstant(rfString.getValue()));
    }

    @Override
    public ST visit(RfBool rfBool) {
        return manager
                .getTemplate(CodeGenVisitorConstants.LITERAL_PATTERN)
                .add(CodeGenVisitorConstants.ADDRESS_CONSTANT, "bool_const" + (rfBool.getValue() ? "1" : "0"));
    }

    @Override
    public ST visit(RfId rfId) {
        ST idAttributeTemplate = manager.getTemplate(CodeGenVisitorConstants.LOAD_ID_ATTRIBUTE_VAR);
        if (idAttributeTemplate == null)
            throw new IllegalStateException("Unable to locate id attribute or local variable template");

        String id = rfId.getToken().getText();

        Symbol idSymbol = null;
        boolean isParameter = false;
        boolean isField = false;
        if (scope instanceof MethodSymbol) {
            Map<String, Symbol> parameters = ((MethodSymbol) scope).getParameters();
            idSymbol = parameters.get(id);
            if (idSymbol != null)
                isParameter = true;

            if (idSymbol == null) {
                Scope parentWithClassType = scope.getParentWithClassType(ClassTypeSymbol.class);
                idSymbol  = parentWithClassType.lookup(id);
                isField = idSymbol != null;
            }
        }

        boolean isDeclaredVariable = false;

        if (scope instanceof LetSymbol) {
            Map<String, Symbol> declaredVariables = ((LetSymbol) scope).getSymbols();
            idSymbol = declaredVariables.get(id);
            if (idSymbol != null)
                isDeclaredVariable = true;

            if (idSymbol == null) {
                Scope parentWithClassType = scope.getParentWithClassType(ClassTypeSymbol.class);
                idSymbol = parentWithClassType.lookup(id);
                isField = idSymbol != null;
            }
        }

        if (isDeclaredVariable && idSymbol instanceof IdSymbol) {
            idAttributeTemplate.add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.FP);
            idAttributeTemplate.add(CodeGenVisitorConstants.OFFSET, ((IdSymbol) idSymbol).getOffset());
            return idAttributeTemplate;
        }

        if (isParameter && idSymbol instanceof IdSymbol) {
            idAttributeTemplate.add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.FP);
            idAttributeTemplate.add(CodeGenVisitorConstants.OFFSET, ((IdSymbol) idSymbol).getOffset());
            return idAttributeTemplate;
        }

        if (isField && idSymbol instanceof IdSymbol) {
            idAttributeTemplate.add(CodeGenVisitorConstants.OFFSET, ((IdSymbol) idSymbol).getOffset());
            idAttributeTemplate
                    .add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.S0);
            return idAttributeTemplate;
        }

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
        ST value = rfAssignment.getExpr().accept(this);

        ST template = manager.getTemplate(CodeGenVisitorConstants.STORE_ID_ATTRIBUTE_VAR);
        if (template == null)
            throw new IllegalStateException("Unable to locate template " + CodeGenVisitorConstants.STORE_ID_ATTRIBUTE_VAR);

        template.add(CodeGenVisitorConstants.DEFAULT_VALUE, value);

        String symbol = rfAssignment.getId().getText();
        Symbol idSymbol = null;
        boolean isParameter = false;
        boolean isField = false;
        if (scope instanceof MethodSymbol) {
            Map<String, Symbol> parameters = ((MethodSymbol) scope).getParameters();
            idSymbol = parameters.get(symbol);
            if (idSymbol != null)
                isParameter = true;

            if (idSymbol == null) {
                Scope parentWithClassType = scope.getParentWithClassType(ClassTypeSymbol.class);
                idSymbol  = parentWithClassType.lookup(symbol);
                isField = idSymbol != null;
            }
        }

        boolean isDeclaredVariable = false;

        if (scope instanceof LetSymbol) {
            Map<String, Symbol> declaredVariables = ((LetSymbol) scope).getSymbols();
            idSymbol = declaredVariables.get(symbol);
            if (idSymbol != null)
                isDeclaredVariable = true;

            if (idSymbol == null) {
                Scope parentWithClassType = scope.getParentWithClassType(ClassTypeSymbol.class);
                idSymbol = parentWithClassType.lookup(symbol);
                isField = idSymbol != null;
            }
        }

        if (isDeclaredVariable && idSymbol instanceof IdSymbol) {
            template.add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.FP);
            template.add(CodeGenVisitorConstants.OFFSET, ((IdSymbol) idSymbol).getOffset());
            return template;
        }

        if (idSymbol instanceof IdSymbol)
            template.add(CodeGenVisitorConstants.OFFSET, ((IdSymbol) idSymbol).getOffset());

        if (isParameter) {
            template.add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.FP);
            return template;
        }

        if (isField && idSymbol instanceof IdSymbol) {
            template
                    .add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.S0);
            return template;
        }

        return null;
    }

    @Override
    public ST visit(RfNewExpression rfNewExpression) {
        return null;
    }

    @Override
    public ST visit(RfDispatch rfDispatch) {

        ST dispatchTemplate = manager.getTemplate(CodeGenVisitorConstants.DISPATCH_METHOD_PATTERN);
        if (dispatchTemplate == null)
            throw new IllegalStateException("Unable to locate dispatch method pattern");

        Token dispatchToken = rfDispatch.getDispatch();
        String methodDispatchName = dispatchToken.getText();
        dispatchTemplate
                .add(CodeGenVisitorConstants.METHOD_NAME, methodDispatchName)
                .add(CodeGenVisitorConstants.METHOD_ID, manager.getMethodId());

        List<RfExpression> args = rfDispatch.getParameters();
        Collections.reverse(args);

        StringBuilder argsRender = new StringBuilder();
        args.forEach(param -> argsRender.append(manager.getTemplate(CodeGenVisitorConstants.DISPATCH_ARG_PATTERN).add(CodeGenVisitorConstants.ARG, param.accept(this)).render()));

        ParserPath parserPath = manager.computeParserPathContext(dispatchToken, rfDispatch.getContext());

        dispatchTemplate
                .add(CodeGenVisitorConstants.ARGS, argsRender)
                .add(CodeGenVisitorConstants.PARSER_PATH, String.format(CodeGenVisitorConstants.STR_CONST_FORMAT, parserPath.getStringConstId()))
                .add(CodeGenVisitorConstants.PARSER_PATH_LINE, parserPath.getLine());

        Symbol callerType = rfDispatch.getCallerType();
        if (callerType instanceof  ClassTypeSymbol) {
            MethodSymbol methodSymbol = ((ClassTypeSymbol) callerType).lookUpMethod(methodDispatchName);
            if (methodSymbol == null)
                throw new IllegalStateException("Unable to locate method dispatch with name " + methodDispatchName);

            int offset = methodSymbol.getOffset();
            dispatchTemplate
                    .add(CodeGenVisitorConstants.OFFSET, offset);
        }

        RfExpression objectToCall = rfDispatch.getObjectToCall();
        if (objectToCall != null && !TypeSymbolConstants.SELF_STR.equals(objectToCall.getToken().getText())) {
            ST objectToCallST = objectToCall.accept(this);
            if (objectToCallST != null)
                dispatchTemplate.add(CodeGenVisitorConstants.CALLER, objectToCallST);
        }

        return dispatchTemplate;
    }

    @Override
    public ST visit(RfImplicitDispatch rfImplicitDispatch) {

        ST dispatchTemplate = manager.getTemplate(CodeGenVisitorConstants.DISPATCH_METHOD_PATTERN);
        if (dispatchTemplate == null)
            throw new IllegalStateException("Unable to locate dispatch method pattern");

        Token dispatchToken = rfImplicitDispatch.getDispatch();
        String methodDispatchName = dispatchToken.getText();
        dispatchTemplate
                .add(CodeGenVisitorConstants.METHOD_NAME, methodDispatchName)
                .add(CodeGenVisitorConstants.METHOD_ID, manager.getMethodId());

        List<RfExpression> args = rfImplicitDispatch.getParameters();
        Collections.reverse(args);

        StringBuilder argsRender = new StringBuilder();
        args.forEach(param -> argsRender.append(
                manager.getTemplate(CodeGenVisitorConstants.DISPATCH_ARG_PATTERN)
                        .add(CodeGenVisitorConstants.ARG, param.accept(this))
                        .render())
        );

        ParserPath parserPath = manager.computeParserPathContext(dispatchToken, rfImplicitDispatch.getContext());

        dispatchTemplate
                .add(CodeGenVisitorConstants.ARGS, argsRender)
                .add(CodeGenVisitorConstants.PARSER_PATH, String.format(CodeGenVisitorConstants.STR_CONST_FORMAT, parserPath.getStringConstId()))
                .add(CodeGenVisitorConstants.PARSER_PATH_LINE, parserPath.getLine());

        Symbol callerType = rfImplicitDispatch.getCallerType();
        if (callerType instanceof ClassTypeSymbol) {
            MethodSymbol methodSymbol = ((ClassTypeSymbol) callerType).lookUpMethod(methodDispatchName);
            if (methodSymbol == null)
                throw new IllegalStateException("Unable to locate method dispatch with name " + methodDispatchName);

            int offset = methodSymbol.getOffset();
            dispatchTemplate
                    .add(CodeGenVisitorConstants.OFFSET, offset);
        }

        return dispatchTemplate;
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
        ST pushStack = manager.getTemplate(CodeGenVisitorConstants.PUSH_STACK);
        if (pushStack == null)
            throw new IllegalStateException("Unable to locate template " + CodeGenVisitorConstants.PUSH_STACK);

        List<RfLet.RfDeclareVariable> vars = rfLet.getVars();
        pushStack.add(CodeGenVisitorConstants.OFFSET, vars.size() * (-4));

        ST template = manager.getTemplate(CodeGenVisitorConstants.SEQUENCE_SPACED_PATTERN);
        if (template == null)
            throw new IllegalStateException("Unable to locate template " + CodeGenVisitorConstants.SEQUENCE_SPACED_PATTERN);

        template.add(CodeGenVisitorConstants.E, pushStack);

        Scope currentScope = scope;
        scope = rfLet.getLetScope();

        int i = -4;
        for (RfLet.RfDeclareVariable var : vars) {
            var.getIdSymbol().setOffset(i);
            template.add(CodeGenVisitorConstants.E, var.accept(this));
            i -= 4;
        }

        template.add(CodeGenVisitorConstants.E, rfLet.getBody().accept(this));
        scope = currentScope;

        ST popStack = manager.getTemplate(CodeGenVisitorConstants.PUSH_STACK);
        if (popStack == null)
            throw new IllegalStateException("Unable to locate template " + CodeGenVisitorConstants.PUSH_STACK);

        popStack.add(CodeGenVisitorConstants.OFFSET, vars.size() * 4);
        template.add(CodeGenVisitorConstants.E, popStack);

        return template;
    }

    @Override
    public ST visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        ST storeIdVar = manager.getTemplate(CodeGenVisitorConstants.STORE_ID_ATTRIBUTE_VAR);
        if (storeIdVar == null)
            throw new IllegalStateException("Unable to locate store var attribute");

        RfExpression rfExpression = rfDeclareVariable.getValue();
        IdSymbol idSymbol = rfDeclareVariable.getIdSymbol();
        ClassTypeSymbol typeSymbol = idSymbol.getTypeSymbol();

        String value = null;
        if (rfExpression == null) {
            switch (typeSymbol.getName()) {
            case TypeSymbolConstants.INT_STR:
                value = CodeGenVisitorConstants.LOAD_ADDRESS_INT;
                break;
            case TypeSymbolConstants.BOOL_STR:
                value = CodeGenVisitorConstants.LOAD_ADDRESS_BOOL;
                break;
            case TypeSymbolConstants.STRING_STR:
                value = CodeGenVisitorConstants.LOAD_ADDRESS_STRING;
                break;
            }
        } else {
            value = rfExpression.accept(this).render();
        }

        storeIdVar.add(CodeGenVisitorConstants.DEFAULT_VALUE, value);
        storeIdVar.add(CodeGenVisitorConstants.OFFSET, idSymbol.getOffset());
        storeIdVar.add(CodeGenVisitorConstants.POINTER, CodeGenVisitorConstants.FP);

        return storeIdVar;
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
        ST template = manager.getTemplate(CodeGenVisitorConstants.SEQUENCE_PATTERN);
        if (template == null)
            throw new IllegalStateException("Unable to locate template " + CodeGenVisitorConstants.SEQUENCE_PATTERN);

        for (RfExpression expression : rfBody.getExpressions()) {
            ST expressionSolved = expression.accept(this);
            template.add(CodeGenVisitorConstants.E, expressionSolved);
        }

        return template;
    }
}
