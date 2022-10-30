package cool.reflection.feature.features;

import cool.reflection.expression.RfExpression;
import cool.reflection.feature.RfFeature;
import cool.visitor.ASTVisitor;
import org.antlr.v4.runtime.Token;

public class RfField extends RfFeature {

    private final String fieldName;
    private final String fieldType;
    private final RfExpression rfExpression;

    public RfField(String text, String s, RfExpression rfExpression, Token token) {
        super(token);
        this.fieldName = text;
        this.fieldType = s;
        this.rfExpression = rfExpression;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public RfExpression getRfExpression() {
        return rfExpression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
