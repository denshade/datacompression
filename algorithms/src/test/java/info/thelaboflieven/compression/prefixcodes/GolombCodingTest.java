package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GolombCodingTest {

    @Test
    void encode() {
        var g = new GolombCoding();
        var b = g.encode(10, 11, new BitSetStream());
        assertEquals(11, g.decode(10, b.asReadStream()));
        b = g.encode(10, 12, new BitSetStream());
        assertEquals(12, g.decode(10, b.asReadStream()));
    }

}