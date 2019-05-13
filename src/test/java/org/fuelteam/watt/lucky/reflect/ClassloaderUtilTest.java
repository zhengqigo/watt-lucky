package org.fuelteam.watt.lucky.reflect;

import org.fuelteam.watt.lucky.reflect.ClassLoaderUtil;
import org.junit.Test;

public class ClassloaderUtilTest {

    @Test
    public void test() {
        ClassLoader loader = ClassLoaderUtil.getDefaultClassLoader();
        ClassLoaderUtil.isPresent("com.vip.vjtools.vjkit.reflect.ClassUtil", loader);
    }
}