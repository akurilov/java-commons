package com.github.akurilov.commons.io.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The items output which may be written infinitely.
 */
public class CircularListOutput<T>
extends ListOutput<T> {

	protected int capacity, i = 0;

	public CircularListOutput(final List<T> itemList, final int capacity)
	throws IllegalArgumentException {
		super(itemList);
		if(ArrayList.class.isInstance(itemList)) {
			ArrayList.class.cast(itemList).ensureCapacity(capacity);
		}
		if(capacity < 1) {
			throw new IllegalArgumentException("Capacity should be > 0");
		}
		this.capacity = capacity;
	}

	/**
	 @param item the item to put
	 @throws IOException if the destination collection fails to add the item
	 */
	@Override
	public boolean put(final T item)
	throws IOException {
		if(i >= capacity) {
			i = 0;
		}
		return super.put(item);
	}

	/**
	 Bulk circular put method
	 @param buffer the list of the items to put in a batch mode
	 @throws IOException if the destination collection fails to add the items
	 @return the size of the buffer to put
	 */
	@Override
	public int put(final List<T> buffer, final int from, final int to)
	throws IOException {
		//
		var n = to - from;
		if(buffer.size() > n) {
			return put(buffer.subList(from, to), 0, n);
		}
		//
		n = buffer.size();
		if(n < capacity) {
			// buffer may be placed entirely into the capacitor
			final var limit = capacity - items.size(); // how many free space is in the capacitor;
			if(n > limit) {
				// should remove some items from the beginning of the capacitor in order to place
				// the buffer entirely
				items.removeAll(items.subList(0, n - limit));
			}
			for(var j = from; j < to; j ++) {
				items.add(buffer.get(j));
			}
		} else {
			// only a tail part of the buffer may be placed into the capacitor
			items.clear(); // discard all the items in the capacitor
			for(final var item : buffer.subList(n - capacity, n)) {
				items.add(item);
			}
		}
		return n;
	}

	/**
	 @return the corresponding input
	 @throws IOException doesn't throw
	 */
	@Override
	public CircularListInput<T> getInput()
	throws IOException {
		return new CircularListInput<>(new ArrayList<>(items));
	}

	@Override
	public final String toString() {
		return "circularListOutput<" + items.hashCode() + ">";
	}
}
