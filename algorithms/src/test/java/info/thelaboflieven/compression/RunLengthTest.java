package info.thelaboflieven.compression;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RunLengthTest {

    @Test
    void encodeAndDecode() {
        var test = "abcdefghijklmnopqrstuvwxyz";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RunLength.encode(new ByteArrayInputStream(test.getBytes()), outputStream);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();

        RunLength.decode(new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8)), expectedOutput);
        assertEquals(test, expectedOutput.toString());
    }

    @Test
    void encodeAndDecodeEmpty() {
        var test = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RunLength.encode(new ByteArrayInputStream(test.getBytes()), outputStream);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();

        RunLength.decode(new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8)), expectedOutput);
        assertEquals(test, expectedOutput.toString());
    }

    @Test
    void encodeAndDecodeMultiples() {
        var test = "aaaaabbbaaaaabbb";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RunLength.encode(new ByteArrayInputStream(test.getBytes()), outputStream);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();

        RunLength.decode(new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8)), expectedOutput);
        assertEquals(test, expectedOutput.toString());
    }

    @Test
    void encodeEscape() {
        var test = "@";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RunLength.encode(new ByteArrayInputStream(test.getBytes()), outputStream);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();

        RunLength.decode(new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8)), expectedOutput);
        assertEquals(test, expectedOutput.toString());
    }

    @Test
    void encodeEscape2() {
        var test = "aaaa@bbb";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RunLength.encode(new ByteArrayInputStream(test.getBytes()), outputStream);
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();

        RunLength.decode(new ByteArrayInputStream(outputStream.toString().getBytes(StandardCharsets.UTF_8)), expectedOutput);
        assertEquals(test, expectedOutput.toString());
    }

}