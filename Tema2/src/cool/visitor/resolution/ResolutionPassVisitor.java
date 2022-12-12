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
import cool.reflection.expression.single.value.RfNotExpression;
import cool.reflection.expression.single.value.RfParenExpression;
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.LetSymbol;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        ClassTypeSymbol currentScope = rfClass.getTypeSymbol();

        Symbol parentSymbol = null;

        while (currentScope != TypeSymbolConstants.OBJECT) {
            parentSymbol = SymbolTable.globals.lookup(currentScope.getParentScopeName());
            if (parentSymbol == null) {
                SymbolTable.error(rfClass.getContext(), rfClass.getInheritedTokenType(), new StringBuilder().append("Class ").append(rfClass.getToken().getText()).append(" has undefined parent ").append(currentScope.getParentScopeName()).toString());
                return;
            }

            if (!(parentSymbol instanceof ClassTypeSymbol))
                throw new IllegalStateException("Undefined parent symbol");
            currentScope.setParentScope((ClassTypeSymbol) parentSymbol);

            currentScope = (ClassTypeSymbol) parentSymbol;

            if (currentScope == rfClass.getTypeSymbol()) {
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

        Symbol symbolType = SymbolTable.globals.lookup(fieldType.getText());
        if (symbolType == null) {
            if (!(currentScope instanceof ClassTypeSymbol))
                throw new IllegalStateException("Unable to log error for undefined type " + fieldType.getText() + " of field " + rfField + " in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldType, new StringBuilder().append("Class ").append(((ClassTypeSymbol) currentScope).getName()).append(" has attribute ").append(fieldName.getText()).append(" with undefined type ").append(fieldType.getText()).toString());
            return null;
        }

        rfField.setIdSymbolType(symbolType);

        if (rfField.getRfExpression() != null)
            rfField.getRfExpression().accept(this);

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
            SymbolTable.error(rfMethod.getContext(), rfMethod.getReturnType(), new StringBuilder().append("Class ").append(((ClassTypeSymbol) scope).getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes return type from ").append(overriddenReturnTypeSymbol.getName()).append(" to ").append(returnTypeSymbol.getName()).toString());
            return true;
        }

        return false;
    }

    public static boolean checkParameters(RfMethod rfMethod, MethodSymbol methodSymbol, MethodSymbol overriddenMethodSymbol, ClassTypeSymbol scope) {
        Map<String, Symbol> parameters = methodSymbol.getParameters();
        Map<String, Symbol> overriddenParameters = overriddenMethodSymbol.getParameters();

        if (overriddenParameters.size() != parameters.size()) {
            SymbolTable.error(rfMethod.getContext(), rfMethod.getName(), new StringBuilder().append("Class ").append(((ClassTypeSymbol) scope).getName()).append(" overrides method ").append(methodSymbol.getName()).append(" with different number of formal parameters").toString());
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

                SymbolTable.error(rfMethod.getContext(), parametersMethod.get(i).getType(), new StringBuilder("Class ").append(((ClassTypeSymbol) scope).getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes type of formal parameter ").append(parameter.getKey()).append(" from ").append(overriddenParameterTypeSymbolName).append(" to ").append(parameterTypeSymbolName).toString());
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
        rfMethod.getRfMethodBody().accept(this);

        Scope enclosingClassScope = initialScope.getParent();
        if (enclosingClassScope instanceof ClassTypeSymbol) {
            Symbol overriddenMethodSymbol = ((ClassTypeSymbol) enclosingClassScope).lookUpMethod(methodSymbol.getName());
            if (overriddenMethodSymbol instanceof MethodSymbol) {
                if (checkParameters(rfMethod, methodSymbol, (MethodSymbol) overriddenMethodSymbol, (ClassTypeSymbol) initialScope))
                    return null;

                if (checkReturnType(rfMethod, methodSymbol, (MethodSymbol) overriddenMethodSymbol, (ClassTypeSymbol) initialScope))
                    return null;
            }
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
            return null;

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

        return symbol;
    }

    @Override
    public Symbol visit(RfAssignment rfAssignment) {
        return null;
    }

    @Override
    public Symbol visit(RfNewExpression rfNewExpression) {
        return null;
    }

    @Override
    public Symbol visit(RfDispatch rfDispatch) {
        return null;
    }

    @Override
    public Symbol visit(RfImplicitDispatch rfImplicitDispatch) {
        return null;
    }

    @Override
    public Symbol visit(RfIf rfIf) {
        return null;
    }

    @Override
    public Symbol visit(RfWhile rfWhile) {
        return null;
    }

    @Override
    public Symbol visit(RfLet rfLet) {
        LetSymbol letScope = rfLet.getLetScope();
        if (letScope == null)
            throw new IllegalStateException("Unable to find let scope " + rfLet);

        Scope initialScope = currentScope;

        currentScope = letScope;

        rfLet.getVars().forEach(rfDeclareVariable -> rfDeclareVariable.accept(this));
        rfLet.getBody().accept(this);

        currentScope = initialScope;
        return null;
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

        rfDeclareVariable.getValue().accept(this);

        return null;
    }

    @Override
    public Symbol visit(RfCase rfCase) {
        rfCase.getBranches().forEach(rfCaseBranch -> rfCaseBranch.accept(this));
        return null;
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
        return null;
    }

    @Override
    public Symbol visit(RfBody rfBody) {
        return null;
    }

}
