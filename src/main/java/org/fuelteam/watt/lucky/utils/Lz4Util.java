package org.fuelteam.watt.lucky.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class Lz4Util {

    public static byte[] compress(byte[] data) throws IOException {
        LZ4Compressor compressor = LZ4Factory.safeInstance().highCompressor();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LZ4BlockOutputStream compressed = new LZ4BlockOutputStream(baos, 2048, compressor);
        compressed.write(data);
        compressed.close();
        return baos.toByteArray();
    }

    public static void compressFile(String from, String to) throws IOException {
        byte[] bytes = Files.toByteArray(new File(from));
        byte[] compressed = compress(bytes);
        Files.write(compressed, new File(to));
    }

    public static byte[] decompress(byte[] data) throws IOException {
        LZ4FastDecompressor decompresser = LZ4Factory.fastestInstance().fastDecompressor();;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(data), decompresser);

        int count;
        byte[] buffer = new byte[2048];
        while ((count = lzis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        lzis.close();

        return baos.toByteArray();
    }

    public static void decompressFile(String from, String to) throws IOException {
        byte[] bytes = Files.toByteArray(new File(from));
        byte[] decompressed = decompress(bytes);
        Files.write(decompressed, new File(to));
    }
}