package info.thelaboflieven.compression.dictionary;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LZ77Test {
    @Test
    void check() throws IOException {
        var lz = new LZ77();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        BitSetStream bitSetStream = new BitSetStream();
        lz.encode("Hello, this is a test".getBytes(), bitSetStream);
        lz.decode(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray()))), new PrintWriter(result));
        assertEquals("Hello, this is a test".getBytes(), result.toByteArray());
    }

}