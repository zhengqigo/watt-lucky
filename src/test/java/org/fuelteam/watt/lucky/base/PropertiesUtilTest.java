package org.fuelteam.watt.lucky.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.fuelteam.watt.lucky.base.PropertiesUtil;
import org.junit.Test;

public class PropertiesUtilTest {

    @Test
    public void loadProperties() {
        Properties p1 = PropertiesUtil.loadFromFile("classpath:application.properties");
        assertThat(p1.get("lucky.min")).isEqualTo("1");
        assertThat(p1.get("lucky.max")).isEqualTo("10");

        Properties p2 = PropertiesUtil.loadFromString("lucky.min=1\nlucky.max=10\nisOpen=true");
        assertThat(PropertiesUtil.getInt(p2, "lucky.min", 0)).isEqualTo(1);
        assertThat(PropertiesUtil.getInt(p2, "lucky.max", 0)).isEqualTo(10);
        assertThat(PropertiesUtil.getInt(p2, "lucky.maxA", 0)).isEqualTo(0);

        assertThat(PropertiesUtil.getLong(p2, "lucky.min", 0L)).isEqualTo(1);
        assertThat(PropertiesUtil.getLong(p2, "lucky.max", 0L)).isEqualTo(10);
        assertThat(PropertiesUtil.getLong(p2, "lucky.maxA", 0L)).isEqualTo(0);

        assertThat(PropertiesUtil.getDouble(p2, "lucky.min", 0d)).isEqualTo(1);
        assertThat(PropertiesUtil.getDouble(p2, "lucky.max", 0d)).isEqualTo(10);
        assertThat(PropertiesUtil.getDouble(p2, "lucky.maxA", 0d)).isEqualTo(0);

        assertThat(PropertiesUtil.getString(p2, "lucky.min", "")).isEqualTo("1");
        assertThat(PropertiesUtil.getString(p2, "lucky.max", "")).isEqualTo("10");
        assertThat(PropertiesUtil.getString(p2, "lucky.maxA", "")).isEqualTo("");

        assertThat(PropertiesUtil.getBoolean(p2, "isOpen", false)).isTrue();
    }
}