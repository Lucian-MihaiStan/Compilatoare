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
import cool.structures.Symbol;
import cool.structures.SymbolTable;
import cool.structures.custom.symbols.TypeSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.lang.reflect.Type;

public class ClassHierarchyPassVisitor implements ASTVisitor<Void> {

    Scope currentScope;

    @Override
    public Void visit(RfProgram rfProgram) {
        rfProgram.getRfClasses().forEach(rfClass -> rfClass.accept(this));
        return null;
    }

    @Override
    public Void visit(RfClass rfClass) {
        TypeSymbol classScope = rfClass.getTypeSymbol();
        if (classScope == null)
            return null;

        checkClassHierarchy(rfClass);

        currentScope = classScope;
        rfClass.getRfFeatures().forEach(rfFeature -> rfFeature.accept(this));

        return null;
    }

    private void checkClassHierarchy(RfClass rfClass) {
        TypeSymbol currentScope = rfClass.getTypeSymbol();

        Symbol parentSymbol = null;

        while (currentScope != TypeSymbolConstants.OBJECT) {
            parentSymbol = SymbolTable.globals.lookup(currentScope.getParentScopeName());
            if (parentSymbol == null) {
                SymbolTable.error(rfClass.getContext(), rfClass.getInheritedTokenType(), new StringBuilder().append("Class ").append(rfClass.getToken().getText()).append(" has undefined parent ").append(currentScope.getParentScopeName()).toString());
                return;
            }

            if (!(parentSymbol instanceof TypeSymbol))
                throw new IllegalStateException("Undefined parent symbol");
            currentScope.setParentScope((TypeSymbol) parentSymbol);

            currentScope = (TypeSymbol) parentSymbol;

            if (currentScope == rfClass.getTypeSymbol()) {
                SymbolTable.error(rfClass.getContext(), rfClass.getToken(), new StringBuilder().append("Inheritance cycle for class ").append(rfClass.getActualTokenType().getText()).toString());
                return;
            }
        }
    }

    @Override
    public Void visit(RfField rfField) {
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
            if (!(currentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log error for redefined field " + rfField + " in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldName, new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" redefines inherited attribute ").append(fieldName.getText()).toString());
            return null;
        }

        Symbol symbolType = SymbolTable.globals.lookup(fieldType.getText());
        if (symbolType == null) {
            if (!(currentScope instanceof TypeSymbol))
                throw new IllegalStateException("Unable to log error for undefined type " + fieldType.getText() + " of field " + rfField + " in context " + currentScope);

            SymbolTable.error(rfField.getContext(), fieldType, new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" has attribute ").append(fieldName.getText()).append(" with undefined type ").append(fieldType.getText()).toString());
            return null;
        }

        rfField.setIdSymbolType(symbolType);

        return null;
    }

    @Override
    public Void visit(RfInt rfInt) {
        return null;
    }

    @Override
    public Void visit(RfArgument rfArgument) {
        return null;
    }

    @Override
    public Void visit(RfMethod rfMethod) {
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
        return null;
    }

    @Override
    public Void visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        return null;
    }

    @Override
    public Void visit(RfCase rfCase) {
        return null;
    }

    @Override
    public Void visit(RfCase.RfCaseBranch rfCaseBranch) {
        return null;
    }

    @Override
    public Void visit(RfBody rfBody) {
        return null;
    }

}
