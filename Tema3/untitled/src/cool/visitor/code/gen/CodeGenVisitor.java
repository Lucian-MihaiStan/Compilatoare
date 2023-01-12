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
import cool.reflection.feature.features.RfField;
import cool.reflection.feature.features.RfMethod;
import cool.reflection.type.RfBool;
import cool.reflection.type.RfInt;
import cool.reflection.type.RfString;
import cool.structures.custom.symbols.constants.TypeSymbolConstants;
import cool.visitor.ASTVisitor;
import org.stringtemplate.v4.ST;

public class CodeGenVisitor implements ASTVisitor<ST> {

    private final CodeGenManager manager;
    private int i;
    private int offset;

    public CodeGenVisitor(CodeGenManager manager) {
        this.manager = manager;
    }

    @Override
    public ST visit(RfProgram rfProgram) {
//        rfProgram.getRfClasses().forEach(clazz -> clazz.accept(this));
        return manager.getTemplate(CodeGenVisitorConstants.PROGRAM_PATTERN)
                .add(CodeGenVisitorConstants.DISPATCH_TABLES, manager.getDispatchTables())
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
                .add(CodeGenVisitorConstants.CLASS_NAME_TAB, manager.getClassNameTab())
                .add(CodeGenVisitorConstants.CLASS_OBJECTS_TAB, manager.getClassObjectsTab())
                .add(CodeGenVisitorConstants.CLASS_PROTOTYPES_TAB, manager.getClassPrototypesTab())
                .add(CodeGenVisitorConstants.DISPATCH_TABLES, manager.getDispatchTables())
                .add(CodeGenVisitorConstants.HEAP_START_TAB, manager.getHeapStartTab());
    }

    @Override
    public ST visit(RfClass rfClass) {
        return null;
    }

    @Override
    public ST visit(RfField rfField) {
        return null;
    }

    @Override
    public ST visit(RfInt rfInt) {
        return null;
    }

    @Override
    public ST visit(RfArgument rfArgument) {
        return null;
    }

    @Override
    public ST visit(RfMethod rfMethod) {
        return null;
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
