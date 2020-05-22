import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Lexer {
    private static final String OPERATOR_CHARS = "+-*/(){}[]=<>!&|,";//символы строки

    private static final Map<String, TokenType> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+",TokenType.PLUS);
        OPERATORS.put("-",TokenType.MINUS);
        OPERATORS.put("*",TokenType.STAR);
        OPERATORS.put("/",TokenType.SLASH);
        OPERATORS.put("(",TokenType.LP);
        OPERATORS.put(")",TokenType.RP);
        OPERATORS.put("{",TokenType.LB);
        OPERATORS.put("}",TokenType.RB);
        OPERATORS.put("[",TokenType.LBT);
        OPERATORS.put("]",TokenType.RBT);
        OPERATORS.put("=",TokenType.EQ);
        OPERATORS.put("<",TokenType.LESSER);
        OPERATORS.put(">",TokenType.GREATER);
        OPERATORS.put(",",TokenType.COMMA);

        OPERATORS.put("!",TokenType.EXCL);
        OPERATORS.put("&",TokenType.AMP);
        OPERATORS.put("|",TokenType.BAR);

        OPERATORS.put("==",TokenType.EQEQ);
        OPERATORS.put("!=",TokenType.EXCLEQ);
        OPERATORS.put("<=",TokenType.LEQ);
        OPERATORS.put(">=",TokenType.GEQ);
        OPERATORS.put("&&",TokenType.AMPAMP);
        OPERATORS.put("||",TokenType.BARBAR);
    }
    private final String input;
    private final int length;//длина строки
    private List<Token> tokens;//список токенов
    private int pos;

    public Lexer(String input) {//конструктор сохраняет строку(input) в поле
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {//возвращает список из токенов
        while (pos < length) {
            final char current = peek(0);//текущий символ
            if (Character.isDigit(current)) tokenizeNumber();//проверка число
            else if (Character.isLetter(current)) tokenizeWord();// проверка 16 числа
            else if (current == '#') {
                next();
                tokenizeHexNumber();

            } else if (current == '"') {//проверка текста
                tokenizeText();
            } else if (OPERATOR_CHARS.indexOf(current) != -1) {//проверка символ
                tokenizeOperator();
            } else {
                next();
            }

        }
        return tokens;
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {//пока идут числовые символы они добавляются в буффер
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid flout number");
            } else if (!Character.isDigit(current)) {
                break;
            }
            buffer.append(current);//добавляем в буффер тек  символ
            current = next();//след символ

        }
        addToken(TokenType.NUMBER, buffer.toString());//добавляем число и его текст
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();

        }
        addToken(TokenType.HEX_NUMBER, buffer.toString());
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    private void tokenizeOperator() {//берем позицию символа и добавляем его в список
        char current = peek(0);
        if(current == '/'){
            if(peek(1)=='/'){
                next();
                next();
                tokenizeComment();
                return;
            }else if (peek(1) == '*'){
                next();
                next();
                tokenizeMultilineComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while(true){
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text + current) && !text.isEmpty()){
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {

        if (!Character.isLetterOrDigit(current) && (current != '_') && (current != '$')){
            break;
        }
        buffer.append(current);//добавляем в буффер тек  символ
        current = next();}//след символ
            final String word = buffer.toString();
        switch (word){
            case "print" : addToken(TokenType.PRINT); break;
            case "if" : addToken(TokenType.IF); break;
            case "else" : addToken(TokenType.ELSE); break;
            case "while" : addToken(TokenType.WHILE); break;
            case "for" : addToken(TokenType.FOR); break;
            case "do" : addToken(TokenType.DO); break;
            case "break" : addToken(TokenType.BREAK); break;
            case "continue" : addToken(TokenType.CONTINUE); break;
            case "fun" : addToken(TokenType.DEF); break;
            case "return" : addToken(TokenType.RETURN); break;
            default:
                addToken(TokenType.WORD,word);//добавляем число и его текст
                  break;
        }
 }
    private void tokenizeText() {
        next();
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {
            if (current == '\\'){
                current = next();
                switch (current){
                    case '"' : current = next(); buffer.append('"');
                    continue;
                    case 'n' : current = next(); buffer.append('\n');
                        continue;
                    case 't' : current = next(); buffer.append('\t');
                        continue;
                }
                buffer.append('\\');
            }
            if (current == '"')break;
            buffer.append(current);//добавляем в буффер тек  символ
            current = next();}//след символ
        next();
            addToken(TokenType.TEXT,buffer.toString());//добавляем число и его текст

    }
private void tokenizeComment(){
 char current = peek(0);
 while ("\r\n\0".indexOf(current) == -1){
     current = next();
 }
}
private void tokenizeMultilineComment(){
    char current = peek(0);
    while (true){
        if (current == '\0') throw new RuntimeException("Missing close tag");
        if (current == '*' && peek(1) == '/') break;
        current = next();
    }
    next();
    next();
}
    private char next(){//увеличываем позицию и возвр тек символ
       pos++;
       return peek(0);
    }
private char peek(int relativePosition){//для просмотра символов
final int position = pos + relativePosition;//берем символ след после тек
if (position >= length)
    return '\0';
return input.charAt(position);
  }
private void addToken(TokenType type){
    addToken(type, "");
}//добавление токенов по типу(для операций)
private void addToken(TokenType type, String text){
    tokens.add(new Token(type,text));
}//добавление токенов по типу(для чисел)
}
