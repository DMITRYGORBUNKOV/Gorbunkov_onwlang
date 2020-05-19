import library.Functionf;
import library.NumberValue;
import library.Value;

import java.util.List;

public final class UserDefineFunction implements Functionf {
    private final List<String> argNames;
    private final Statement body;
    public UserDefineFunction(List<String> argNames, Statement body){
        this.argNames = argNames;
        this.body = body;
    }
    public int getArgsCount(){
        return argNames.size();
    }
    public String getArgsName(int index){
        if (index < 0 || index >= getArgsCount()) return "";
        return argNames.get(index);
    }
    public Value execute(Value... args){
        try{
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt){
            return rt.getResult();
        }
    }
}
