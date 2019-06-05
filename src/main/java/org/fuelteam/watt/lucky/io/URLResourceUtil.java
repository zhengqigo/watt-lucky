package org.fuelteam.watt.lucky.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.fuelteam.watt.lucky.annotation.NotNull;

public class URLResourceUtil {

    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String URL_PROTOCOL_FILE = "file";

    public static File asFile(String generalPath) throws IOException {
        if (StringUtils.startsWith(generalPath, CLASSPATH_PREFIX)) {
            String resourceName = StringUtils.substringAfter(generalPath, CLASSPATH_PREFIX);
            return getFileByURL(ResourceUtil.asUrl(resourceName));
        }
        try {
            return getFileByURL(new URL(generalPath));
        } catch (MalformedURLException ex) {
            return new File(generalPath);
        }
    }

    public static InputStream asStream(String generalPath) throws IOException {
        if (StringUtils.startsWith(generalPath, CLASSPATH_PREFIX)) {
            String resourceName = StringUtils.substringAfter(generalPath, CLASSPATH_PREFIX);
            return ResourceUtil.asStream(resourceName);
        }
        try {
            return FileUtil.asInputStream(getFileByURL(new URL(generalPath)));
        } catch (MalformedURLException ex) {
            return FileUtil.asInputStream(generalPath);
        }
    }

    private static File getFileByURL(@NotNull URL fileUrl) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(fileUrl.getProtocol())) {
            String message = "URL cannot be resolved to absolute file path because it does not reside in the file system: %s";
            throw new FileNotFoundException(String.format(message, fileUrl));
        }
        try {
            return new File(toURI(fileUrl.toString()).getSchemeSpecificPart());
        } catch (URISyntaxException ex) { // NOSONAR
            return new File(fileUrl.getFile());
        }
    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }
}