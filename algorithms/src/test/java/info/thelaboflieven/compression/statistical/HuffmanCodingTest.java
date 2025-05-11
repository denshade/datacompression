package info.thelaboflieven.compression.statistical;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanCodingTest {
    @Test
    void check() {
        var data = "this is data";
        var setStream = new BitSetStream();

        var huffmanCoding = new HuffmanCoding();
        var t = huffmanCoding.createTree(data.getBytes());
        huffmanCoding.encode(data.getBytes(), setStream, t);
    }
}