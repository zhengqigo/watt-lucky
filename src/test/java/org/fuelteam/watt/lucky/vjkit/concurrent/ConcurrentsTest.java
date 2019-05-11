package org.fuelteam.watt.lucky.vjkit.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import org.fuelteam.watt.lucky.concurrent.Concurrents;
import org.fuelteam.watt.lucky.concurrent.jsr166e.LongAdder;
import org.junit.Test;

public class ConcurrentsTest {

	@Test
	public void longAdder() {
		LongAdder counter = Concurrents.longAdder();
		counter.increment();
		counter.add(2);
		assertThat(counter.longValue()).isEqualTo(3L);
	}

}
