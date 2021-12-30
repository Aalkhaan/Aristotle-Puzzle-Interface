import java.util.LinkedList;
import java.util.List;

public class Row {
    private final int length;
    private final Slot[] slots;

    public Row(int length) {
        this.length = length;
        slots = new Slot[length];
    }

    public int lack() {
        int lack = 38;
        for (Slot slot : slots) lack -= slot.getNumber();
        return lack;
    }

    public boolean correct() {
        int sum = 0;
        for (Slot slot : slots) sum += slot.getNumber();
        return sum == 38;
    }

    public int getLength() {
        return length;
    }

    public void placeToken(Token token, int index) {
        slots[index].setToken(token);
    }

    public Slot getSlot(int index) {
        if (index < 0 || index >= slots.length) throw new IndexOutOfBoundsException();
        return slots[index];
    }

    public void setSlot(int index, Slot slot) {
        if (index < 0 || index >= slots.length) throw new IndexOutOfBoundsException();
        slots[index] = slot;
    }

    public List<Slot> emptySlots() {
        List<Slot> emptySlots = new LinkedList<>();
        for (Slot slot : slots) if (slot.isEmpty()) emptySlots.add(slot);
        return emptySlots;
    }
}
