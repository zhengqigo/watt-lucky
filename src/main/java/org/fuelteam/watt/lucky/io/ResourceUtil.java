package org.fuelteam.watt.lucky.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.fuelteam.watt.lucky.reflect.ClassLoaderUtil;
import org.fuelteam.watt.lucky.text.Charsets;
import org.fuelteam.watt.lucky.utils.ListUtil;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/**
 * ClassLoader: 不指定contextClass时, 优先使用Thread.getContextClassLoader(), 若ContextClassLoader未设置则使用Resources类的ClassLoader, 
 * 指定contextClass时则直接使用该contextClass的ClassLoader
 * <p>
 * 路径: 不指定contextClass时, 按URLClassLoader的实现从JarFile中查找resourceName, 故resourceName无需以"/"打头即表示JarFile根目录, 
 * 带"/"反而导致JarFile.getEntry(resouceName)时没有返回, 指定contextClass时, class.getResource()会先对name进行处理再交给classLoader, 
 * 打头的"/"的会被去除, 不以"/"打头则表示与该contextClass package的相对路径, 会先转为绝对路径
 * <p>
 * 同名资源: 如果有多个同名资源, 除非调用getResources()获取全部资源, 否则在URLClassLoader中按ClassPath顺序打开第一个命中的Jar文件
 */
public class ResourceUtil {

    public static URL asUrl(String resourceName) {
        return Resources.getResource(resourceName);
    }

    public static URL asUrl(Class<?> contextClass, String resourceName) {
        return Resources.getResource(contextClass, resourceName);
    }

    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    public static InputStream asStream(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }

    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    public static List<URL> getResourcesQuietly(String resourceName) {
        return getResourcesQuietly(resourceName, ClassLoaderUtil.getDefaultClassLoader());
    }

    public static List<URL> getResourcesQuietly(String resourceName, ClassLoader contextClassLoader) {
        try {
            Enumeration<URL> urls = contextClassLoader.getResources(resourceName);
            List<URL> list = Lists.newArrayListWithCapacity(10);
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
            return list;
        } catch (IOException e) {// NOSONAR
            return ListUtil.emptyList();
        }
    }
}