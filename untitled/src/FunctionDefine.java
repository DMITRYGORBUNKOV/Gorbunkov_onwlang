import library.Functions;

import java.util.List;

public class FunctionDefine implements Statement{
    private final String name ;
    private final List<String> argNames;
    private final Statement body;
    public FunctionDefine(String name,List<String> argNames, Statement body ){
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }
    public void execute(){
Functions.set(name, new UserDefineFunction(argNames,body));
    }
    public String toString(){
        return "fun (" + argNames.toString() + ") " + body.toString();
    }
}
