package cool.visitor.resolution;

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
import cool.reflection.expression.single.value.RfBitNegExpression;
import cool.reflection.expression.single.value.RfIsVoidExpression;
import cool.reflection.expression.single.value.RfNotExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.structures.*;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.LetSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ResolutionPassVisitor implements ASTVisitor<Symbol> {

    Scope currentScope;

    @Override
    public Symbol visit(RfProgram rfProgram) {
        rfProgram.getRfClasses().forEach(rfClass -> rfClass.accept(this));
        return null;
    }

    @Override
    public Symbol visit(RfClass rfClass) {
        ClassTypeSymbol classScope = rfClass.getTypeSymbol();
        if (classScope == null)
            return null;

        checkClassHierarchy(rfClass);

        currentScope = classScope;
        rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));

        return null;
    }

    private void checkClassHierarchy(RfClass rfClass) {
        RfClass currentInspectedRfClass = rfClass;

        ClassTypeSymbol classType = currentInspectedRfClass.getTypeSymbol();

        Symbol parentSymbol;

        while (classType != TypeSymbolConstants.OBJECT) {
            parentSymbol = (Symbol) classType.getParent();

            // here are two cases if the parent wasn't set in the definition pass due to forward references, otherwise he doesn't really exit, so we try to search one more time in globals
            if (parentSymbol == null) {
                if (currentInspectedRfClass.getInheritedTokenType() == null)
                    throw new IllegalStateException("Unable to locate inherited type token of class " + currentInspectedRfClass);

                parentSymbol = SymbolTable.globals.lookup(currentInspectedRfClass.getInheritedTokenType().getText());
                if (parentSymbol == null) {
                    SymbolTable.error(currentInspectedRfClass.getContext(), currentInspectedRfClass.getInheritedTokenType(), new StringBuilder().append("Class ").append(currentInspectedRfClass.getToken().getText()).append(" has undefined parent ").append(currentInspectedRfClass.getInheritedTokenType().getText()).toString());
                    return;
                }
            }

            if (!(parentSymbol instanceof ClassTypeSymbol))
                throw new IllegalStateException("Undefined parent symbol");

            if (currentInspectedRfClass == null)
                return;

            currentInspectedRfClass.getTypeSymbol().setParentScope((ClassTypeSymbol) parentSymbol);

            classType = (ClassTypeSymbol) parentSymbol;
            currentInspectedRfClass = SymbolTable.globals.getGlobalClassWithName(classType.getName());

            if (classType == rfClass.getTypeSymbol()) {
                SymbolTable.error(rfClass.getContext(), rfClass.getToken(), new StringBuilder().append("Inheritance cycle for class ").append(rfClass.getActualTokenType().getText()).toString());
                return;
            }
        }
    }

    @Override
    public Symbol visit(RfField rfField) {
        // if the following condition is true it means the name of field was illegal
        if (rfField.getIdSymbolName() == null)
            return null;

        Token fieldName = rfField.getFieldName();
        if (fieldName == null)
            throw new IllegalStateException("Unable to find name token for field " + rfField);

        Token fieldType = rfField.getFieldType();
        if  (fieldType == null)
            throw new IllegalStateException("Unable to find type token for field " + rfField);

        Scope parent = currentScope.getParent();
        if (parent == null)
            throw new IllegalStateException("Unable to locate parent scope of scope " + currentScope);

        Symbol symbolInherited = parent.lookup(fieldName.getText());
        if (symbolInherited != null) {
            if (!(currentScope instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unable to log error for redefined field " + rfField + " in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldName, new StringBuilder().append("Class ").append(((ClassTypeSymbol) currentScope).getName()).append(" redefines inherited attribute ").append(fieldName.getText()).toString());
            return null;
        }

        String typeName = fieldType.getText();
        Symbol fieldSymbolType = SymbolTable.globals.lookup(typeName);

        if (fieldSymbolType == TypeSymbolConstants.SELF_TYPE)
            fieldSymbolType = (Symbol) currentScope.getParentWithClassType(ClassTypeSymbol.class);

        if (fieldSymbolType == null) {
            if (!(currentScope instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unable to log error for undefined type " + typeName + " of field " + rfField + " in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldType, new StringBuilder().append("Class ").append(((ClassTypeSymbol) currentScope).getName()).append(" has attribute ").append(fieldName.getText()).append(" with undefined type ").append(typeName).toString());
            return null;
        }

        rfField.setIdSymbolType(fieldSymbolType);

        if (rfField.getRfExpression() != null) {
            Symbol initializationSymbol = rfField.getRfExpression().accept(this);
            // TODO Lucian what is this
            if (initializationSymbol instanceof SelfSymbol)
                initializationSymbol = ((SelfSymbol) initializationSymbol).getScope();

            if (initializationSymbol == null)
                return null;

            if (!(checkInheritanceType((ClassTypeSymbol) fieldSymbolType, (ClassTypeSymbol) initializationSymbol)))
                SymbolTable.error(rfField.getContext(), rfField.getRfExpression().getContext().start, new StringBuilder().append("Type ").append(initializationSymbol.getName()).append(" of initialization expression of attribute ").append(fieldName.getText()).append(" is incompatible with declared type ").append(fieldSymbolType.getName()).toString());
        }

        return null;
    }

    @Override
    public Symbol visit(RfInt rfInt) {
        return TypeSymbolConstants.INT;
    }

    @Override
    public Symbol visit(RfArgument rfArgument) {
        // if the following condition is true it means the name of formal argument was illegal
        if (rfArgument.getIdSymbolName() == null)
            return null;

        Token argumentName = rfArgument.getName();
        if (argumentName == null)
            throw new IllegalStateException("Unable to find name token for field " + rfArgument);

        Token argumentType = rfArgument.getType();
        if  (argumentType == null)
            throw new IllegalStateException("Unable to find type token for field " + rfArgument);

//        Scope parent = currentScope.getParent();
//        if (parent == null)
//            throw new IllegalStateException("Unable to locate parent scope of scope " + currentScope);

//        Symbol symbolInherited = parent.lookup(argumentName.getText());
//        if (symbolInherited != null) {
//            if (!(currentScope instanceof TypeSymbol))
//                throw new IllegalStateException("Unable to log error for redefined field " + rfField + " in context " + currentScope);
//
//            SymbolTable.error(rfField.getContext(), argumentName, new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" redefines inherited attribute ").append(argumentName.getText()).toString());
//            return null;
//        }

        Symbol symbolType = SymbolTable.globals.lookup(argumentType.getText());
        if (symbolType == null) {
            if (!(currentScope instanceof MethodSymbol))
                throw new IllegalStateException("Unable to log error for undefined type " + argumentType.getText() + " of field " + rfArgument + " in context " + currentScope);

            Scope parentScope = currentScope.getParent();
            if (!(parentScope instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unable to log error for undefined type of parameter " + rfArgument + " in context " + currentScope + " due unknown parent scope " + parentScope);

            SymbolTable.error(rfArgument.getContext(), argumentType, new StringBuilder().append("Method ").append(((MethodSymbol) currentScope).getName()).append(" of class ").append(((ClassTypeSymbol) parentScope).getName()).append(" has formal parameter ").append(argumentName.getText()).append(" with undefined type ").append(argumentType.getText()).toString());
            return null;
        }

        rfArgument.setIdSymbolType(symbolType);

        return null;
    }

    public static boolean checkReturnType(RfMethod rfMethod, MethodSymbol methodSymbol, MethodSymbol overriddenMethodSymbol, ClassTypeSymbol scope) {
        Symbol returnTypeSymbol = methodSymbol.getReturnTypeSymbol();
        Symbol overriddenReturnTypeSymbol = overriddenMethodSymbol.getReturnTypeSymbol();

        if (!overriddenReturnTypeSymbol.getName().equals(returnTypeSymbol.getName())) {
            SymbolTable.error(rfMethod.getContext(), rfMethod.getReturnType(), new StringBuilder().append("Class ").append(scope.getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes return type from ").append(overriddenReturnTypeSymbol.getName()).append(" to ").append(returnTypeSymbol.getName()).toString());
            return true;
        }

        return false;
    }

    public static boolean checkParameters(RfMethod rfMethod, MethodSymbol methodSymbol, MethodSymbol overriddenMethodSymbol, ClassTypeSymbol scope) {
        Map<String, Symbol> parameters = methodSymbol.getParameters();
        Map<String, Symbol> overriddenParameters = overriddenMethodSymbol.getParameters();

        if (overriddenParameters.size() != parameters.size()) {
            SymbolTable.error(rfMethod.getContext(), rfMethod.getName(), new StringBuilder().append("Class ").append(scope.getName()).append(" overrides method ").append(methodSymbol.getName()).append(" with different number of formal parameters").toString());
            return true;
        }

        int numberOfParameters = overriddenParameters.size();

        List<Map.Entry<String, Symbol>> overriddenParametersList = new LinkedList<>(overriddenParameters.entrySet());
        List<Map.Entry<String, Symbol>> parametersList = new LinkedList<>(parameters.entrySet());

        for (int i = 0; i < numberOfParameters; i++) {
            Map.Entry<String, Symbol> overriddenParameter = overriddenParametersList.get(i);
            Map.Entry<String, Symbol> parameter = parametersList.get(i);

            Symbol overriddenParameterSymbolValue = overriddenParameter.getValue();
            Symbol parameterSymbolValue = parameter.getValue();

            if (!(overriddenParameterSymbolValue instanceof IdSymbol && parameterSymbolValue instanceof IdSymbol))
                throw new IllegalStateException("Unknown provided symbol " + overriddenParameterSymbolValue + " or " + parameterSymbolValue);

            ClassTypeSymbol overriddenParameterClassTypeSymbol = ((IdSymbol) overriddenParameterSymbolValue).getTypeSymbol();
            ClassTypeSymbol parameterClassTypeSymbol = ((IdSymbol) parameterSymbolValue).getTypeSymbol();

            String overriddenParameterTypeSymbolName = overriddenParameterClassTypeSymbol.getName();
            String parameterTypeSymbolName = parameterClassTypeSymbol.getName();
            if (!overriddenParameterTypeSymbolName.equals(parameterTypeSymbolName)) {

                List<RfArgument> parametersMethod = rfMethod.getArgs();

                SymbolTable.error(rfMethod.getContext(), parametersMethod.get(i).getType(), new StringBuilder("Class ").append(scope.getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes type of formal parameter ").append(parameter.getKey()).append(" from ").append(overriddenParameterTypeSymbolName).append(" to ").append(parameterTypeSymbolName).toString());
                return true;
            }
        }

        return false;
    }

    @Override
    public Symbol visit(RfMethod rfMethod) {
        // if the following condition is true it means the name of method was self
        MethodSymbol methodSymbol = rfMethod.getMethodSymbol();
        if (methodSymbol == null)
            return null;

        Token methodName = rfMethod.getName();
        if (methodName == null)
            throw new IllegalStateException("Unable to locate name of method " + rfMethod);

        Token returnType = rfMethod.getReturnType();
        if (returnType == null)
            throw new IllegalStateException("Unable to locate return type of method " + rfMethod);

        Symbol returnTypeSymbol = SymbolTable.globals.lookup(returnType.getText());
        if (returnTypeSymbol == null)
            throw new IllegalStateException("Unable to locate return type symbol in global context of method " + rfMethod);

        methodSymbol.setReturnTypeSymbol(returnTypeSymbol);

        Scope initialScope = currentScope;
        currentScope = methodSymbol;
        rfMethod.getArgs().forEach(rfArgument -> rfArgument.accept(this));

        Scope enclosingClassScope = initialScope.getParent();
        if (enclosingClassScope instanceof ClassTypeSymbol) {
            MethodSymbol overriddenMethodSymbol = ((ClassTypeSymbol) enclosingClassScope).lookUpMethod(methodSymbol.getName());
            if (overriddenMethodSymbol != null) {
                if (checkParameters(rfMethod, methodSymbol, overriddenMethodSymbol, (ClassTypeSymbol) initialScope))
                    return null;

                if (checkReturnType(rfMethod, methodSymbol, overriddenMethodSymbol, (ClassTypeSymbol) initialScope))
                    return null;
            }
        }

        RfExpression rfMethodBody = rfMethod.getRfMethodBody();
        if (rfMethodBody != null) {
            Symbol bodyMethodSymbol = rfMethodBody.accept(this);
            if (bodyMethodSymbol == null) {
                currentScope = initialScope;
                return null;
            }


            // TOOD Lucian you have to check here if the self type is well checked
            if (bodyMethodSymbol instanceof SelfSymbol) {
                currentScope = initialScope;
                return null;
            }

            if (!checkInheritanceType((ClassTypeSymbol) returnTypeSymbol, (ClassTypeSymbol) bodyMethodSymbol))
                SymbolTable.error(rfMethod.getContext(), rfMethodBody.getContext().start, new StringBuilder("Type ").append(bodyMethodSymbol.getName()).append(" of the body of method ").append(methodName.getText()).append(" is incompatible with declared return type ").append(returnTypeSymbol.getName()).toString());

        }

        // return from depth traversal
        currentScope = initialScope;

        return null;
    }

    @Override
    public Symbol visit(RfString rfString) {
        return TypeSymbolConstants.STRING;
    }

    @Override
    public Symbol visit(RfBool rfBool) {
        return TypeSymbolConstants.BOOL;
    }

    @Override
    public Symbol visit(RfId rfId) {
        Token idToken = rfId.getToken();
        if (idToken == null)
            throw new IllegalStateException("Unable to locate the name of id " + rfId);

        // TODO Lucian
        if (TypeSymbolConstants.SELF_STR.equals(idToken.getText()))
            return new SelfSymbol(TypeSymbolConstants.SELF_STR, currentScope);

        Symbol symbol = currentScope.lookup(idToken.getText());
        if (symbol == null) {
            SymbolTable.error(rfId.getContext(), rfId.getValue(), new StringBuilder().append("Undefined identifier ").append(idToken.getText()).toString());
            return null;
        }

        if (!(symbol instanceof IdSymbol))
            throw new IllegalStateException("Unknown symbol type located for id " + rfId);

        return ((IdSymbol) symbol).getTypeSymbol();
    }

    @Override
    public Symbol visit(RfArithmeticExpression rfArithmeticExpression) {
        Token arithmeticSymbol = rfArithmeticExpression.getArithmeticSymbol();
        if (arithmeticSymbol == null)
            throw new IllegalStateException("Unable to find arithmetic symbol");

        Symbol lhValueSymbol = rfArithmeticExpression.getLhValue().accept(this);
        Symbol rhValueSymbol = rfArithmeticExpression.getRhValue().accept(this);

        if (lhValueSymbol == null || rhValueSymbol == null)
            return null;

        String lhValueSymbolName = lhValueSymbol.getName();
        String rhValueSymbolName = rhValueSymbol.getName();

        if ((lhValueSymbol != TypeSymbolConstants.INT && rhValueSymbol == TypeSymbolConstants.INT) || (lhValueSymbol == TypeSymbolConstants.INT && rhValueSymbol != TypeSymbolConstants.INT)) {
            Symbol wrongSymbol = lhValueSymbol != TypeSymbolConstants.INT ? lhValueSymbol : rhValueSymbol;
            SymbolTable.error(rfArithmeticExpression.getContext(), wrongSymbol == lhValueSymbol ? rfArithmeticExpression.getContext().start : rfArithmeticExpression.getContext().stop, new StringBuilder().append("Operand of ").append(rfArithmeticExpression.getArithmeticSymbol().getText()).append(" has type ").append(wrongSymbol.getName()).append(" instead of Int").toString());
        }

        return TypeSymbolConstants.INT;
    }

    @Override
    public Symbol visit(RfParenExpression rfParenExpression) {
        RfExpression expression = rfParenExpression.getExpression();
        if (expression == null)
            throw new IllegalStateException("Expression from parenthesis could not be found");

        return expression.accept(this);
    }

    @Override
    public Symbol visit(RfRelationalExpression rfRelationalExpression) {
        Token relationalSymbol = rfRelationalExpression.getRelationalSymbol();
        if (relationalSymbol == null)
            throw new IllegalStateException("Unable to find arithmetic symbol");

        Symbol lhValueSymbol = rfRelationalExpression.getLhValue().accept(this);
        Symbol rhValueSymbol = rfRelationalExpression.getRhValue().accept(this);

        if ("=".equals(relationalSymbol.getText())) {
            if (!(lhValueSymbol instanceof ClassTypeSymbol && rhValueSymbol instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unknown types of relational expression");

            if ((isCustomType(lhValueSymbol) || isCustomType(rhValueSymbol)) && lhValueSymbol != rhValueSymbol)
                SymbolTable.error(rfRelationalExpression.getContext(), rfRelationalExpression.getToken(), new  StringBuilder().append("Cannot compare ").append(lhValueSymbol.getName()).append(" with ").append(rhValueSymbol.getName()).toString());
        } else if ((lhValueSymbol != TypeSymbolConstants.INT && rhValueSymbol == TypeSymbolConstants.INT) || (lhValueSymbol == TypeSymbolConstants.INT && rhValueSymbol != TypeSymbolConstants.INT)) {
            Symbol wrongSymbol = lhValueSymbol != TypeSymbolConstants.INT ? lhValueSymbol : rhValueSymbol;
            SymbolTable.error(rfRelationalExpression.getContext(), wrongSymbol == lhValueSymbol ? rfRelationalExpression.getContext().start : rfRelationalExpression.getContext().stop, new StringBuilder().append("Operand of ").append(rfRelationalExpression.getRelationalSymbol().getText()).append(" has type ").append(wrongSymbol.getName()).append(" instead of Int").toString());
        }

        return TypeSymbolConstants.BOOL;
    }

    private boolean isCustomType(Symbol symbol) {
        return symbol == TypeSymbolConstants.INT || symbol == TypeSymbolConstants.BOOL || symbol == TypeSymbolConstants.STRING;
    }

    @Override
    public Symbol visit(RfSingleValueExpression rfSingleValueExpression) {
        RfExpression expression = rfSingleValueExpression.getExpression();
        if (expression == null)
            throw new IllegalStateException("Unable to find operand of single operand expression " + rfSingleValueExpression);

        Symbol symbol = expression.accept(this);

        if (symbol != TypeSymbolConstants.INT && rfSingleValueExpression instanceof RfBitNegExpression) {
            SymbolTable.error(rfSingleValueExpression.getContext(), rfSingleValueExpression.getContext().stop, new StringBuilder().append("Operand of ").append(rfSingleValueExpression.getSymbol()).append(" has type ").append(symbol.getName()).append(" instead of Int").toString());
            return TypeSymbolConstants.INT;
        }

        if (symbol != TypeSymbolConstants.BOOL && rfSingleValueExpression instanceof RfNotExpression) {
            SymbolTable.error(rfSingleValueExpression.getContext(), rfSingleValueExpression.getContext().stop, new StringBuilder().append("Operand of ").append(rfSingleValueExpression.getSymbol()).append(" has type ").append(symbol.getName()).append(" instead of Bool").toString());
            return TypeSymbolConstants.BOOL;
        }

        if (rfSingleValueExpression instanceof RfIsVoidExpression)
            return TypeSymbolConstants.BOOL;

        return symbol;
    }

    @Override
    public Symbol visit(RfAssignment rfAssignment) {
        Token id = rfAssignment.getId();
        if (id == null)
            throw new IllegalStateException("Unable to locate assigner from " + rfAssignment);

        RfExpression expression = rfAssignment.getExpr();
        if (expression == null)
            throw new IllegalStateException("Unable to locate expression of assignment " + rfAssignment);

        // TODO Lucian
        if (TypeSymbolConstants.SELF_STR.equals(id.getText()))
            return TypeSymbolConstants.BOOL;

        Symbol expressionSymbol = expression.accept(this);
        if (expressionSymbol == null)
            return null;

        Symbol idSymbol = currentScope.lookup(id.getText());

        if (!(idSymbol instanceof IdSymbol))
            throw new IllegalStateException("Unable to locate idSymbol for element " + id);

        ClassTypeSymbol actualTypeSymbol = null;
        ClassTypeSymbol typeSymbol = ((IdSymbol) idSymbol).getTypeSymbol();
        if (TypeSymbolConstants.SELF_TYPE_STR.equals(((IdSymbol) idSymbol).getTypeSymbol().getName())) {
            actualTypeSymbol = typeSymbol;
            typeSymbol = ((IdSymbol) idSymbol).getCurrentTSelfTypeSymbol();
            if (TypeSymbolConstants.SELF_TYPE_STR.equals(expressionSymbol.getName()))
                expressionSymbol = (Symbol) currentScope.getParentWithClassType(ClassTypeSymbol.class);
        }

        if (!(expressionSymbol instanceof ClassTypeSymbol) && !(expressionSymbol instanceof SelfSymbol))
            throw new IllegalStateException("Unable to compute class returned type of expression " + expression);

        if (expressionSymbol instanceof SelfSymbol)
            expressionSymbol = ((SelfSymbol) expressionSymbol).getScope();

        boolean exprReturnedSelfType = false;
        if (expressionSymbol == TypeSymbolConstants.SELF_TYPE) {
            expressionSymbol = (Symbol) currentScope.getParentWithClassType(ClassTypeSymbol.class);
            exprReturnedSelfType = true;
        }

        if (checkInheritanceType(typeSymbol, (ClassTypeSymbol) expressionSymbol))
            return expressionSymbol;

        if (typeSymbol != expressionSymbol)
            SymbolTable.error(rfAssignment.getContext(), rfAssignment.getValue().start, new StringBuilder("Type ").append(exprReturnedSelfType ? TypeSymbolConstants.SELF_TYPE_STR : expressionSymbol.getName()).append(" of assigned expression is incompatible with declared type ").append(TypeSymbolConstants.SELF_TYPE_STR.equals(((IdSymbol) idSymbol).getRawTypeSymbolName()) ? TypeSymbolConstants.SELF_TYPE_STR : actualTypeSymbol != null ? actualTypeSymbol.getName() : typeSymbol.getName()).append(" of identifier ").append(idSymbol.getName()).toString());

        return expressionSymbol;
    }

    private static boolean checkInheritanceType(ClassTypeSymbol typeSymbol, ClassTypeSymbol expressionSymbol) {
        if (typeSymbol == expressionSymbol)
            return true;

        String parent = expressionSymbol.getParentScopeName();
        while (parent != null) {
            if (parent.equals(typeSymbol.getName()))
                return true;

            Symbol inherited = SymbolTable.globals.lookup(parent);
            if (!(inherited instanceof ClassTypeSymbol))
                return false;

            parent = ((ClassTypeSymbol) inherited).getParentScopeName();
        }

        return false;
    }

    @Override
    public Symbol visit(RfNewExpression rfNewExpression) {
        Token type = rfNewExpression.getType();
        if (type == null)
            throw new IllegalStateException("Unable to compute type of new expression " + rfNewExpression);

        Symbol rawType = SymbolTable.globals.lookup(type.getText());
        if (rawType == null) {
            SymbolTable.error(rfNewExpression.getContext(), rfNewExpression.getType(), new StringBuilder().append("new is used with undefined type ").append(type.getText()).toString());
            return null;
        }

        return rawType;
    }

    @Override
    public Symbol visit(RfDispatch rfDispatch) {
        Token dispatchToken = rfDispatch.getDispatch();
        if (dispatchToken == null)
            throw new IllegalStateException("Unable to locate function name on dispatchToken " + rfDispatch);

        Symbol symbolToCall = null;
        RfExpression objectToCall = rfDispatch.getObjectToCall();
        if (objectToCall != null) {
            if (TypeSymbolConstants.SELF_STR.equals(objectToCall.getToken().getText())) {
                symbolToCall = new SelfSymbol(TypeSymbolConstants.SELF_STR, currentScope.getParentWithClassType(ClassTypeSymbol.class));
            } else {
                symbolToCall = objectToCall.accept(this);
            }
        }

        Token atType = rfDispatch.getAtType();
        if (atType != null) {
            String atTypeText = atType.getText();
            if (TypeSymbolConstants.SELF_TYPE_STR.equals(atTypeText)) {
                SymbolTable.error(rfDispatch.getContext(), atType, new StringBuilder().append("Type of static dispatch cannot be SELF_TYPE").toString());
                return TypeSymbolConstants.OBJECT;
            }

            Symbol atTypeSymbol = SymbolTable.globals.lookup(atTypeText);
            if (atTypeSymbol == null) {
                SymbolTable.error(rfDispatch.getContext(), atType, new StringBuilder().append("Type ").append(atTypeText).append(" of static dispatch is undefined").toString());
                return TypeSymbolConstants.OBJECT;
            }

            if (symbolToCall == null)
                return null;

            if (!checkInheritanceType((ClassTypeSymbol) atTypeSymbol, (ClassTypeSymbol) symbolToCall)) {
                SymbolTable.error(rfDispatch.getContext(), atType, new StringBuilder().append("Type ").append(atType.getText()).append(" of static dispatch is not a superclass of type ").append(symbolToCall.getName()).toString());
                return TypeSymbolConstants.OBJECT;
            }

            MethodSymbol methodSymbol = ((ClassTypeSymbol) atTypeSymbol).lookUpMethod(dispatchToken.getText());
            if (methodSymbol != null) {
                if (!methodSymbol.isResolved())
                    methodSymbol.resolve();

                Symbol returnTypeSymbol = methodSymbol.getReturnTypeSymbol();
                if (!TypeSymbolConstants.SELF_TYPE_STR.equals(returnTypeSymbol.getName()))
                    symbolToCall = atTypeSymbol;
            } else {
                symbolToCall = atTypeSymbol;
            }

        }

        if (symbolToCall == null)
            return null;

        if (!(symbolToCall instanceof ClassTypeSymbol) && !(symbolToCall instanceof SelfSymbol))
            throw new IllegalStateException("Unknown type of symbol to call " + symbolToCall);

        MethodSymbol methodSymbol = null;
        if (symbolToCall.getName().equals(TypeSymbolConstants.SELF_TYPE_STR)) {

            RfExpression toCall = rfDispatch.getObjectToCall();
            while (toCall instanceof RfDispatch)
                toCall = ((RfDispatch) toCall).getObjectToCall();

            Symbol toCallClassSymbol = null;
            if (toCall instanceof RfImplicitDispatch) {
                toCallClassSymbol = (Symbol) currentScope.getParentWithClassType(ClassTypeSymbol.class);
            } else {
                toCallClassSymbol = toCall.accept(this);
            }

            symbolToCall = toCallClassSymbol;

            if (toCallClassSymbol instanceof SelfSymbol)
                toCallClassSymbol = ((SelfSymbol) toCallClassSymbol).getScope();

            methodSymbol = ((ClassTypeSymbol) toCallClassSymbol).lookUpMethod(dispatchToken.getText());
        } else if (symbolToCall instanceof SelfSymbol) {
            ClassTypeSymbol parentWithClassType = ((SelfSymbol) symbolToCall).getScope();
            if (parentWithClassType == null)
                throw new IllegalStateException("Unable to locate class type symbol of method " + dispatchToken.getText());

            methodSymbol = parentWithClassType.lookUpMethod(dispatchToken.getText());

        } else {

            methodSymbol = ((ClassTypeSymbol) symbolToCall).lookUpMethod(dispatchToken.getText());
        }

        if (methodSymbol == null) {
            SymbolTable.error(rfDispatch.getContext(), dispatchToken, new StringBuilder().append("Undefined method ").append(dispatchToken.getText()).append(" in class ").append(symbolToCall.getName()).toString());
            return TypeSymbolConstants.OBJECT;
        }

        if (!methodSymbol.isResolved())
            methodSymbol.resolve();

        List<Symbol> parametersSymbols = rfDispatch.getParameters().stream().map(rfExpression -> rfExpression.accept(this)).collect(Collectors.toList());

        Map<String, Symbol> parametersOfDefinition = methodSymbol.getParameters();

        if (parametersSymbols.size() != parametersOfDefinition.size()) {
            SymbolTable.error(rfDispatch.getContext(), dispatchToken, new StringBuilder().append("Method ").append(dispatchToken.getText()).append(" of class ").append(symbolToCall.getName()).append(" is applied to wrong number of arguments").toString());
            return TypeSymbolConstants.OBJECT;
        }

        List<Symbol> parametersOfDefinitionValues = new LinkedList<>(parametersOfDefinition.values());

        for (int i = 0; i < parametersSymbols.size(); ++i){
            Symbol definitionSymbol = parametersOfDefinitionValues.get(i);
            if (!(definitionSymbol instanceof IdSymbol))
                throw new IllegalStateException("Unknown evaluation of parameter " + definitionSymbol);

            if (!((IdSymbol) definitionSymbol).isResolved())
                ((IdSymbol) definitionSymbol).resolve();

            ClassTypeSymbol definitionParameterType = ((IdSymbol) definitionSymbol).getTypeSymbol();
            ClassTypeSymbol expressionParameterType = (ClassTypeSymbol) parametersSymbols.get(i);
            if (!(checkInheritanceType(definitionParameterType, expressionParameterType))) {
                String className = symbolToCall.getName();
                if (TypeSymbolConstants.SELF_STR.equals(className)) {
                    Scope parentWithClassType = currentScope.getParentWithClassType(ClassTypeSymbol.class);
                    if (!(parentWithClassType instanceof ClassTypeSymbol))
                        throw new IllegalStateException("Unable to compute enclosing class of method " + rfDispatch.getDispatch().getText());

                    className = ((ClassTypeSymbol) parentWithClassType).getName();
                }

                SymbolTable.error(rfDispatch.getContext(), rfDispatch.getParameters().get(i).getContext().start, new StringBuilder()
                        .append("In call to method ")
                        .append(dispatchToken.getText())
                        .append(" of class ")
                        .append(className)
                        .append(", actual type ")
                        .append(parametersSymbols.get(i).getName()).append(" of formal parameter ").append(definitionSymbol.getName())
                        .append(" is incompatible with declared type ").append(((IdSymbol) definitionSymbol).getTypeSymbol().getName())
                        .toString()
                );
            }
        }

//        return methodSymbol.getReturnTypeSymbol() == TypeSymbolConstants.SELF_TYPE ? symbolToCall : methodSymbol.getReturnTypeSymbol();
        return methodSymbol.getReturnTypeSymbol();
    }

    @Override
    public Symbol visit(RfImplicitDispatch rfImplicitDispatch) {
        Token dispatchToken = rfImplicitDispatch.getDispatch();
        if (dispatchToken == null)
            throw new IllegalStateException("Unable to locate name on dispatchToken " + rfImplicitDispatch);

        Scope parentScope = currentScope.getParentWithClassType(ClassTypeSymbol.class);
        if (!(parentScope instanceof Symbol))
            throw new IllegalStateException("Unknown evaluation of parent scope " + parentScope);

        Symbol symbolToCall = (Symbol) parentScope;
        if (!(symbolToCall instanceof ClassTypeSymbol))
            throw new IllegalStateException("Unknown evaluation of symbol " + symbolToCall + " instead of ClassTypeSymbol");

        String dispatchTokenName = dispatchToken.getText();
        MethodSymbol methodSymbol = ((ClassTypeSymbol) symbolToCall).lookUpMethod(dispatchTokenName);
        if (methodSymbol == null){
            SymbolTable.error(rfImplicitDispatch.getContext(), dispatchToken, new StringBuilder().append("Undefined method ").append(dispatchTokenName).append(" in class ").append(symbolToCall.getName()).toString());
            return TypeSymbolConstants.OBJECT;
        }

        if (!methodSymbol.isResolved())
            methodSymbol.resolve();

        List<Symbol> parametersSymbols = rfImplicitDispatch.getParameters().stream().map(rfExpression -> rfExpression.accept(this)).collect(Collectors.toList());

        Map<String, Symbol> parametersOfDefinition = methodSymbol.getParameters();

        if (parametersSymbols.size() != parametersOfDefinition.size()) {
            SymbolTable.error(rfImplicitDispatch.getContext(), dispatchToken, new StringBuilder().append("Method ").append(dispatchTokenName).append(" of class ").append(symbolToCall.getName()).append(" is applied to wrong number of arguments").toString());
            return TypeSymbolConstants.OBJECT;
        }

        List<Symbol> parametersOfDefinitionValues = new LinkedList<>(parametersOfDefinition.values());

        for (int i = 0; i < parametersSymbols.size(); ++i){
            Symbol definitionSymbol = parametersOfDefinitionValues.get(i);
            if (!(definitionSymbol instanceof IdSymbol))
                throw new IllegalStateException("Unknown evaluation of parameter " + definitionSymbol);

            if (!((IdSymbol) definitionSymbol).isResolved())
                ((IdSymbol) definitionSymbol).resolve();

            if (!(checkInheritanceType(((IdSymbol) definitionSymbol).getTypeSymbol(), (ClassTypeSymbol) parametersSymbols.get(i)))) {
                String className = symbolToCall.getName();
                if (TypeSymbolConstants.SELF_STR.equals(className)) {
                    Scope parentWithClassType = currentScope.getParentWithClassType(ClassTypeSymbol.class);
                    if (!(parentWithClassType instanceof ClassTypeSymbol))
                        throw new IllegalStateException("Unable to compute enclosing class of method " + rfImplicitDispatch.getDispatch().getText());

                    className = ((ClassTypeSymbol) parentWithClassType).getName();
                }


                SymbolTable.error(rfImplicitDispatch.getContext(), rfImplicitDispatch.getParameters().get(i).getContext().start, new StringBuilder()
                        .append("In call to method ")
                        .append(dispatchTokenName)
                        .append(" of class ")
                        .append(className)
                        .append(", actual type ")
                        .append(parametersSymbols.get(i).getName()).append(" of formal parameter ").append(definitionSymbol.getName())
                        .append(" is incompatible with declared type ").append(((IdSymbol) definitionSymbol).getTypeSymbol().getName())
                        .toString()
                );
            }

        }

        return methodSymbol.getReturnTypeSymbol();
    }

    @Override
    public Symbol visit(RfIf rfIf) {
        RfExpression cond = rfIf.getCond();
        if (cond == null)
            throw new IllegalStateException("Unable to locate condition of if " + rfIf.getContext());

        Symbol condSymbol = cond.accept(this);
        if (condSymbol != TypeSymbolConstants.BOOL)
            SymbolTable.error(rfIf.getContext(), rfIf.getCondContext().start, new StringBuilder().append("If condition has type ").append(condSymbol.getName()).append(" instead of ").append(TypeSymbolConstants.BOOL_STR).toString());

        RfExpression ifBranch = rfIf.getIfBranch();
        if (ifBranch == null)
            throw new IllegalStateException("Unable to locate then branch of if " + rfIf.getContext());

        Symbol thenBranchSymbol = ifBranch.accept(this);

        RfExpression elseBranch = rfIf.getElseBranch();
        if (elseBranch == null)
            throw new IllegalStateException("Unable to locate else branch of if " + rfIf.getElseBranch());

        Symbol elseBranchSymbol = elseBranch.accept(this);

        if (thenBranchSymbol instanceof SelfSymbol && elseBranchSymbol instanceof SelfSymbol) {
            Scope parentWithClassType = currentScope.getParentWithClassType(ClassTypeSymbol.class);
            if (!(parentWithClassType instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unable to compute enclosing class symbol");

            return SymbolTable.globals.lookup(((ClassTypeSymbol) parentWithClassType).getName());
        }

        if (thenBranchSymbol instanceof SelfSymbol  || elseBranchSymbol instanceof SelfSymbol) {
            List<Symbol> selfReplace = thenBranchSymbol instanceof SelfSymbol ? computePossibleSelfTypes((SelfSymbol) thenBranchSymbol) : computePossibleSelfTypes((SelfSymbol) elseBranchSymbol);
            Symbol otherSymbolBranch = thenBranchSymbol instanceof SelfSymbol ? elseBranchSymbol : thenBranchSymbol;

            if (otherSymbolBranch == TypeSymbolConstants.SELF_TYPE)
                return selfReplace.get(0);

            for (Symbol symbolSelfReplace : selfReplace) {
                if (symbolSelfReplace == otherSymbolBranch)
                    return symbolSelfReplace;
            }

            return TypeSymbolConstants.OBJECT;
        }

        return findCommonParent((ClassTypeSymbol) thenBranchSymbol, (ClassTypeSymbol) elseBranchSymbol);
    }

    private List<Symbol> computePossibleSelfTypes(SelfSymbol symbol) {
        List<Symbol> possibleTypes = new LinkedList<>();

        ClassTypeSymbol classTypeSymbol = symbol.getScope();
        if (classTypeSymbol == null)
            throw new IllegalStateException("Unable to compute class scope of symbol self type");

        possibleTypes.add(classTypeSymbol);

        while (classTypeSymbol != null) {
            classTypeSymbol = (ClassTypeSymbol) classTypeSymbol.getParent();
            if (classTypeSymbol != null)
                possibleTypes.add(classTypeSymbol);
        }

        return possibleTypes;
    }

    private static Symbol findCommonParent(ClassTypeSymbol thenBranchSymbol, ClassTypeSymbol elseBranchSymbol) {
        Set<ClassTypeSymbol> types = new HashSet<>();
        while (thenBranchSymbol != null) {
            types.add(thenBranchSymbol);
            thenBranchSymbol = (ClassTypeSymbol) thenBranchSymbol.getParent();
        }

        while (elseBranchSymbol != null) {
            if (types.contains(elseBranchSymbol))
                return elseBranchSymbol;

            elseBranchSymbol = (ClassTypeSymbol) elseBranchSymbol.getParent();
        }

        return TypeSymbolConstants.OBJECT;
    }

    @Override
    public Symbol visit(RfWhile rfWhile) {

        RfExpression cond = rfWhile.getCond();
        if (cond == null)
            throw new IllegalStateException("Unable to locate the condition of while statement");

        Symbol condSymbol = cond.accept(this);
        if (condSymbol != TypeSymbolConstants.BOOL) {
            if (rfWhile.getCondContext() == null)
                throw new IllegalStateException("Unable to locate condition context of while " + rfWhile.getContext());
            SymbolTable.error(rfWhile.getContext(), rfWhile.getCondContext().start, new StringBuilder().append("While condition has type ").append(condSymbol.getName()).append(" instead of ").append(TypeSymbolConstants.BOOL_STR).toString());

            return TypeSymbolConstants.OBJECT;
        }

        return condSymbol;
    }

    @Override
    public Symbol visit(RfLet rfLet) {
        LetSymbol letScope = rfLet.getLetScope();
        if (letScope == null)
            throw new IllegalStateException("Unable to find let scope " + rfLet);

        Scope initialScope = currentScope;

        currentScope = letScope;

        rfLet.getVars().forEach(rfDeclareVariable -> rfDeclareVariable.accept(this));
        Symbol bodySymbol = rfLet.getBody().accept(this);

        currentScope = initialScope;
        return bodySymbol;
    }

    @Override
    public Symbol visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        IdSymbol idSymbol = rfDeclareVariable.getIdSymbol();
        if (idSymbol == null)
            return null;

        Token name = rfDeclareVariable.getName();
        if (name == null)
            throw new IllegalStateException("Unable to find name of declared variable " + rfDeclareVariable);

        Token type = rfDeclareVariable.getType();
        if (type == null)
            throw new IllegalStateException("Unable to find type of declared variable " + rfDeclareVariable);

        Symbol typeSymbol = SymbolTable.globals.lookup(type.getText());
        if (typeSymbol == null) {
            SymbolTable.error(rfDeclareVariable.getContext(), rfDeclareVariable.getType(), new StringBuilder().append("Let variable ").append(idSymbol.getName()).append(" has undefined type ").append(rfDeclareVariable.getType().getText()).toString());
            return null;
        }

        if (!(typeSymbol instanceof ClassTypeSymbol))
            throw new IllegalStateException("Unable to find symbol for type let declared variable " + rfDeclareVariable);

        idSymbol.setTypeSymbol((ClassTypeSymbol) typeSymbol);


        RfExpression value = rfDeclareVariable.getValue();
        if (value != null) {
            Symbol initializationExpressionSymbol = value.accept(this);
            if (initializationExpressionSymbol == null)
                return null;

            currentScope.add(idSymbol);

            if (!checkInheritanceType((ClassTypeSymbol) typeSymbol, (ClassTypeSymbol) initializationExpressionSymbol))
                SymbolTable.error(rfDeclareVariable.getContext(), rfDeclareVariable.getValue().getContext().start, new StringBuilder().append("Type ").append(initializationExpressionSymbol.getName()).append(" of initialization expression of identifier ").append(idSymbol.getName()).append(" is incompatible with declared type ").append(idSymbol.getTypeSymbol().getName()).toString());
        }

        currentScope.add(idSymbol);

        return null;
    }

    @Override
    public Symbol visit(RfCase rfCase) {
        RfExpression toEvaluate = rfCase.getToEvaluate();
        if (toEvaluate == null)
            throw new IllegalStateException("Unable to locate evaluation of branch " + rfCase.getContext());

        List<RfCase.RfCaseBranch> branches = rfCase.getBranches();
        if (branches == null)
            throw new IllegalStateException("Unable to locate branched of case " + rfCase.getContext());

        List<Symbol> branchesSymbols = branches.stream().map(rfCaseBranch -> rfCaseBranch.accept(this)).filter(Objects::nonNull).collect(Collectors.toList());
        if (branchesSymbols.isEmpty())
            return null;

        Symbol firstBranchSymbol = branchesSymbols.get(0);
        if (branchesSymbols.size() == 1)
            return firstBranchSymbol;

        Symbol secondBranchSymbol = branchesSymbols.get(1);

        Symbol commonParent = findCommonParent((ClassTypeSymbol) firstBranchSymbol, (ClassTypeSymbol) secondBranchSymbol);
        if (commonParent == TypeSymbolConstants.OBJECT)
            return TypeSymbolConstants.OBJECT;

        for (int i = 2; i < branchesSymbols.size(); i++) {
            Symbol branchSymbol = branchesSymbols.get(i);
            commonParent = findCommonParent((ClassTypeSymbol) commonParent, (ClassTypeSymbol) branchSymbol);
            if (commonParent == TypeSymbolConstants.OBJECT)
                return TypeSymbolConstants.OBJECT;
        }

        return commonParent;
    }

    @Override
    public Symbol visit(RfCase.RfCaseBranch rfCaseBranch) {
        Token id = rfCaseBranch.getId();
        if (id == null)
            throw new IllegalStateException("Unable to find variable name for branch " + rfCaseBranch);

        Token type = rfCaseBranch.getType();
        if (type == null)
            throw new IllegalStateException("Unable to find variable type for branch " + rfCaseBranch);

        Symbol symbolType = SymbolTable.globals.lookup(type.getText());
        if (symbolType == null) {
            SymbolTable.error(rfCaseBranch.getContext(), rfCaseBranch.getType(), new StringBuilder().append("Case variable ").append(id.getText()).append(" has undefined type ").append(type.getText()).toString());
            return null;
        }

        if (!(symbolType instanceof ClassTypeSymbol))
            throw new IllegalStateException("Unknown evaluation of variable type " + symbolType);

        IdSymbol variableDeclared = new IdSymbol(id.getText(), type.getText());
        variableDeclared.setTypeSymbol((ClassTypeSymbol) symbolType);

        CaseScope caseScope = new CaseScope(variableDeclared, currentScope);

        Scope initialScope = currentScope;
        currentScope = caseScope;

        RfExpression expression = rfCaseBranch.getExpression();
        if (expression == null)
            throw new IllegalStateException("Unable to find expression case branch for case " + rfCaseBranch.getContext());

        Symbol branchSymbol = expression.accept(this);
        currentScope = initialScope;

        return branchSymbol;
    }

    @Override
    public Symbol visit(RfBody rfBody) {
        List<RfExpression> expressions = rfBody.getExpressions();
        if (expressions == null)
            throw new IllegalStateException("Unable to evaluate expressions of body " + rfBody.getContext());

        List<Symbol> expressionsSymbols = expressions.stream().map(rfExpression -> rfExpression.accept(this)).filter(Objects::nonNull).collect(Collectors.toList());
        if (expressionsSymbols.isEmpty())
            return null;

        return expressionsSymbols.get(expressionsSymbols.size() - 1);
    }

}
