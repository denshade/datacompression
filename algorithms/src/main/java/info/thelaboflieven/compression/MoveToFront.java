package info.thelaboflieven.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MoveToFront {

    public static void encode(InputStream inputStream, OutputStream outputStream){
        List<Byte> history = new ArrayList<>();
        while(true) {
            try {
                int character = inputStream.read();
                if (history.contains(character)) {

                }
                //outputStream.write();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
