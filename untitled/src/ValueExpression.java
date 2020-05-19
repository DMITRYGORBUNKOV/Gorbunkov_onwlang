import library.NumberValue;
import library.StringValue;
import library.Value;

public class ValueExpression implements Expression{
    private final Value value;
    public ValueExpression(double value){
        this.value = new NumberValue(value);
    }
    public ValueExpression(String value){
        this.value = new StringValue(value);
    }
    @Override
    public Value eval() {
        return value;
    }
    public String toString (){
        return value.asString();
    }

}
