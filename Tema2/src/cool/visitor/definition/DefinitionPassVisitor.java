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
import cool.structures.custom.symbols.TypeSymbol;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;

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
