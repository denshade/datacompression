package info.thelaboflieven.compression.prefixcodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnaryCodeTest {
    @Test
    public void testCode() {
        var unaryCode = new UnaryCode();
        assertEquals(unaryCode.encode(1).get(0), true);
        assertEquals(unaryCode.encode(1).get(1), false);
        assertEquals(unaryCode.encode(2).get(0), true);
        assertEquals(unaryCode.encode(2).get(1), true);
        assertEquals(unaryCode.encode(2).get(2), false);
        assertEquals(unaryCode.encode(3).get(0), true);
        assertEquals(unaryCode.encode(3).get(1), true);
        assertEquals(unaryCode.encode(3).get(2), true);
        assertEquals(unaryCode.encode(3).get(3), false);
    }

    @Test
    public void testDeCode() {
        var unaryCode = new UnaryCode();
        assertEquals(1, unaryCode.decode(unaryCode.encode(1)));
        assertEquals(3, unaryCode.decode(unaryCode.encode(3)));

    }

}