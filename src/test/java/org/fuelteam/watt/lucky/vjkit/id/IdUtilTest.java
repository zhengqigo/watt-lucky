package org.fuelteam.watt.lucky.vjkit.id;

import java.util.UUID;

import org.fuelteam.watt.lucky.id.IdUtil;
import org.junit.Test;

public class IdUtilTest {

    @Test
    public void normal() {
        UUID id1 = IdUtil.fastUUID();
        UUID id2 = IdUtil.fastUUID();
        System.out.println("UUID1:" + id1);
        System.out.println("UUID2:" + id2);
    }
}