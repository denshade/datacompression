package info.thelaboflieven.compression.prefixcodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GolombCodingTest {

    @Test
    void encode() {
        var g = new GolombCoding();
        g.encode(10, 11);
    }

}