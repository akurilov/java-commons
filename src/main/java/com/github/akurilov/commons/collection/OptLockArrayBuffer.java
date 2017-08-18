package com.github.akurilov.commons.collection;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The <i>optionally locked buffer</i> based on the {@link java.util.ArrayList}
 */
public final class OptLockArrayBuffer<T>
extends ArrayList<T>
implements OptLockBuffer<T> {

	private final Lock lock = new ReentrantLock();

	public OptLockArrayBuffer(final int capacity) {
		super(capacity);
	}

	/**
	 * Publishes the {@link java.util.ArrayList#removeRange(int, int)} method
	 * @see java.util.ArrayList#removeRange(int, int)
	 */
	@Override
	public final void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}

	/**
	 * @see Lock#lock()
	 */
	@Override
	public final void lock() {
		lock.lock();
	}

	/**
	 * @see Lock#lockInterruptibly()
	 */
	@Override
	public final void lockInterruptibly()
	throws InterruptedException {
		lock.lockInterruptibly();
	}

	/**
	 * @see Lock#tryLock()
	 */
	@Override
	public final boolean tryLock() {
		return lock.tryLock();
	}

	/**
	 * @see Lock#tryLock(long, TimeUnit)
	 */
	@Override
	public final boolean tryLock(final long time, final TimeUnit unit)
	throws InterruptedException {
		return lock.tryLock(time, unit);
	}

	/**
	 * @see Lock#unlock()
	 */
	@Override
	public final void unlock() {
		lock.unlock();
	}

	/**
	 * @see Lock#newCondition()
	 */
	@Override
	public final Condition newCondition() {
		return lock.newCondition();
	}
}
