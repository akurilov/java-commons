package com.github.akurilov.commons.collection;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * The use case for the <i>optionally locking buffer</i> is to share the buffer of the items between the threads in the
 * non-blocking manner (using a {@link java.util.concurrent.locks.Lock#tryLock} method which allows to try to get a lock
 * w/o waiting).
 */
public interface OptLockBuffer<T>
extends List<T>, Lock {

	/**
	 * Remove the elements in the specified range
	 * @see java.util.ArrayList#removeRange(int, int)
	 */
	void removeRange(int fromIndex, int toIndex);
}
