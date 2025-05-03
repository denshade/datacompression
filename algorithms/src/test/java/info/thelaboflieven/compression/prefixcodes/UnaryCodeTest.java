package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnaryCodeTest {
    @Test
    public void testCode() {
        var unaryCode = new UnaryCode();
        assertTrue(getEncode(unaryCode, 1).getBitSet().get(0));
        assertFalse(getEncode(unaryCode, 1).getBitSet().get(1));
        assertTrue(getEncode(unaryCode, 2).getBitSet().get(0));
        assertTrue(getEncode(unaryCode, 2).getBitSet().get(1));
        assertFalse(getEncode(unaryCode, 2).getBitSet().get(2));
        assertTrue(getEncode(unaryCode, 3).getBitSet().get(0));
        assertTrue(getEncode(unaryCode, 3).getBitSet().get(1));
        assertTrue(getEncode(unaryCode, 3).getBitSet().get(2));
        assertFalse(getEncode(unaryCode, 3).getBitSet().get(3));
    }

    private BitSetStream getEncode(UnaryCode unaryCode, int number) {
        return unaryCode.encode(number, new BitSetStream());
    }

    @Test
    public void testDeCode() {
        var unaryCode = new UnaryCode();
        assertEquals(1, unaryCode.decode(getEncode(unaryCode, 1)));
        assertEquals(3, unaryCode.decode(getEncode(unaryCode, 3)));

    }

}