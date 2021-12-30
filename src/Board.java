import java.util.*;

public class Board {
    private final Set<Token> unplaced;
    private final Set<Token> placed;
    private final Row[] rows;
    private final Row[] leftRows;
    private final Row[] rightRows;

    public Board() {
        unplaced = new TreeSet<>();
        placed = new HashSet<>();
        for (int i = 1; i <= 38; i++) {
            Token token = new Token(i);
            unplaced.add(token);
        }
        rows = new Row[5];
        leftRows = new Row[5];
        rightRows = new Row[5];
        rows[0] = new Row(3);
        rows[1] = new Row(4);
        rows[2] = new Row(5);
        rows[3] = new Row(4);
        rows[4] = new Row(3);
        leftRows[0] = new Row(3);
        leftRows[1] = new Row(4);
        leftRows[2] = new Row(5);
        leftRows[3] = new Row(4);
        leftRows[4] = new Row(3);
        rightRows[0] = new Row(3);
        rightRows[1] = new Row(4);
        rightRows[2] = new Row(5);
        rightRows[3] = new Row(4);
        rightRows[4] = new Row(3);

        leftRows[0].setSlot(0, rows[2].getSlot(0));
        leftRows[0].setSlot(1, rows[1].getSlot(0));
        leftRows[0].setSlot(2, rows[0].getSlot(0));
        leftRows[1].setSlot(0, rows[3].getSlot(0));
        leftRows[1].setSlot(1, rows[2].getSlot(1));
        leftRows[1].setSlot(2, rows[1].getSlot(1));
        leftRows[1].setSlot(3, rows[0].getSlot(1));
        leftRows[2].setSlot(0, rows[4].getSlot(0));
        leftRows[2].setSlot(1, rows[3].getSlot(1));
        leftRows[2].setSlot(2, rows[2].getSlot(2));
        leftRows[2].setSlot(3, rows[1].getSlot(2));
        leftRows[2].setSlot(4, rows[0].getSlot(2));
        leftRows[3].setSlot(0, rows[4].getSlot(1));
        leftRows[3].setSlot(1, rows[3].getSlot(2));
        leftRows[3].setSlot(2, rows[2].getSlot(3));
        leftRows[3].setSlot(3, rows[1].getSlot(3));
        leftRows[4].setSlot(0, rows[4].getSlot(2));
        leftRows[4].setSlot(1, rows[3].getSlot(3));
        leftRows[4].setSlot(2, rows[2].getSlot(4));

        rightRows[0].setSlot(0, leftRows[2].getSlot(0));
        rightRows[0].setSlot(1, leftRows[1].getSlot(0));
        rightRows[0].setSlot(2, leftRows[0].getSlot(0));
        rightRows[1].setSlot(0, leftRows[3].getSlot(0));
        rightRows[1].setSlot(1, leftRows[2].getSlot(1));
        rightRows[1].setSlot(2, leftRows[1].getSlot(1));
        rightRows[1].setSlot(3, leftRows[0].getSlot(1));
        rightRows[2].setSlot(0, leftRows[4].getSlot(0));
        rightRows[2].setSlot(1, leftRows[3].getSlot(1));
        rightRows[2].setSlot(2, leftRows[2].getSlot(2));
        rightRows[2].setSlot(3, leftRows[1].getSlot(2));
        rightRows[2].setSlot(4, leftRows[0].getSlot(2));
        rightRows[3].setSlot(0, leftRows[4].getSlot(1));
        rightRows[3].setSlot(1, leftRows[3].getSlot(2));
        rightRows[3].setSlot(2, leftRows[2].getSlot(3));
        rightRows[3].setSlot(3, leftRows[1].getSlot(3));
        rightRows[4].setSlot(0, leftRows[4].getSlot(2));
        rightRows[4].setSlot(1, leftRows[3].getSlot(3));
        rightRows[4].setSlot(2, leftRows[2].getSlot(4));
    }

    private boolean completable(Row row) {
        int lack = row.lacks();
        if (lack < 0) return false;
        List<Set<Token>> tokenSubsets = getSubsets(unplaced.stream().toList(), row.emptySlots());
        for (Set<Token> tokens : tokenSubsets) {
            int sum = 0;
            for (Token token : tokens) sum += token.getNumber();
            if (sum == lack) return true;
        }
        return false;
    }

    private boolean completable(Row[] rows) {
        for (Row row : rows) {
            if (!completable(row)) return false;
        }
        return true;
    }

    public boolean completable() {
        return completable(rows) && completable(leftRows) && completable(rightRows);
    }

    private static void getSubsets(List<Token> superSet, int k, int idx, Set<Token> current, List<Set<Token>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        Token x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }

    public static List<Set<Token>> getSubsets(List<Token> superSet, int k) {
        List<Set<Token>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<>(), res);
        return res;
    }

    public void placeToken(Token token, int index) {
        if (placed.contains(token)) throw new IllegalArgumentException("This token is already placed");
        if (index < 0 || index > 18) throw new IllegalArgumentException("La position d'un jeton est comprise" +
                " entre 0 et 18");
        if (index <= 2) rows[0].placeToken(token, index);
        else if (index <= 6) rows[1].placeToken(token, index - 4);
        else if (index <= 11) rows[2].placeToken(token, index - 7);
        else if (index <= 15) rows[3].placeToken(token, index - 11);
        else rows[4].placeToken(token, index - 16);
        unplaced.remove(token);
        placed.add(token);
    }
}
