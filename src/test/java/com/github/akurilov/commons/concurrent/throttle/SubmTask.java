package com.github.akurilov.commons.concurrent.throttle;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public final class SubmTask
	implements Runnable {
	private final int origin;
	private final SequentialWeightsThrottle wt;
	private final LongAdder[] resultCounters;
	public SubmTask(final int origin, final SequentialWeightsThrottle wt, final LongAdder[] resultCounters) {
		this.origin = origin;
		this.wt = wt;
		this.resultCounters = resultCounters;
	}
	@Override
	public final void run() {
		while(true) {
			if(wt.tryAcquire(origin)) {
				resultCounters[origin].increment();
			} else {
				LockSupport.parkNanos(1);
			}
		}
	}
}
