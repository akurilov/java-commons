package com.github.akurilov.commons.concurrent.throttle;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public final class BatchSubmTask
implements Runnable {
	private final int origin;
	private final SequentialWeightsThrottle wt;
	private final LongAdder[] resultCounters;
	public BatchSubmTask(final int origin, final SequentialWeightsThrottle wt, final LongAdder[] resultCounters) {
		this.origin = origin;
		this.wt = wt;
		this.resultCounters = resultCounters;
	}
	@Override
	public final void run() {
		int n;
		while(true) {
			n = wt.tryAcquire(origin, 128);
			if(n > 0) {
				resultCounters[origin].add(n);
			} else {
				LockSupport.parkNanos(1);
			}
		}
	}
}
