package info.thelaboflieven.compression.dictionary;

import info.thelaboflieven.compression.BitSetStream;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LZ77Test {
    @Test
    void roundAbout() {
        var lz = new LZ77();
        BitSetStream bitSetStream = new BitSetStream();
        lz.encode("Hello, this is a test".getBytes(), bitSetStream);
        var response = lz.decode(bitSetStream.asReadStream());
        assertEquals("Hello, this is a test".chars().boxed().map(e -> (char) e.intValue()).toList(), response);
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
        window.addString("abr");
        assertEquals(Optional.of(new LZ77.Match(0,3, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

    @Test
    void matchCheckerKnown2() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.addString("cabr");
        assertEquals(Optional.of(new LZ77.Match(1,3, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

    @Test
    void matchCheckerKnown3() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.addString("cabcabra");
        assertEquals(Optional.of(new LZ77.Match(4,4, '0')), searcher.findMatch(new StringBuffer("abra"), window));
    }

    @Test
    void matchCheckerKnown4() {
        var searcher = new LZ77.MatchSearcher();
        LZ77.Window window = new LZ77.Window();
        window.addString("Hello, thi");
        assertEquals(Optional.empty(), searcher.findMatch(new StringBuffer("s is a test"), window));
    }

}