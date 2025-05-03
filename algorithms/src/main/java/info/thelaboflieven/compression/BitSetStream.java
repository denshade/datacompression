package info.thelaboflieven.compression;

import java.util.BitSet;

public class BitSetStream {
    private int currentIndex = 0;
    private BitSet bitset = new BitSet();

    public void addBit(boolean flag) {
        bitset.set(currentIndex++, flag);
    }

    public BitSet getBitSet() {
        return bitset;
    }

    public int nrBits() {
        return currentIndex;
    }

}
