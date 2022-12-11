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
import cool.structures.custom.symbols.TypeSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.*;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {

    Scope currentScope;

    @Override
    public TypeSymbol visit(RfProgram rfProgram) {
        rfProgram.getRfClasses().forEach(rfClass -> rfClass.accept(this));
        return null;
    }

    @Override
    public TypeSymbol visit(RfClass rfClass) {
        if (rfClass.getTypeSymbol() == null)
            return null;

        currentScope = rfClass.getTypeSymbol();
        rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));

        return null;
    }

    @Override
    public TypeSymbol visit(RfField rfField) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfInt rfInt) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfArgument rfArgument) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfMethod rfMethod) {
        MethodSymbol methodSymbol = rfMethod.getMethodSymbol();
        if (methodSymbol == null)
            return null;

        Token methodName = rfMethod.getName();
        if (methodName == null)
            throw new IllegalStateException("Unable to locate name of method " + rfMethod);

        if (!(currentScope instanceof TypeSymbol))
            throw new IllegalStateException("Unknown evaluation of current scope " + currentScope);

        Scope enclosingClassScope = currentScope.getParent();
        String name = methodName.getText();
        Symbol overriddenMethodSymbol = enclosingClassScope.lookup(name);
        if (overriddenMethodSymbol instanceof MethodSymbol) {
            if (checkParameters(rfMethod, methodSymbol, (MethodSymbol) overriddenMethodSymbol))
                return null;

            if (checkReturnType(rfMethod, methodSymbol, (MethodSymbol) overriddenMethodSymbol))
                return null;
        }

        Scope initialScope = currentScope;
        currentScope = methodSymbol;

        rfMethod.getRfMethodBody().accept(this);

        currentScope = initialScope;

        return null;
    }

    private boolean checkReturnType(RfMethod rfMethod, MethodSymbol methodSymbol, MethodSymbol overriddenMethodSymbol) {
        Symbol returnTypeSymbol = methodSymbol.getReturnTypeSymbol();
        Symbol overriddenReturnTypeSymbol = overriddenMethodSymbol.getReturnTypeSymbol();

        if (!overriddenReturnTypeSymbol.getName().equals(returnTypeSymbol.getName())) {
            SymbolTable.error(rfMethod.getContext(), rfMethod.getReturnType(), new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes return type from ").append(overriddenReturnTypeSymbol.getName()).append(" to ").append(returnTypeSymbol.getName()).toString());
            return true;
        }

        return false;
    }

    private boolean checkParameters(RfMethod rfMethod, MethodSymbol methodSymbol, MethodSymbol overriddenMethodSymbol) {
        Map<String, Symbol> parameters = methodSymbol.getParameters();
        Map<String, Symbol> overriddenParameters = overriddenMethodSymbol.getParameters();

        if (overriddenParameters.size() != parameters.size()) {
            SymbolTable.error(rfMethod.getContext(), rfMethod.getName(), new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" overrides method ").append(methodSymbol.getName()).append(" with different number of formal parameters").toString());
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

            TypeSymbol overriddenParameterTypeSymbol = ((IdSymbol) overriddenParameterSymbolValue).getTypeSymbol();
            TypeSymbol parameterTypeSymbol = ((IdSymbol) parameterSymbolValue).getTypeSymbol();

            String overriddenParameterTypeSymbolName = overriddenParameterTypeSymbol.getName();
            String parameterTypeSymbolName = parameterTypeSymbol.getName();
            if (!overriddenParameterTypeSymbolName.equals(parameterTypeSymbolName)) {

                List<RfArgument> parametersMethod = rfMethod.getArgs();

                SymbolTable.error(rfMethod.getContext(), parametersMethod.get(i).getType(), new StringBuilder("Class ").append(((TypeSymbol) currentScope).getName()).append(" overrides method ").append(rfMethod.getName().getText()).append(" but changes type of formal parameter ").append(parameter.getKey()).append(" from ").append(overriddenParameterTypeSymbolName).append(" to ").append(parameterTypeSymbolName).toString());
                return true;
            }
        }

        return false;
    }

    @Override
    public TypeSymbol visit(RfString rfString) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfBool rfBool) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfId rfId) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfArithmeticExpression rfArithmeticExpression) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfParenExpression rfParenExpression) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfRelationalExpression rfRelationalExpression) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfSingleValueExpression rfSingleValueExpression) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfAssignment rfAssignment) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfNewExpression rfNewExpression) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfDispatch rfDispatch) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfImplicitDispatch rfImplicitDispatch) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfIf rfIf) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfWhile rfWhile) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfLet rfLet) {
        LetSymbol letScope = rfLet.getLetScope();
        if (letScope == null)
            throw new IllegalStateException("Unable to find let scope " + rfLet);

        Scope initialScope = currentScope;

        currentScope = letScope;

        rfLet.getVars().forEach(rfDeclareVariable -> rfDeclareVariable.accept(this));
        TypeSymbol value = rfLet.getBody().accept(this);

        currentScope = initialScope;
        return value;
    }

    @Override
    public TypeSymbol visit(RfLet.RfDeclareVariable rfDeclareVariable) {
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

        if (!(typeSymbol instanceof TypeSymbol))
            throw new IllegalStateException("Unable to find symbol for type let declared variable " + rfDeclareVariable);

        idSymbol.setTypeSymbol((TypeSymbol) typeSymbol);

        return null;
    }

    @Override
    public TypeSymbol visit(RfCase rfCase) {
        rfCase.getBranches().forEach(rfCaseBranch -> rfCaseBranch.accept(this));
        return null;
    }

    @Override
    public TypeSymbol visit(RfCase.RfCaseBranch rfCaseBranch) {
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
    public TypeSymbol visit(RfBody rfBody) {
        return null;
    }

}
