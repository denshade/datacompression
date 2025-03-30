package info.thelaboflieven.compression;

import java.io.ByteArrayOutputStream;
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
                if (character == -1) {
                    if (repeatCount < 4) {
                        for (int i = 0; i < repeatCount; i++) {
                            outputStream.write(savedCharacter);
                        }
                    } else {
                        outputStream.write('@');
                        outputStream.write(repeatCount);
                        outputStream.write(savedCharacter);
                    }
                    return;
                }
                charCount ++;
                if (charCount == 1) {
                    savedCharacter = character;
                }  else if (savedCharacter == character) {
                    repeatCount++;
                }  else if (repeatCount < 4) {
                    for (int i = 0; i < repeatCount; i++) {
                        outputStream.write(savedCharacter);
                    }
                    repeatCount = 1;
                    savedCharacter = character;
                } else {
                    outputStream.write('@');
                    outputStream.write(repeatCount);
                    outputStream.write(savedCharacter);
                    repeatCount = 1;
                    savedCharacter = character;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        int character = 0;
        boolean compressionFlag = false;
        while(true) {
            try {
                character = inputStream.read();
                if (character == -1) {
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
}
