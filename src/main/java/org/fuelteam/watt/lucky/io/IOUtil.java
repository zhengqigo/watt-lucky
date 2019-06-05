package org.fuelteam.watt.lucky.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.fuelteam.watt.lucky.text.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

public class IOUtil {

    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ioe) {
            logger.warn("IOException thrown while closing Closeable", ioe);
        }
    }

    public static String toString(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, Charsets.UTF_8);
        return toString(reader);
    }

    public static String toString(Reader input) throws IOException {
        return CharStreams.toString(input);
    }

    public static List<String> toLines(final InputStream input) throws IOException {
        return CharStreams.readLines(new BufferedReader(new InputStreamReader(input, Charsets.UTF_8)));
    }

    public static List<String> toLines(final Reader input) throws IOException {
        return CharStreams.readLines(toBufferedReader(input));
    }

    public static String readLine(final InputStream input) throws IOException {
        return new BufferedReader(new InputStreamReader(input, Charsets.UTF_8)).readLine();
    }

    public static String readLine(final Reader reader) throws IOException {
        return toBufferedReader(reader).readLine();
    }

    public static void write(final String data, final OutputStream output) throws IOException {
        if (data != null) output.write(data.getBytes(Charsets.UTF_8));
    }

    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) output.write(data);
    }

    public static InputStream toInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes(Charsets.UTF_8));
    }

    public static Reader toInputStreamReader(String input) {
        return new InputStreamReader(toInputStream(input), Charsets.UTF_8);
    }

    public static long copy(final Reader input, final Writer output) throws IOException {
        return CharStreams.copy(input, output);
    }

    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        return ByteStreams.copy(input, output);
    }

    public static BufferedReader toBufferedReader(final Reader reader) {
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
    }
}