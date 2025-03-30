package info.thelaboflieven.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RunLength {

    public static void encode(InputStream inputStream, OutputStream outputStream) {
        byte charCount = 0;
        byte repeatCount = 1;
        int savedCharacter = 0;
        try {
            while(true) {
                int character = inputStream.read();
                if (isEndOfStream(character)) {
                    if (charCount > 0) {
                        if (repeatCount < 4) {
                            writeNonCompressedCharacters(repeatCount, outputStream, savedCharacter);
                        } else {
                            writeRepeatedCharacters(outputStream, repeatCount, savedCharacter);
                        }
                    }
                    return;
                }
                charCount ++;
                if (charCount == 1) {
                    savedCharacter = character;
                }  else if (savedCharacter == character) {
                    repeatCount++;
                }  else if (repeatCount < 4) {
                    writeNonCompressedCharacters(repeatCount, outputStream, savedCharacter);
                    repeatCount = 1;
                    savedCharacter = character;
                } else {
                    writeRepeatedCharacters(outputStream, repeatCount, savedCharacter);
                    repeatCount = 1;
                    savedCharacter = character;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeRepeatedCharacters(OutputStream outputStream, byte repeatCount, int savedCharacter) throws IOException {
        outputStream.write('@');
        outputStream.write(repeatCount);
        outputStream.write(savedCharacter);
    }

    private static void writeNonCompressedCharacters(byte repeatCount, OutputStream outputStream, int savedCharacter) throws IOException {
        for (int i = 0; i < repeatCount; i++) {
            outputStream.write(savedCharacter);
        }
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        int character = 0;
        boolean compressionFlag = false;
        while(true) {
            try {
                character = inputStream.read();
                if (isEndOfStream(character)) {
                    return;
                }
                if (!compressionFlag) {
                    if (character == '@') {
                        compressionFlag = true;
                    } else {
                        outputStream.write(character);
                    }
                } else {
                    int repeat = character;
                    character = inputStream.read();
                    for (int k = 0; k < repeat; k++) {
                        outputStream.write(character);
                    }
                    compressionFlag = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isEndOfStream(int character) {
        return character == -1;
    }
}
