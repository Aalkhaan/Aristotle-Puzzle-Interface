public class Token implements Comparable<Token>{
    private final int number;

    public Token(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        return number == ((Token) o).getNumber();
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public int compareTo(Token token) {
        return Integer.compare(number, token.getNumber());
    }
}
