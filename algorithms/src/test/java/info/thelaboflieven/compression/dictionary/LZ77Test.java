package info.thelaboflieven.compression.dictionary;

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
        lz.encode(new BufferedReader(new CharArrayReader("Hello, this is a test".toCharArray())), new PrintWriter(out));
        lz.decode(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray()))), new PrintWriter(result));
        assertEquals("Hello, this is a test".getBytes(), result.toByteArray());
    }

}