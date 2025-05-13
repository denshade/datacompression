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

    public void writeByte(byte data) {
        for (int i = 0; i < 8; i++) {
            if ((1 << i & data) > 0 ) {
                addBit(true);
            } else {
                addBit(false);
            }
        }
    }

    public byte readByte() {
        byte data = 0;
        for (int i = 0; i < 8; i++) {
            if (readBit()) {
                data += 1 << i;
            }
        }
        return data;
    }
}
