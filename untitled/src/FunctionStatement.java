
public final class FunctionStatement implements Statement {
    private final FunctionalExpression function;
    public FunctionStatement(FunctionalExpression function){
        this.function = function;
    }
    @Override
    public void execute() {
    function.eval();
    }
    public String toString(){
        return function.toString();
    }
}
