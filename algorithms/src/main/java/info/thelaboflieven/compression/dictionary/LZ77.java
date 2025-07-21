package info.thelaboflieven.compression.dictionary;

import info.thelaboflieven.compression.BitSetStream;
import info.thelaboflieven.compression.prefixcodes.UnaryCode;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LZ77 {

    public record Match(int startIndex, int length, char nextCharInInput) {
    }

    public static class Window {
        private List<Character> chars = new ArrayList<>();
        public long length() {
            return chars.size();
        }

        public char charAt(int index) {
            return chars.get(index);
        }

        public Window add(char input) {
            chars.add(input);
            return this;
        }
    }

    public static class MatchSearcher {
        public Optional<Match> findMatch(StringBuffer buffer, Window window) {
            List<Match> matches = new ArrayList<>();
            for (int windowIndex = 0; windowIndex < window.length(); windowIndex++) {
                var q = new ArrayList<Character>();
                for (int bufferIndex = 0; windowIndex + bufferIndex < window.length() && bufferIndex < buffer.length(); bufferIndex++) {
                    if (window.charAt(windowIndex + bufferIndex) == buffer.charAt(bufferIndex)) {
                        q.add(buffer.charAt(bufferIndex));
                    }
                    else {
                        if (q.size() > 1) {
                            matches.add(new Match(windowIndex, q.size(), buffer.charAt(bufferIndex)));
                            q.clear();
                        }
                    }
                }
                if (q.size() > 1) {
                    matches.add(new Match(windowIndex, q.size(), '0'));
                }
            }
            return matches.stream().max(Comparator.comparingInt(a -> a.length));
        }
    }
    /**
     * while input is not empty do
     *     match := longest repeated occurrence of input that begins in window
     *
     *     if match exists then
     *         d := distance to start of match
     *         l := length of match
     *         c := char following match in input
     *     else
     *         d := 0
     *         l := 0
     *         c := first char of input
     *     end if
     *
     *     output (d, l, c)
     *
     *     discard l + 1 chars from front of window
     *     s := pop l + 1 chars from front of input
     *     append s to back of window
     * repeat
     */

    private static final int DEFAULT_BUFF_SIZE = 1024;
    protected int mBufferSize;
    protected Reader inputReader;
    protected PrintWriter outputWriter;
    protected StringBuffer searchBuffer;

    public LZ77() {
        this(DEFAULT_BUFF_SIZE);
    }

    public LZ77(int buffSize) {
        mBufferSize = buffSize;
        searchBuffer = new StringBuffer(mBufferSize);
    }

    public void encode(byte[] bytes, BitSetStream bitSetStream) {
        var searcher = new MatchSearcher();
        Window window = new Window();
        for (int inputByteIndex = 0; inputByteIndex < bytes.length; inputByteIndex++) {
            var inputByte = bytes[inputByteIndex];
            var outputByte = new byte[bytes.length - inputByteIndex];
            System.arraycopy(bytes, inputByteIndex, outputByte, 0, bytes.length - inputByteIndex);
            var match = searcher.findMatch(new StringBuffer(new String(outputByte)), window);
            if (match.isPresent()) {
                write(match.get(), bitSetStream);
            } else{
                write(new Match(0,0, (char)inputByte), bitSetStream);
            }
            window.add((char)inputByte);
            trimSearchBuffer();
        }
    }

    private void write(Match match, BitSetStream bitSetStream) {
        var coder = new UnaryCode();
        coder.encode(match.startIndex + 1, bitSetStream);
        coder.encode(match.length + 1, bitSetStream);
        coder.encode(match.nextCharInInput, bitSetStream);
    }

    public List<Character> decode(BitSetStream bitSetStream) {
        var list = new ArrayList<Character>();
        var coder = new UnaryCode();
        var window = new Window();
        while(bitSetStream.hasBitsLeft()) {
            var matchStart = coder.decode(bitSetStream) - 1;
            var matchLength = coder.decode(bitSetStream) - 1;
            var input = coder.decode(bitSetStream);
            if (matchLength == 0) {
                list.add((char)input);
                window.add((char)input);
            } else {
                for (int i = 0; i < matchLength; i++) {
                    list.add(window.charAt((int)(i + matchStart)));
                }
            }
        }
        return list;
    }



    private void trimSearchBuffer() {
        if (searchBuffer.length() > mBufferSize) {
            searchBuffer =
                    searchBuffer.delete(0,  searchBuffer.length() - mBufferSize);
        }
    }

}
