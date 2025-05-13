package info.thelaboflieven.compression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitSetStreamTest {

    @Test
    void readWriteByte() {
        var set = new BitSetStream();
        set.writeByte((byte)255);
        var l = set.asReadStream().readByte();
        assertEquals((byte)255, l);
        set = new BitSetStream();
        set.writeByte((byte)0);
        l = set.asReadStream().readByte();
        assertEquals(0, l);
    }

}