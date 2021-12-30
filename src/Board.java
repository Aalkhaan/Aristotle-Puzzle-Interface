import java.util.*;

public class Board {
    private final Map<Integer, Token> unplaced;
    private final Map<Token, Slot> placed;
    private final Slot[] slots;
    private final Row[] rows;
    private final Row[] leftRows;
    private final Row[] rightRows;

    public Board() {
        unplaced = new HashMap<>();
        placed = new HashMap<>();
        slots = new Slot[19];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot();
        }
        for (int i = 1; i <= 38; i++) {
            Token token = new Token(i);
            unplaced.put(token.getNumber(), token);
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

        int i;
        for (i = 0; i <= 2; i++) rows[0].setSlot(i, slots[i]);
        for (i = 3; i <= 6; i++) rows[0].setSlot(i - 3, slots[i]);
        for (i = 7; i <= 11; i++) rows[0].setSlot(i - 7, slots[i]);
        for (i = 12; i <= 15; i++) rows[0].setSlot(i - 12, slots[i]);
        for (i = 16; i <= 18; i++) rows[0].setSlot(i - 16, slots[i]);

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

    private boolean notFillable(Row row) {
        int lack = row.lack();
        if (lack < 0) return true;
        List<Set<Token>> tokenSubsets = getSubsets(unplaced.values().stream().toList(), row.emptySlots().size());
        for (Set<Token> tokens : tokenSubsets) {
            int sum = 0;
            for (Token token : tokens) sum += token.getNumber();
            if (sum == lack) return false;
        }
        return true;
    }

    private boolean completable(Row[] rows) {
        for (Row row : rows) {
            if (notFillable(row)) return false;
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
        if (placed.containsKey(token)) throw new IllegalArgumentException("This token is already placed");
        if (index < 0 || index > 18) throw new IllegalArgumentException("La position d'un jeton est comprise" +
                " entre 0 et 18");
        slots[index].setToken(token);
        unplaced.remove(token.getNumber());
        placed.put(token, slots[index]);
    }

    private void placeToken(Token token, Slot slot) {
        if (placed.containsKey(token)) throw new IllegalArgumentException("This token is already placed");
        slot.setToken(token);
        unplaced.remove(token.getNumber());
        placed.put(token, slot);
    }

    public void unplaceToken(Token token) {
        if (!placed.containsKey(token)) throw new IllegalArgumentException("This token isn't placed yet");
        placed.get(token).setToken(null);
        placed.remove(token);
        unplaced.put(token.getNumber(), token);
    }

    public boolean forcedComplete() {
        for (Row row : rows) {
            if (notFillable(row)) return false;
            List<Slot> emptySlots = row.emptySlots();
            if (emptySlots.size() == 1) {
                int lack = row.lack();
                if (unplaced.containsKey(lack)) {
                    placeToken(unplaced.get(lack), emptySlots.get(0));
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String blank = "    ";
        String halfBlank = "  ";
        int i;
        StringBuilder toReturn = new StringBuilder(blank);
        for (i = 0; i <= 2; i++) {
            toReturn.append(halfBlank);
            String slot = slots[i].toString();
            if (slot.length() == 1) toReturn.append(" ").append(slots[i].toString()).append(halfBlank);
            else toReturn.append(slots[i].toString()).append(halfBlank);
        }
        toReturn.append("\n").append(halfBlank);
        for (i = 3; i <= 6; i++) {
            toReturn.append(halfBlank);
            String slot = slots[i].toString();
            if (slot.length() == 1) toReturn.append(" ").append(slots[i].toString()).append(halfBlank);
            else toReturn.append(slots[i].toString()).append(halfBlank);
        }
        toReturn.append("\n");
        for (i = 7; i <= 11; i++) {
            toReturn.append(halfBlank);
            String slot = slots[i].toString();
            if (slot.length() == 1) toReturn.append(" ").append(slots[i].toString()).append(halfBlank);
            else toReturn.append(slots[i].toString()).append(halfBlank);
        }
        toReturn.append("\n").append(halfBlank);
        for (i = 12; i <= 15; i++) {
            toReturn.append(halfBlank);
            String slot = slots[i].toString();
            if (slot.length() == 1) toReturn.append(" ").append(slots[i].toString()).append(halfBlank);
            else toReturn.append(slots[i].toString()).append(halfBlank);
        }
        toReturn.append("\n").append(blank);
        for (i = 16; i <= 18; i++) {
            toReturn.append(halfBlank);
            String slot = slots[i].toString();
            if (slot.length() == 1) toReturn.append(" ").append(slots[i].toString()).append(halfBlank);
            else toReturn.append(slots[i].toString()).append(halfBlank);
        }
        return toReturn.toString();
    }
}
