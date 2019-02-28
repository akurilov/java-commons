package com.github.akurilov.commons.io.collection;

import java.util.List;

/**
 An array input which may use fixed count of the items for unlimited (circular) retrieving.
 */
public class CircularArrayInput<T>
extends ArrayInput<T> {

	public CircularArrayInput(final T[] items) {
		super(items);
	}

	/**
	 @return next item
	 */
	@Override
	public T get() {
		if(i >= size) {
			reset();
		}
		return items[i ++];
	}

	/**
	 @param buffer buffer for the items
	 @param maxCount the count limit
	 @return the actual count of the items got in the buffer
	 */
	@Override
	public int get(final List<T> buffer, final int maxCount) {
		int n = 0;
		while(n < maxCount) {
			if(i >= size) {
				reset();
			}
			n += super.get(buffer, Math.min(size - i, maxCount - n));
		}
		return n;
	}

	@Override
	public String toString() {
		return "circularArrayInput<" + items.hashCode() + ">";
	}
}
