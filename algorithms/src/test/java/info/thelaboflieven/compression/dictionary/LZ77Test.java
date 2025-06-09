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

    @Test
    void matchCheckerUnknown() {
        var searcher = new LZ77.MatchSearcher();
        assertEquals(new LZ77.Match(0,0, 'h'), searcher.findMatch(new StringBuffer(), new LZ77.Window("hello".toCharArray(), 0)));
    }

    @Test
    void matchCheckerKnown() {
        var searcher = new LZ77.MatchSearcher();
        assertEquals(new LZ77.Match(0,0, 'h'), searcher.findMatch(new StringBuffer("abra"), new LZ77.Window("a".toCharArray(), 0)));
    }

}