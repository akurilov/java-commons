package com.github.akurilov.commons.collection;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 Created by andrey on 31.03.17.
 */
public final class OptLockArrayBuffer<T>
extends ArrayList<T>
implements OptLockBuffer<T> {

	private final Lock lock = new ReentrantLock();

	public OptLockArrayBuffer(final int capacity) {
		super(capacity);
	}

	@Override
	public final void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}

	@Override
	public final void lock() {
		lock.lock();
	}

	@Override
	public final void lockInterruptibly()
	throws InterruptedException {
		lock.lockInterruptibly();
	}

	@Override
	public final boolean tryLock() {
		return lock.tryLock();
	}

	@Override
	public final boolean tryLock(final long time, final TimeUnit unit)
	throws InterruptedException {
		return lock.tryLock(time, unit);
	}

	@Override
	public final void unlock() {
		lock.unlock();
	}

	@Override
	public final Condition newCondition() {
		return lock.newCondition();
	}
}
