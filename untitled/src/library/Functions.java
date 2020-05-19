package library;
import java.util.HashMap;
import java.util.Map;

public class Functions {
    private static final NumberValue ZERO= new NumberValue(0);
    private static Map<String , Functionf> functions;
    static {
        functions = new HashMap<>();
        functions.put("sin", new Functionf() {

            public Value execute(Value... args){
             if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.sin(args[0].asNumber()));
            }
        });
        functions.put("cos",(Functionf) (Value... args) -> {
                if (args.length != 1) throw new RuntimeException("One args expected");
                return new NumberValue(Math.cos(args[0].asNumber()));
        });
        functions.put("tan",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.tan(args[0].asNumber()));
        });
        functions.put("sqrt",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.sqrt(args[0].asNumber()));
        });
        functions.put("abs",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.abs(args[0].asNumber()));
        });
        functions.put("cbrt",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        });
        functions.put("log",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.log(args[0].asNumber()));
        });
        functions.put("rounding",(Functionf) (Value... args) -> {
            if (args.length != 1) throw new RuntimeException("One args expected");
            return new NumberValue(Math.rint(args[0].asNumber()));
        });
        functions.put("con" , args -> {
           for (Value arg : args){
               System.out.println(arg.asString());
           }
            return ZERO;
        });
        functions.put("mas" ,  args -> {
            return new ArrayValue(args);
        });
    }
    public static boolean isExists(String key){
        return functions.containsKey(key);
    }
    public static Functionf get(String key){//получение константы по ключу
        if (!isExists(key)) throw new RuntimeException("Unknown function" + key);
        return functions.get(key);
    }

    public static void set(String key , Functionf functionf){
        functions.put(key, functionf);
    }
}
