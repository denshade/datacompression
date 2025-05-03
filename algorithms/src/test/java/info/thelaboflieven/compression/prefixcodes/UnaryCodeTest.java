package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnaryCodeTest {
    @Test
    public void testCode() {
        var unaryCode = new UnaryCode();
        assertEquals(unaryCode.encode(1, new BitSetStream()).getBitSet().get(0), true);
        assertEquals(unaryCode.encode(1, new BitSetStream()).getBitSet().get(1), false);
        assertEquals(unaryCode.encode(2, new BitSetStream()).getBitSet().get(0), true);
        assertEquals(unaryCode.encode(2, new BitSetStream()).getBitSet().get(1), true);
        assertEquals(unaryCode.encode(2, new BitSetStream()).getBitSet().get(2), false);
        assertEquals(unaryCode.encode(3, new BitSetStream()).getBitSet().get(0), true);
        assertEquals(unaryCode.encode(3, new BitSetStream()).getBitSet().get(1), true);
        assertEquals(unaryCode.encode(3, new BitSetStream()).getBitSet().get(2), true);
        assertEquals(unaryCode.encode(3, new BitSetStream()).getBitSet().get(3), false);
    }

    @Test
    public void testDeCode() {
        var unaryCode = new UnaryCode();
        assertEquals(1, unaryCode.decode(unaryCode.encode(1, new BitSetStream())));
        assertEquals(3, unaryCode.decode(unaryCode.encode(3, new BitSetStream())));

    }

}