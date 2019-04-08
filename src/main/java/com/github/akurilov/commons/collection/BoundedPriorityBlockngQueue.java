package com.github.akurilov.commons.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

public class BoundedPriorityBlockngQueue<E>
extends PriorityBlockingQueue<E>
implements BlockingQueue<E> {

	private final int capacity;
	private final LongAdder size = new LongAdder();

	public BoundedPriorityBlockngQueue(final int capacity, final Comparator<E> comparator) {
		super(capacity, comparator);
		this.capacity = capacity;
	}

	@Override
	public boolean offer(final E element) {
		if(size.sum() < capacity) {
			if(super.offer(element)) {
				size.increment();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int drainTo(final Collection<? super E> c, final int maxElements) {
		final var n = super.drainTo(c, maxElements);
		size.add(-n);
		return n;
	}
}
