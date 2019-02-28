package com.github.akurilov.commons.io.collection;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 * The blocking queue wrapped in order to act as output from the tail and as input from the head.
 */
public class LimitedQueueBuffer<T>
implements IoBuffer<T> {
	
	private T lastItem = null;
	protected final BlockingQueue<T> queue;
	
	public LimitedQueueBuffer(final BlockingQueue<T> queue) {
		this.queue = queue;
	}

	/**
	 Non-blocking put implementation
	 @param item the item to put
	 */
	@Override
	public boolean put(final T item) {
		return queue.offer(item);
	}

	/**
	 Non-blocking bulk put implementation
	 @param buffer the buffer containing the items to put
	 @return the count of the items been written, may return less count than specified if not enough
	 free capacity is in the queue
	 */
	@Override
	public int put(final List<T> buffer, final int from, final int to) {
		int i = from;
		while(i < to && queue.offer(buffer.get(i))) {
			i ++;
		}
		return i - from;
	}
	
	@Override
	public final int put(final List<T> items) {
		return put(items, 0, items.size());
	}

	/**
	 @return self
	 */
	@Override
	public LimitedQueueBuffer<T> getInput() {
		return this;
	}

	/**
	 Non-blocking get implementation
	 @return the item or null if the buffer is empty
	 */
	@Override
	public T get() {
		return queue.poll();
	}

	/**
	 Non-blocking bulk get implementation
	 @param maxCount the count limit
	 @param buffer buffer for the items
	 @throws UnsupportedOperationException
	 @throws IllegalArgumentException
	 @return the count of the items been get
	 */
	@Override
	public int get(final List<T> buffer, final int maxCount)
	throws UnsupportedOperationException, IllegalArgumentException {
		return queue.drainTo(buffer, maxCount);
	}
	
	@Override
	public long skip(final long itemsCount) {
		try {
			T item;
			long i = 0;
			for(; i < itemsCount; i++) {
				item = queue.take();
				if(item.equals(lastItem)) {
					break;
				}
			}
			return i;
		} catch (final InterruptedException e) {
			throwUnchecked(e);
			return 0;
		}
	}
	
	@Override
	public final boolean isEmpty() {
		return queue.isEmpty();
	}
	
	@Override
	public final int size() {
		return queue.size();
	}

	/**
	 Does nothing
	 */
	@Override
	public void reset() {
	}

	/**
	 Does nothing
	 */
	@Override
	public void close() {
		queue.clear();
	}
	
	@Override
	public String toString() {
		return "queueBuffer#" + hashCode() + "";
	}
}
