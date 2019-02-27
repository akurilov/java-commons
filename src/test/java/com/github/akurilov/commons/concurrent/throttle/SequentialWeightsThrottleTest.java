package com.github.akurilov.commons.concurrent.throttle;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;

/**
 Created by andrey on 06.11.16.
 */

public class SequentialWeightsThrottleTest {

	private static final int WRITE = 0;
	private static final int READ = 1;

	private final int[] weights = new int[] {
		80,
		20
	};
	private final LongAdder[] resultCounters = new LongAdder[] {
		new LongAdder(),
		new LongAdder()
	};

	private final SequentialWeightsThrottle wt = new SequentialWeightsThrottle(weights);

	@Test
	public void testRequestApprovalFor()
	throws Exception {
		final ExecutorService es = Executors.newFixedThreadPool(2);
		es.submit(new SubmTask(WRITE, wt, resultCounters));
		es.submit(new SubmTask(READ, wt, resultCounters));
		es.awaitTermination(10, TimeUnit.SECONDS);
		es.shutdownNow();
		final double writes = resultCounters[WRITE].sum();
		final long reads = resultCounters[READ].sum();
		assertEquals(80/20, writes / reads, 0.01);
		System.out.println("Write rate: " + writes / 10 + " Hz, read rate: " + reads / 10 + " Hz");
	}

	@Test
	public void testRequestBatchApprovalFor()
	throws Exception {
		final ExecutorService es = Executors.newFixedThreadPool(2);
		es.submit(new BatchSubmTask(WRITE, wt, resultCounters));
		es.submit(new BatchSubmTask(READ, wt, resultCounters));
		es.awaitTermination(10, TimeUnit.SECONDS);
		es.shutdownNow();
		final double writes = resultCounters[WRITE].sum();
		final long reads = resultCounters[READ].sum();
		assertEquals(80/20, writes / reads, 0.01);
		System.out.println("Write rate: " + writes / 10 + " Hz, read rate: " + reads / 10 + " Hz");
	}
}
