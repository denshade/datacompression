package info.thelaboflieven.compression.prefixcodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GolombCodingTest {

    @Test
    void encode() {
        var g = new GolombCoding();
        var b = g.encode(10, 11);
        assertEquals(11, g.decode(10, b));
    }

}