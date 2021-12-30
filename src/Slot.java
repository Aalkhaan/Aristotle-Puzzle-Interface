public class Slot {
    private Token token;

    public Slot() {
        token = null;
    }

    public Slot(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public int getNumber() {
        if (token == null) return 0;
        return token.getNumber();
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isEmpty() {
        return token == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "__";
        return String.valueOf(token.getNumber());
    }
}
