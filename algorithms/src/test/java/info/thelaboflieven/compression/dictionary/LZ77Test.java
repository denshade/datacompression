package info.thelaboflieven.compression.dictionary;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LZ77Test {
    @Test
    void roundAbout() throws IOException {
        var lz = new LZ77();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        BitSetStream bitSetStream = new BitSetStream();
        lz.encode("Hello, this is a test".getBytes(), bitSetStream);
        lz.decode(bitSetStream.asReadStream());
        assertEquals("Hello, this is a test".getBytes(), result.toByteArray());
    }

    @Test
    void matchCheckerUnknown() {
        var searcher = new LZ77.MatchSearcher();
        assertEquals(Optional.empty(), searcher.findMatch(new StringBuffer(), new LZ77.Window()));
    }

    @Test
    void matchCheckerKnown() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.add('a').add('b').add('r');
        assertEquals(Optional.of(new LZ77.Match(0,3, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

    @Test
    void matchCheckerKnown2() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.add('c').add('a').add('b').add('r');
        assertEquals(Optional.of(new LZ77.Match(1,3, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

    @Test
    void matchCheckerKnown3() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.add('c').add('a').add('b').add('c').add('a').add('b').add('r').add('a');
        assertEquals(Optional.of(new LZ77.Match(4,4, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

}