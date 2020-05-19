package library;


public class NumberValue implements Value {
    public  static final Value  ZERO = new NumberValue(0);
    private final double value;
    public NumberValue(boolean value){
        this.value = value ? 1 : 0;
    }
     public NumberValue(double value){
         this.value = value;
     }
    public double asNumber(){
      return value;
    }
    public String asString(){
       return Double.toString(value);
    }
   public String toString(){
         return asString();
   }
}
