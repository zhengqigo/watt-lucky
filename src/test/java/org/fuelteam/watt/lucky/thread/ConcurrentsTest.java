package org.fuelteam.watt.lucky.thread;

import static org.assertj.core.api.Assertions.assertThat;

import org.fuelteam.watt.lucky.jsr166e.LongAdder;
import org.fuelteam.watt.lucky.thread.Concurrents;
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