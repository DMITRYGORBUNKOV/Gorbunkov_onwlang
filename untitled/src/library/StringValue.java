package library;



public class StringValue implements Value {
    private final String value;

    public StringValue(String value){
        this.value = value;
    }
    public double asNumber(){
       try {
           return Double.parseDouble(value);
       }catch (NumberFormatException e){
           return 0;
       }
    }
    public String asString(){
        return value;
    }
    public String toString(){
        return asString();
    }

}
