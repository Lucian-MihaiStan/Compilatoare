package cool.visitor.definition;

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
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.LetSymbol;
import cool.structures.custom.symbols.TypeSymbol;
import cool.structures.custom.symbols.MethodSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class DefinitionPassVisitor implements ASTVisitor<Void> {

    Scope currentScope = null;

    @Override
    public Void visit(RfProgram rfProgram) {
        rfProgram.getRfClasses().forEach(rfClass -> rfClass.accept(this));
        return null;
    }

    @Override
    public Void visit(RfClass rfClass) {
        String actualTypeName = rfClass.getActualTokenType().getText();

        if (TypeSymbolConstants.SELF_TYPE_STR.equals(actualTypeName)) {
            SymbolTable.error(rfClass.getContext(), rfClass.getToken(), "Class has illegal name SELF_TYPE");
            return null;
        }

        String parentSymbolName = rfClass.getInheritedTokenType() != null ? rfClass.getInheritedTokenType().getText() : "Object";

        TypeSymbol typeClassSymbol = new TypeSymbol(actualTypeName, parentSymbolName);
        if (!SymbolTable.globals.add(typeClassSymbol)) {
            SymbolTable.error(rfClass.getContext(), rfClass.getToken(), new StringBuilder().append("Class ").append(actualTypeName).append(" is redefined").toString());
            return null;
        }

        if (TypeSymbolConstants.illegalParents.contains(parentSymbolName)) {
            SymbolTable.error(rfClass.getContext(), rfClass.getInheritedTokenType(), new StringBuilder().append("Class ").append(actualTypeName).append(" has illegal parent ").append(parentSymbolName).toString());
            return null;
        }

        currentScope = typeClassSymbol;

        rfClass.setTypeSymbol(typeClassSymbol);

        rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));

        return null;
    }

    @Override
    public Void visit(RfField rfField) {
        Token fieldName = rfField.getFieldName();
        if (fieldName == null)
            throw new IllegalStateException("Unable to find field name for " + rfField);

        Token fieldType = rfField.getFieldType();
        if (fieldType == null)
            throw new IllegalStateException("Unable to find field type for " + rfField);

        String name = fieldName.getText();
        if (TypeSymbolConstants.SELF_STR.equals(name)) {
            if (!(currentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to throw error for field " + rfField + " with illegal name self in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldName, new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" has attribute with illegal name self").toString());
            return null;
        }

        IdSymbol idSymbolName = new IdSymbol(name);
        if (!currentScope.add(idSymbolName)) {
            if (!(currentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to throw error for symbol with name " + name + " in the context of " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldName, new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" redefines attribute ").append(name).toString());
            return null;
        }

        rfField.setIdSymbol(idSymbolName);

        if (rfField.getRfExpression() != null)
            rfField.getRfExpression().accept(this);

        return null;
    }

    @Override
    public Void visit(RfInt rfInt) {
        return null;
    }

    @Override
    public Void visit(RfArgument rfArgument) {
        Token argumentName = rfArgument.getName();
        if (argumentName == null)
            throw new IllegalStateException("Unable to find name of argument " + rfArgument + " in the context of " + currentScope);

        Token argumentType = rfArgument.getType();
        if (argumentType == null)
            throw new IllegalStateException("Unable to find type of argument" + rfArgument + " in the context of " + currentScope);

        String name = argumentName.getText();
        if (TypeSymbolConstants.SELF_STR.equals(name)) {
            if (!(currentScope instanceof MethodSymbol))
                throw new IllegalStateException("Unable to log error for unaccepted name of parameter " + rfArgument + " in context " + currentScope);

            Scope parentScope = currentScope.getParent();
            if (!(parentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log error for unaccepted name of parameter " + rfArgument + " in context " + currentScope + " due unknown parent scope " + parentScope);

            SymbolTable.error(rfArgument.getContext(), rfArgument.getName(), new StringBuilder().append("Method ").append(((MethodSymbol) currentScope).getName()).append(" of class ").append(((TypeSymbol) parentScope).getName()).append(" has formal parameter with illegal name self").toString());
            return null;
        }

        String type = argumentType.getText();
        if (TypeSymbolConstants.SELF_TYPE_STR.equals(type)) {
            if (!(currentScope instanceof MethodSymbol))
                throw new IllegalStateException("Unable to log error for unaccepted SELF_TYPE of parameter " + rfArgument + " in context " + currentScope);

            Scope parentScope = currentScope.getParent();
            if (!(parentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log error for unaccepted SELF_TYPE of parameter " + rfArgument + " in context " + currentScope + " due unknown parent scope " + parentScope);

            SymbolTable.error(rfArgument.getContext(), rfArgument.getType(), new StringBuilder().append("Method ").append(((MethodSymbol) currentScope).getName()).append(" of class ").append(((TypeSymbol) parentScope).getName()).append(" has formal parameter ").append(name).append(" with illegal type SELF_TYPE").toString());
            return null;
        }

        IdSymbol argSymbol = new IdSymbol(name);
        if (!(currentScope.add(argSymbol))) {
            if (!(currentScope instanceof MethodSymbol))
                throw new IllegalStateException("Unable to log error for redefined parameter " + rfArgument + " in context " + currentScope);

            Scope parentScope = currentScope.getParent();
            if (!(parentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log error for redefined parameter " + rfArgument + " in context " + currentScope + " due unknown parent scope " + parentScope);

            SymbolTable.error(rfArgument.getContext(), rfArgument.getName(), new StringBuilder().append("Method ").append(((MethodSymbol) currentScope).getName()).append(" of class ").append(((TypeSymbol) parentScope).getName()).append(" redefines formal parameter ").append(name).toString());
            return null;
        }

        rfArgument.setIdSymbol(argSymbol);

        return null;
    }

    @Override
    public Void visit(RfMethod rfMethod) {
        Token methodName = rfMethod.getName();
        if (methodName == null)
            throw new IllegalStateException("Unable to find method name in context " + currentScope);

        String name = methodName.getText();
        MethodSymbol methodSymbol = new MethodSymbol(name, currentScope);

        if (!(currentScope.add(methodSymbol))) {
            if (!(currentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log redefined error for method " + name + " in context " + currentScope);

            SymbolTable.error(rfMethod.getContext(), rfMethod.getName(), new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" redefines method ").append(name).toString());
            return null;
        }

        rfMethod.setMethodSymbol(methodSymbol);

        Scope initialScope = currentScope;
        currentScope = methodSymbol;
        rfMethod.getArgs().forEach(rfArgument -> rfArgument.accept(this));
        rfMethod.getRfMethodBody().accept(this);

        // return from the depth traversal
        currentScope = initialScope;

        return null;
    }

    @Override
    public Void visit(RfString rfString) {
        return null;
    }

    @Override
    public Void visit(RfBool rfBool) {
        return null;
    }

    @Override
    public Void visit(RfId rfId) {
        return null;
    }

    @Override
    public Void visit(RfArithmeticExpression rfArithmeticExpression) {
        return null;
    }

    @Override
    public Void visit(RfParenExpression rfParenExpression) {
        return null;
    }

    @Override
    public Void visit(RfRelationalExpression rfRelationalExpression) {
        return null;
    }

    @Override
    public Void visit(RfSingleValueExpression rfSingleValueExpression) {
        return null;
    }

    @Override
    public Void visit(RfAssignment rfAssignment) {
        return null;
    }

    @Override
    public Void visit(RfNewExpression rfNewExpression) {
        return null;
    }

    @Override
    public Void visit(RfDispatch rfDispatch) {
        return null;
    }

    @Override
    public Void visit(RfImplicitDispatch rfImplicitDispatch) {
        return null;
    }

    @Override
    public Void visit(RfIf rfIf) {
        return null;
    }

    @Override
    public Void visit(RfWhile rfWhile) {
        return null;
    }

    @Override
    public Void visit(RfLet rfLet) {
        LetSymbol letScope = new LetSymbol("LetScope", currentScope);
        rfLet.setLetSymbolScope(letScope);

        Scope initialScope = currentScope;
        currentScope = letScope;
        rfLet.getVars().forEach(rfDeclareVariable -> rfDeclareVariable.accept(this));
        rfLet.getBody().accept(this);

        currentScope = initialScope;

        return null;
    }

    @Override
    public Void visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        Token rfDeclareVariableName = rfDeclareVariable.getName();
        if (rfDeclareVariableName == null)
            throw new IllegalStateException("Unable to compute name of declared variable " + rfDeclareVariable);

        Token rfDeclareVariableType = rfDeclareVariable.getType();
        if (rfDeclareVariableType == null)
            throw new IllegalStateException("Unable to find type of declared variable " + rfDeclareVariable);

        if (TypeSymbolConstants.SELF_STR.equals(rfDeclareVariableName.getText())) {
            SymbolTable.error(rfDeclareVariable.getContext(), rfDeclareVariableName, "Let variable has illegal name self");
            return null;
        }

        IdSymbol idSymbol = new IdSymbol(rfDeclareVariableName.getText());
        rfDeclareVariable.setIdSymbol(idSymbol);

        RfExpression initializedExpression = rfDeclareVariable.getValue();
        if (initializedExpression != null)
            initializedExpression.accept(this);

        return null;
    }

    @Override
    public Void visit(RfCase rfCase) {
        rfCase.getBranches().forEach(rfCaseBranch -> rfCaseBranch.accept(this));
        return null;
    }

    @Override
    public Void visit(RfCase.RfCaseBranch rfCaseBranch) {
        Token id = rfCaseBranch.getId();
        if (id == null)
            throw new IllegalStateException("Unable to find the variable name of the branch " + rfCaseBranch);
        
        if (TypeSymbolConstants.SELF_STR.equals(id.getText())) {
            SymbolTable.error(rfCaseBranch.getContext(), id, "Case variable has illegal name self");
            return null;
        }

        Token type = rfCaseBranch.getType();
        if (TypeSymbolConstants.SELF_TYPE_STR.equals(type.getText())) {
            SymbolTable.error(rfCaseBranch.getContext(), type, new StringBuilder("Case variable ").append(id.getText()).append(" has illegal type SELF_TYPE").toString());
            return null;
        }

        rfCaseBranch.getExpression().accept(this);
        
        return null;
    }

    @Override
    public Void visit(RfBody rfBody) {
        return null;
    }
}
