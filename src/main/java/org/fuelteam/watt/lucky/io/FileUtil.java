package org.fuelteam.watt.lucky.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.fuelteam.watt.lucky.annotation.NotNull;
import org.fuelteam.watt.lucky.annotation.Nullable;
import org.fuelteam.watt.lucky.text.Charsets;
import org.fuelteam.watt.lucky.utils.Platforms;

public class FileUtil {

    private static FileVisitor<Path> deleteFileVisitor = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    public static byte[] toByteArray(final File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static String toString(final File file) throws IOException {
        return com.google.common.io.Files.asCharSource(file, Charsets.UTF_8).toString();
    }

    public static List<String> toLines(final File file) throws IOException {
        return Files.readAllLines(file.toPath(), Charsets.UTF_8);
    }

    public static void write(@NotNull final CharSequence data, @NotNull final File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8)) {
            writer.append(data);
        }
    }

    public static void append(@NotNull final CharSequence data, @NotNull final File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.append(data);
        }
    }

    public static InputStream asInputStream(String fileName) throws IOException {
        return asInputStream(getPath(fileName));
    }

    public static InputStream asInputStream(@NotNull File file) throws IOException {
        return asInputStream(file.toPath());
    }

    public static InputStream asInputStream(@NotNull Path path) throws IOException {
        return Files.newInputStream(path);
    }

    public static OutputStream asOututStream(String fileName) throws IOException {
        return asOututStream(getPath(fileName));
    }

    public static OutputStream asOututStream(@NotNull File file) throws IOException {
        return asOututStream(file.toPath());
    }

    public static OutputStream asOututStream(@NotNull Path path) throws IOException {
        return Files.newOutputStream(path);
    }

    public static BufferedReader asBufferedReader(String fileName) throws IOException {
        return asBufferedReader(getPath(fileName));
    }

    public static BufferedReader asBufferedReader(@NotNull Path path) throws IOException {
        return Files.newBufferedReader(path, Charsets.UTF_8);
    }

    public static BufferedWriter asBufferedWriter(String fileName) throws IOException {
        return Files.newBufferedWriter(getPath(fileName), Charsets.UTF_8);
    }

    public static BufferedWriter asBufferedWriter(@NotNull Path path) throws IOException {
        return Files.newBufferedWriter(path, Charsets.UTF_8);
    }

    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        copy(from.toPath(), to.toPath());
    }

    public static void copy(@NotNull Path from, @NotNull Path to) throws IOException {
        if (Files.isDirectory(from)) copyDir(from, to);
        if (!Files.isDirectory(from)) copyFile(from, to);
    }

    public static void copyFile(@NotNull File from, @NotNull File to) throws IOException {
        copyFile(from.toPath(), to.toPath());
    }

    public static void copyFile(@NotNull Path from, @NotNull Path to) throws IOException {
        if (!Files.exists(from)) return;
        if (FileUtil.isDirExists(to)) return;
        Files.copy(from, to);
    }

    public static void copyDir(@NotNull File from, @NotNull File to) throws IOException {
        if (!isDirExists(from)) return;
        copyDir(from.toPath(), to.toPath());
    }

    public static void copyDir(@NotNull Path from, @NotNull Path to) throws IOException {
        if (!isDirExists(from)) return;
        makesureDirExists(to);
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(from)) {
            for (Path path : dirStream) {
                copy(path, to.resolve(path.getFileName()));
            }
        }
    }

    public static void moveFile(@NotNull File from, @NotNull File to) throws IOException {
        moveFile(from.toPath(), to.toPath());
    }

    public static void moveFile(@NotNull Path from, @NotNull Path to) throws IOException {
        if (!isFileExists(from)) return;
        if (isDirExists(to)) return;
        Files.move(from, to);
    }

    public static void moveDir(@NotNull File from, @NotNull File to) throws IOException {
        if (!isDirExists(from)) return;
        if (isFileExists(to)) return;
        final boolean rename = from.renameTo(to);
        if (rename) return;
        if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
            String message = "Cannot move directory: %s to a subdirectory of itself: %s";
            throw new IOException(String.format(message, from, to));
        }
        copyDir(from, to);
        deleteDir(from);
        if (from.exists()) {
            String message = "Failed to delete original directory %s after copy to %s\\";
            throw new IOException(String.format(message, from, to));
        }
    }

    public static void touch(String filePath) throws IOException {
        touch(new File(filePath));
    }

    public static void touch(File file) throws IOException {
        com.google.common.io.Files.touch(file);
    }

    public static void deleteFile(@Nullable File file) throws IOException {
        if (!isFileExists(file)) return;
        deleteFile(file.toPath());
    }

    public static void deleteFile(@Nullable Path path) throws IOException {
        if (!isFileExists(path)) return;
        Files.delete(path);
    }

    public static void deleteDir(Path dir) throws IOException {
        if (!isDirExists(dir)) return;
        Files.walkFileTree(dir, deleteFileVisitor);
    }

    public static void deleteDir(File dir) throws IOException {
        if (!isDirExists(dir)) return;
        deleteDir(dir.toPath());
    }

    public static boolean isDirExists(String dirPath) {
        if (dirPath == null) return false;
        return isDirExists(getPath(dirPath));
    }

    public static boolean isDirExists(Path dirPath) {
        return dirPath != null && Files.exists(dirPath) && Files.isDirectory(dirPath);
    }

    public static boolean isDirExists(File dir) {
        if (dir == null) return false;
        return isDirExists(dir.toPath());
    }

    public static void makesureDirExists(String dirPath) throws IOException {
        makesureDirExists(getPath(dirPath));
    }

    public static void makesureDirExists(@NotNull File file) throws IOException {
        makesureDirExists(file.toPath());
    }

    public static void makesureDirExists(@NotNull Path dirPath) throws IOException {
        Files.createDirectories(dirPath);
    }

    public static void makesureParentDirExists(@NotNull File file) throws IOException {
        makesureDirExists(file.getParentFile());
    }

    public static boolean isFileExists(String fileName) {
        if (fileName == null) return false;
        return isFileExists(getPath(fileName));
    }

    public static boolean isFileExists(File file) {
        if (file == null) return false;
        return isFileExists(file.toPath());
    }

    public static boolean isFileExists(Path path) {
        if (path == null) return false;
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public static Path createTempDir() throws IOException {
        return Files.createTempDirectory(System.currentTimeMillis() + "-");
    }

    public static Path createTempFile() throws IOException {
        return Files.createTempFile("tmp-", ".tmp");
    }

    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }

    private static Path getPath(String filePath) {
        return Paths.get(filePath);
    }

    public static String getFileName(@NotNull String fullName) {
        Validate.notEmpty(fullName);
        int last = fullName.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        return fullName.substring(last + 1);
    }

    public static String getFileExtension(File file) {
        return com.google.common.io.Files.getFileExtension(file.getName());
    }

    public static String getFileExtension(String fullName) {
        return com.google.common.io.Files.getFileExtension(fullName);
    }
}