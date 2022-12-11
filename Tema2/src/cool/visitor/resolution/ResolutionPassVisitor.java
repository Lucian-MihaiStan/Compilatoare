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
import cool.structures.custom.symbols.TypeSymbol;
import cool.structures.custom.symbols.constants.MethodSymbol;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

import java.util.Map;

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

        Map<String, Symbol> parameters = methodSymbol.getParameters();

        Symbol overridenMethodSymbol = currentScope.getParent().lookup(methodName.getText());
        if (overridenMethodSymbol instanceof MethodSymbol) {
            Map<String, Symbol> overriddenParameters = ((MethodSymbol) overridenMethodSymbol).getParameters();
            if (overriddenParameters.size() != parameters.size()) {
                SymbolTable.error(rfMethod.getContext(), rfMethod.getName(), new StringBuilder().append("Class ").append(((TypeSymbol) currentScope).getName()).append(" overrides method ").append(methodSymbol.getName()).append(" with different number of formal parameters").toString());
                return null;
            }
        }

        Scope initialScope = currentScope;
        currentScope = methodSymbol;

        rfMethod.getRfMethodBody().accept(this);

        currentScope = initialScope;

        return null;
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
        return null;
    }

    @Override
    public TypeSymbol visit(RfLet.RfDeclareVariable rfDeclareVariable) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfCase rfCase) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfCase.RfCaseBranch rfCaseBranch) {
        return null;
    }

    @Override
    public TypeSymbol visit(RfBody rfBody) {
        return null;
    }

}
