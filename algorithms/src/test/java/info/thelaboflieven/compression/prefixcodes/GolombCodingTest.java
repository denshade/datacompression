package info.thelaboflieven.compression.prefixcodes;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GolombCodingTest {

    @Test
    void encode() {
        var g = new GolombCoding();

        var b = g.encode(10, 13, new BitSetStream());
        assertEquals(13, g.decode(10, b.asReadStream()));

        for (int i = 10; i < 20; i++) {
            b = g.encode(10, i, new BitSetStream());
            assertEquals(i, g.decode(10, b.asReadStream()));
        }
    }

}