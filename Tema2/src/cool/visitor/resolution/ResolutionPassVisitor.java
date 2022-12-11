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
import cool.structures.custom.symbols.TypeSymbol;
import cool.visitor.ASTVisitor;

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
