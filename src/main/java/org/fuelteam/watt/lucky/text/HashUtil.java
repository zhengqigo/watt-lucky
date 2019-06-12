package org.fuelteam.watt.lucky.text;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.CRC32;

import org.fuelteam.watt.lucky.annotation.NotNull;
import org.fuelteam.watt.lucky.annotation.Nullable;

import com.google.common.hash.Hashing;

public class HashUtil {

    public static final int MURMUR_SEED = 1_318_007_700;

    private static final ThreadLocal<MessageDigest> MD5_DIGEST = createThreadLocalMessageDigest("MD5");
    private static final ThreadLocal<MessageDigest> SHA_1_DIGEST = createThreadLocalMessageDigest("SHA-1");

    private static SecureRandom random = new SecureRandom();

    // ThreadLocal重用MessageDigest
    private static ThreadLocal<MessageDigest> createThreadLocalMessageDigest(final String digest) {
        return new ThreadLocal<MessageDigest>() {
            @Override
            protected MessageDigest initialValue() {
                try {
                    return MessageDigest.getInstance(digest);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("unexpected exception creating MessageDigest instance for [" + digest + ']', e);
                }
            }
        };
    }

    public static byte[] sha1(@NotNull byte[] input) {
        return digest(input, get(SHA_1_DIGEST), null, 1);
    }

    public static byte[] sha1(@NotNull String input) {
        return digest(input.getBytes(Charsets.UTF_8), get(SHA_1_DIGEST), null, 1);
    }

    public static byte[] sha1(@NotNull byte[] input, @Nullable byte[] salt) {
        return digest(input, get(SHA_1_DIGEST), salt, 1);
    }

    public static byte[] sha1(@NotNull String input, @Nullable byte[] salt) {
        return digest(input.getBytes(Charsets.UTF_8), get(SHA_1_DIGEST), salt, 1);
    }

    public static byte[] sha1(@NotNull byte[] input, @Nullable byte[] salt, int iterations) {
        return digest(input, get(SHA_1_DIGEST), salt, iterations);
    }

    public static byte[] sha1(@NotNull String input, @Nullable byte[] salt, int iterations) {
        return digest(input.getBytes(Charsets.UTF_8), get(SHA_1_DIGEST), salt, iterations);
    }

    private static MessageDigest get(ThreadLocal<MessageDigest> messageDigest) {
        MessageDigest instance = messageDigest.get();
        instance.reset();
        return instance;
    }

    private static byte[] digest(@NotNull byte[] input, MessageDigest digest, byte[] salt, int iterations) {
        if (salt != null) digest.update(salt);
        // 第一次散列
        byte[] result = digest.digest(input);
        // 如果迭代次数>1，进一步迭代散列
        for (int i = 1; i < iterations; i++) {
            digest.reset();
            result = digest.digest(result);
        }
        return result;
    }

    public static byte[] generateSalt(int numBytes) {
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] sha1File(InputStream input) throws IOException {
        return digestFile(input, get(SHA_1_DIGEST));
    }

    public static byte[] md5File(InputStream input) throws IOException {
        return digestFile(input, get(MD5_DIGEST));
    }

    private static byte[] digestFile(InputStream input, MessageDigest messageDigest) throws IOException {
        int bufferLength = 8 * 1024;
        byte[] buffer = new byte[bufferLength];
        int read = input.read(buffer, 0, bufferLength);
        while (read > -1) {
            messageDigest.update(buffer, 0, read);
            read = input.read(buffer, 0, bufferLength);
        }
        return messageDigest.digest();
    }

    public static int crc32AsInt(@NotNull String input) {
        return crc32AsInt(input.getBytes(Charsets.UTF_8));
    }

    public static int crc32AsInt(@NotNull byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return (int) crc32.getValue();
    }

    public static long crc32AsLong(@NotNull String input) {
        return crc32AsLong(input.getBytes(Charsets.UTF_8));
    }

    public static long crc32AsLong(@NotNull byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return crc32.getValue();
    }

    public static int murmur32AsInt(@NotNull byte[] input) {
        return Hashing.murmur3_32(MURMUR_SEED).hashBytes(input).asInt();
    }

    public static int murmur32AsInt(@NotNull String input) {
        return Hashing.murmur3_32(MURMUR_SEED).hashString(input, Charsets.UTF_8).asInt();
    }

    public static long murmur128AsLong(@NotNull byte[] input) {
        return Hashing.murmur3_128(MURMUR_SEED).hashBytes(input).asLong();
    }

    public static long murmur128AsLong(@NotNull String input) {
        return Hashing.murmur3_128(MURMUR_SEED).hashString(input, Charsets.UTF_8).asLong();
    }
}