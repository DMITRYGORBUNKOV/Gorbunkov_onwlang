import java.util.ArrayList;
import java.util.List;

public final class Parser {
    private static final Token EOF = new Token (TokenType.EOF,"");
private final List<Token> tokens;//список токенов
private final int size;
private int pos;

public Parser(List<Token> tokens){
    this.tokens = tokens;
    size = tokens.size();
}
public Statement parse(){
    final BlockStatement result = new BlockStatement();
    while (!match(TokenType.EOF)){
        result.add(statement());//добав выражение
    }
    return result;
}
    private Statement block(){
    final  BlockStatement block = new BlockStatement();
    consume(TokenType.LB);
    while(!match(TokenType.RB)){
        block.add(statement());
    }
    return block;
}
    private Statement statementOrBlock(){
    if(get(0).getType() == TokenType.LB) return block();
    return statement();
    }
    private Statement statement(){
    if(match(TokenType.PRINT)){
        return new PrintStatement(expression());
    }
    if (match(TokenType.IF)){
        return ifElse();
    }
        if (match(TokenType.WHILE)){
            return whileStatement();
        }
        if (match(TokenType.DO)){
            return doWhileStatement();
        }
        if (match(TokenType.BREAK)){
            return new BreakStatement();
        }
        if (match(TokenType.CONTINUE)){
            return new ContinueStatement();
        }
        if (match(TokenType.RETURN)){
            return new ReturnStatement(expression());
        }
        if (match(TokenType.FOR)){
            return forStatement();
        }
        if (match(TokenType.DEF)){
            return functionDefine();
        }
        if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LP){
            return new FunctionStatement(function());
        }
    return assignmentStatement();
    }

    private Statement assignmentStatement() {
    if(get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.EQ){
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.EQ);
        return new AssignmentStatement(variable, expression());
    }
    if(get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBT){
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.LBT);
        final Expression index = expression();
        consume(TokenType.RBT);
        consume(TokenType.EQ);
        return new ArrayAssignmentStatement(variable,index,expression());
    }
 throw new RuntimeException("Unknown statement ");
    }
    private Statement ifElse() {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.ELSE)){
            elseStatement = statementOrBlock();
        }else{
            elseStatement = null;
        }
        return new IfStatement(condition,ifStatement,elseStatement);
    }
  private Statement whileStatement(){
    final Expression condition = expression();
    final Statement statement = statementOrBlock();
    return new WhileStatement(condition,statement);
  }
    private Statement doWhileStatement(){
        final Statement statement = statementOrBlock();
        consume(TokenType.WHILE);
        final Expression condition = expression();
        return new DoWhileStatement(condition,statement);
    }
  private Statement forStatement(){
     final Statement initialization = assignmentStatement();
     consume(TokenType.COMMA);
     final Expression termination = expression();
      consume(TokenType.COMMA);
     final Statement increment = assignmentStatement();
     final Statement statement = statementOrBlock();
     return new ForStatement(initialization, termination, increment, statement);

  }
    private FunctionDefine functionDefine(){
        final String name = consume(TokenType.WORD).getText();
        consume(TokenType.LP);
        final List<String> argNames = new ArrayList<>();
        while (!match(TokenType.RP)){
            argNames.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        final Statement body = statementOrBlock();
        return new FunctionDefine(name,argNames,body);
    }
    private FunctionalExpression function(){
    final String name = consume(TokenType.WORD).getText();
    consume(TokenType.LP);
    final FunctionalExpression function = new FunctionalExpression(name);
    while (!match(TokenType.RP)){
        function.addArgument(expression());
        match(TokenType.COMMA);
    }
    return function;
    }
    private Expression array(){
    consume(TokenType.LBT);
    final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBT)){
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }
    private Expression element(){
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.LBT);
        final Expression index = expression();
        consume(TokenType.RBT);
        return new ArrayAccessExpression(variable,index);
    }
    private Expression expression(){
    return logicalOr();
}
    private Expression logicalOr(){
    Expression result = logicalAnd();
        while (true) {
            if (match(TokenType.BARBAR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

    return result;
    }
    private Expression logicalAnd(){
        Expression result = equality();
        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND,result,equality());
                continue;
            }
            break;
        }

        return result;
    }
    private Expression equality(){
        Expression result = conditionl();
    if (match(TokenType.EQEQ)){
        return new ConditionalExpression(ConditionalExpression.Operator.EQUALS,result,conditionl());

    }
        if (match(TokenType.EXCLEQ)){
           return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS,result,conditionl());

        }
        return result;
    }
    private Expression conditionl(){//лог выр
        Expression result = additive();
        while (true){
            if (match(TokenType.LESSER)){
                result = new ConditionalExpression(ConditionalExpression.Operator.LT,result,additive());
                continue;
            }
            if (match(TokenType.LEQ)){
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ,result,additive());
                continue;
            }
            if (match(TokenType.GREATER)){
                result = new ConditionalExpression(ConditionalExpression.Operator.GT,result,additive());
                continue;
            }
            if (match(TokenType.GEQ)){
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ,result,additive());
                continue;
            }
            break;
        }
        return result;
    }
private Expression additive() {
    Expression result = multiplicative();
    while (true){
        if (match(TokenType.PLUS)){
            result = new BinaryExpression('+',result,multiplicative());
            continue;
        }
        if (match(TokenType.MINUS)){
            result = new BinaryExpression('-',result,multiplicative());
            continue;
        }
        break;
    }
return result;
    }
private Expression multiplicative(){
    Expression result = unary();
    while (true){
        if (match(TokenType.STAR)){
            result = new BinaryExpression('*',result,unary());
       continue;
        }
        if (match(TokenType.SLASH)){
           result = new BinaryExpression('/',result,unary());
        continue;
        }
        break;
    }
return result;
}
private Expression unary(){
    if (match(TokenType.MINUS)){
        return new UnaryExpression('-', primary());
    }
    if (match(TokenType.PLUS)){
        return primary();
    }

return primary();
}
private Expression primary(){//числа строки
final Token current = get(0);
if (match(TokenType.NUMBER)){
    return new ValueExpression(Double.parseDouble (current.getText()));
}
    if (match(TokenType.HEX_NUMBER)){
        return new ValueExpression(Long.parseLong (current.getText(), 16));
    }
    if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LP){
        return function();
    }if (get(0).getType() == TokenType.WORD && get(1).getType() == TokenType.LBT){
        return element();
    }if (get(0).getType() == TokenType.LBT ){
        return array();
    }

    if (match(TokenType.WORD)){
        return new VariableExpression(current.getText());
    }
    if (match(TokenType.TEXT)){
        return new ValueExpression(current.getText());
    }
if (match(TokenType.LP)){
    Expression result = expression();
    match(TokenType.RP);
    return result;
}
throw new RuntimeException("Unknown expression");
}
    private Token consume(TokenType type){//проверка типа токена
        final Token current = get(0);
        if (type != current.getType()) throw new RuntimeException("Token " + current + "doesn't match" + type);
        pos++;
        return current;
    }
private boolean match(TokenType type){//проверка типа токена
    final Token current = get(0);
    if (type != current.getType()) return false;
    pos++;
    return true;
}
    private Token get(int relativePosition){//получение тек символа
        final int position = pos + relativePosition;
        if (position >= size)
            return EOF;
        return tokens.get(position);
    }
}
