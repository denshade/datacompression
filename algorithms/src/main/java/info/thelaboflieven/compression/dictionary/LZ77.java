package info.thelaboflieven.compression.dictionary;

import info.thelaboflieven.compression.BitSetStream;

import java.io.*;

public class LZ77 {

    public record Match(int startIndex, int length, char nextCharInInput) {
    }

    public record Window(char[] array, int currentLocation) {
        public char currentChar() {
            return array[currentLocation];
        }
    }

    public static class MatchSearcher {
        public Match findMatch(StringBuffer buffer, Window window) {
            return new Match(0,0, window.currentChar());
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

    public static final int DEFAULT_BUFF_SIZE = 1024;
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
        StringBuilder currentMatch = new StringBuilder();
        int tempIndex = 0;
        byte d;
        byte l;
        byte c;
        for (int inputByteIndex = 0; inputByteIndex < bytes.length; inputByteIndex++) {
            var inputByte = bytes[inputByteIndex];
            tempIndex = searchBuffer.indexOf(currentMatch.toString() + (char)inputByte);
            if (tempIndex > -1) {
                d = (byte)tempIndex;
                l = (byte)currentMatch.length();
                c = inputByte;
                currentMatch.append((char) c);
            } else{
                d = 0;
                l = 0;
                c = inputByte;
                currentMatch = new StringBuilder();
            }
            bitSetStream.writeByte(d);
            bitSetStream.writeByte(l);
            bitSetStream.writeByte(c);
            inputByteIndex += l;
            searchBuffer.append((char)inputByte);
            trimSearchBuffer();
        }
    }

    public void encode(Reader inputReader, PrintWriter outputWriter) throws IOException {

        int nextChar;
        String currentMatch = "";
        int matchIndex = 0, tempIndex = 0;

        // while there are more characters - read a character
        while ((nextChar = inputReader.read()) != -1) {
            // look in our search buffer for a match
            tempIndex = searchBuffer.indexOf(currentMatch + (char)nextChar);
            // if match then append nextChar to currentMatch
            // and update index of match
            if (tempIndex != -1) {
                currentMatch += (char)nextChar;
                matchIndex = tempIndex;
            } else {
                // found longest match, now should we encode it?
                String codedString =
                        "~"+matchIndex+"~"+currentMatch.length()+"~"+(char)nextChar;
                String concat = currentMatch + (char)nextChar;
                // is coded string shorter than raw text?
                if (codedString.length() <= concat.length()) {
                    outputWriter.print(codedString);
                    searchBuffer.append(concat); // append to the search buffer
                    currentMatch = "";
                    matchIndex = 0;
                } else {
                    // otherwise, output chars one at a time from
                    // currentMatch until we find a new match or
                    // run out of chars
                    currentMatch = concat; matchIndex = -1;
                    while (currentMatch.length() > 1 && matchIndex == -1) {
                        outputWriter.print(currentMatch.charAt(0));
                        searchBuffer.append(currentMatch.charAt(0));
                        currentMatch = currentMatch.substring(1, currentMatch.length());
                        matchIndex = searchBuffer.indexOf(currentMatch);
                    }
                }
                // Adjust search buffer size if necessary
                trimSearchBuffer();
            }
        }
        // flush any match we may have had when EOF encountered
        if (matchIndex != -1) {
            String codedString =
                    "~"+matchIndex+"~"+currentMatch.length();
            if (codedString.length() <= currentMatch.length()) {
                outputWriter.print("~"+matchIndex+"~"+currentMatch.length());
            } else {
                outputWriter.print(currentMatch);
            }
        }
        // close files
        inputReader.close();
        outputWriter.flush(); outputWriter.close();
    }

    public void decode(Reader inputReader, PrintWriter writer) throws IOException {
        StreamTokenizer st = new StreamTokenizer(inputReader);

        st.ordinaryChar((int)' ');
        st.ordinaryChar((int)'.');
        st.ordinaryChar((int)'-');
        st.ordinaryChar((int)'\n');
        st.wordChars((int)'\n', (int)'\n');
        st.wordChars((int)' ', (int)'}');

        int offset, length;
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            switch (st.ttype) {
                case StreamTokenizer.TT_WORD -> {
                    searchBuffer.append(st.sval);
                    System.out.print(st.sval);
                    // Adjust search buffer size if necessary
                    trimSearchBuffer();
                }
                case StreamTokenizer.TT_NUMBER -> {
                    offset = (int) st.nval; // set the offset
                    st.nextToken(); // get the separator (hopefully)
                    if (st.ttype == StreamTokenizer.TT_WORD) {
                        // we got a word instead of the separator,
                        // therefore the first number read was actually part of a word
                        searchBuffer.append(offset + st.sval);
                        System.out.print(offset + st.sval);
                        break; // break out of the switch
                    }
                    // if we got this far then we must be reading a
                    // substitution pointer
                    st.nextToken(); // get the length
                    length = (int) st.nval;
                    // output substring from search buffer
                    String output = searchBuffer.substring(offset, offset + length);
                    writer.print(output);
                    System.out.print(output);
                    searchBuffer.append(output);
                    // Adjust search buffer size if necessary
                    trimSearchBuffer();
                }
                default -> {
                }
                // consume a '~'
            }
        }
        inputReader.close();
    }
    private void trimSearchBuffer() {
        if (searchBuffer.length() > mBufferSize) {
            searchBuffer =
                    searchBuffer.delete(0,  searchBuffer.length() - mBufferSize);
        }
    }

}
