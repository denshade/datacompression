package info.thelaboflieven.compression;

import java.util.BitSet;

public class BitSetStream {
    private int currentIndex = 0;
    private int maxIndex = 0;
    private BitSet bitset = new BitSet();

    public void addBit(boolean flag) {
        bitset.set(currentIndex++, flag);
    }

    public boolean readBit() {
        return bitset.get(currentIndex++);
    }
    public boolean hasBitsLeft() {
        return currentIndex <= maxIndex;
    }

    public BitSetStream asReadStream() {
        var set = new BitSetStream();
        set.maxIndex = currentIndex;
        set.bitset = bitset;
        set.currentIndex = 0;
        return set;
    }

    public BitSet getBitSet() {
        return bitset;
    }

    public int nrBits() {
        return currentIndex;
    }

}
