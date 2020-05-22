public final class Token {
    private TokenType type;
    private String text;//хранит имя переменной
    public Token(TokenType type, String text ){
        this.type = type;
        this.text = text;

    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {

        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String toString(){
        return type +" " + text ;
    }
}
