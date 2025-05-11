package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;
import info.thelaboflieven.compression.prefixcodes.UnaryCode;

import java.util.BitSet;

public class GolombCoding {

    BitSetStream encode(int m, int n, BitSetStream bitSetStream) {
        var q = n / m;
        var r = n % m;
        var u = new UnaryCode();
        u.encode(q, bitSetStream);
        truncatedBinary(r, n, bitSetStream);
        return bitSetStream;
    }

    private int log2(int m) {
        if (m <= 0) {
            throw new IllegalArgumentException("Input must be a positive integer.");
        }

        int result = -1;
        while (m > 0) {
            m >>>= 1; // Unsigned right shift
            result++;
        }
        return result;
    }

    long decode(int m, BitSetStream set) {
        var u = new UnaryCode();
        long q = u.decode(set);
        var result = q * m;
        var b = log2(m);
        var r = convert(set, b);
        long comp = (long) Math.pow(2, b + 1) - m;
        if (r < comp) {
            return result + r;
        } else {
            var r2 = convert(set, b + 1);
            return result + m - (long)Math.pow(2, b + 1) + r2;
        }
    }

    public static long convert(BitSetStream bits, int len) {
        long value = 0L;
        for (int i = 0; i < len; ++i) {
            value += bits.readBit() ? (1L << i) : 0L;
        }
        return value;
    }

    BitSetStream truncatedBinary(int x, int n, BitSetStream bitSetStream)
    {
        // Set k = floor(log2(n)), i.e., k such that 2^k <= n < 2^(k+1).
        int k = 0, t = n;
        while (t > 1) { k++;  t >>= 1; }

        // Set u to the number of unused codewords = 2^(k+1) - n.
        int u = (1 << k + 1) - n;

        if (x < u)
            return binary(x, k, bitSetStream);
        else
            return binary(x + u, k + 1, bitSetStream);
    }
    BitSetStream binary(int x, int len, BitSetStream bitSetStream)
    {
        int start = bitSetStream.nrBits();
        while (x != 0) {
            bitSetStream.addBit(x % 2 != 0);
            x >>= 1;
        }
        while (bitSetStream.nrBits() - start < len ) {
            bitSetStream.addBit(false);
        }
        return bitSetStream;
    }
}
