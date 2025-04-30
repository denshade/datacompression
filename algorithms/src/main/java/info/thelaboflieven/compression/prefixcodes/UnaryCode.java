package info.thelaboflieven.compression.prefixcodes;

import java.util.BitSet;

public class UnaryCode {
    public BitSet encode(int number) {
        if (number < 1) {
            throw new RuntimeException("number < 0: " + number);
        }
        var bitset = new BitSet();
        for (int i = 0; i < number; i++) {
            bitset.set(i);
        }
        bitset.set(number + 1, false);
        return bitset;
    }

    public long decode(BitSet set) {
        int i = 0;
        while (set.get(i++)) {}
        return i - 1;
    }
}
