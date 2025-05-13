package info.thelaboflieven.compression.statistical;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanCodingTest {
    @Test
    void check() {
        var data = "this is data";
        var setStream = new BitSetStream();

        var huffmanCoding = new HuffmanCoding();
        var t = huffmanCoding.createTree(data.getBytes());
        huffmanCoding.encode(data.getBytes(), setStream, t);
        var result = huffmanCoding.decode(setStream.asReadStream(), t);
        var b = data.getBytes();
        for (int i = 0; i < b.length; i++) {
            assertEquals(b[i], result[i]);
        }
    }

    @Test
    void readWriteTree() {
        var data = "this is data";
        var setStream = new BitSetStream();

        var huffmanCoding = new HuffmanCoding();
        var t = huffmanCoding.createTree(data.getBytes());
        BitSetStream bitSetStream = new BitSetStream();
        huffmanCoding.writeTree(t, bitSetStream);
        var l = huffmanCoding.readTree(bitSetStream.asReadStream());
        assertEquals(l.toString(), t.toString());
    }
}