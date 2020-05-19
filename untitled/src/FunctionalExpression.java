
import library.Functionf;
import library.Functions;
import library.Value;
import library.Variables;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalExpression implements Expression {
   private final String name;
   private final List<Expression> arguments;
   public FunctionalExpression (String name){
       this.name = name;
       arguments = new ArrayList<>();
   }
   public FunctionalExpression(String name,List<Expression> arguments){
       this.name = name;
       this.arguments = arguments;
   }
   public void addArgument (Expression arg){
       arguments.add(arg);
   }
    @Override
    public Value eval() {
       final int size = arguments.size();
     Value[] values = new Value[size];
     for (int i = 0; i < size; i++){
         values[i] = arguments.get(i).eval();
     }
     final Functionf functionf = Functions.get(name);
     if (functionf instanceof UserDefineFunction){
         final UserDefineFunction userFunctionf = (UserDefineFunction) functionf;
         if(size != userFunctionf.getArgsCount()) throw new RuntimeException("Args count mismatch");
         Variables.push();
         for(int i = 0; i < size ; i++){
             Variables.set(userFunctionf.getArgsName(i),values[i]);
         }
         final Value result = userFunctionf.execute(values);
         Variables.pop();
         return result;
     }
     return functionf.execute(values);
    }

    @Override
    public String toString() {
        return name + "(" + arguments.toString() + ")";
    }
}
