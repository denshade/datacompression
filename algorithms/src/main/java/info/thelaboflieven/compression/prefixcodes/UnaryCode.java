package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;

import java.util.BitSet;

public class UnaryCode {
    public BitSetStream encode(int number, BitSetStream bitSetStream) {
        if (number < 1) {
            throw new RuntimeException("number < 0: " + number);
        }
        var bitset = new BitSet();
        for (int i = 0; i < number; i++) {
            bitSetStream.addBit(true);
        }
        bitSetStream.addBit(false);
        return bitSetStream;
    }

    public long decode(BitSetStream bitSetStream) {
        BitSet set = bitSetStream.getBitSet();
        int i = 0;
        while (set.get(i++)) {}
        return i - 1;
    }
}
