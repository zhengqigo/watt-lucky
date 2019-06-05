package org.fuelteam.watt.lucky.id;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IdUtil {

    // 使用ThreadLocalRandm的UUID
    public static UUID fastUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong());
    }
}