package cool.reflection.feature.features;

import cool.parser.CoolParser;
import cool.reflection.expression.RfExpression;
import cool.reflection.feature.RfFeature;
import cool.structures.Symbol;
import cool.structures.custom.symbols.IdSymbol;
import cool.structures.custom.symbols.ClassTypeSymbol;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfField extends RfFeature {

    private final Token fieldName;
    private final Token fieldType;
    private final RfExpression rfExpression;
    private IdSymbol idSymbolName;

    public RfField(CoolParser.FieldContext ctx, Token fieldName, Token fieldType, RfExpression rfExpression, Token token) {
        super(ctx, token);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.rfExpression = rfExpression;
    }

    public Token getFieldName() {
        return fieldName;
    }

    public Token getFieldType() {
        return fieldType;
    }

    public RfExpression getRfExpression() {
        return rfExpression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return (fieldName == null ? "null" : fieldName.getText()) + " : " + (fieldType == null ? "null" : fieldType.getText()) + (rfExpression != null ? (" <- " + rfExpression) : "");
    }

    public void setIdSymbol(IdSymbol idSymbolName) {
        this.idSymbolName = idSymbolName;
    }

    public IdSymbol getIdSymbolName() {
        return idSymbolName;
    }

    public void setIdSymbolType(Symbol symbolType) {
        if (!(symbolType instanceof ClassTypeSymbol))
            throw new IllegalStateException("Unmatched provided type for type symbol " + symbolType + " in context of field " + this);

        idSymbolName.setTypeSymbol((ClassTypeSymbol) symbolType);
    }
}
