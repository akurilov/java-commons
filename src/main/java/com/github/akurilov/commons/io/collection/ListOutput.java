package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Output;

import java.util.List;

/**
 * Writable collection of the items. Not thread safe.
 */
public class ListOutput<T>
implements Output<T> {
	
	protected final List<T> items;
	
	public ListOutput(final List<T> items) {
		this.items = items;
	}

	/**
	 @param item the item to put
	 (due to capacity reasons for example)
	 */
	@Override
	public boolean put(final T item) {
		return items.add(item);
	}

	/**
	 Bulk put of the items from the specified buffer
	 @param buffer the buffer containing the items to put
	 @return the count of the items which have been written successfully
	 */
	@Override
	public int put(final List<T> buffer, final int from, final int to) {
		for(var i = from; i < to; i ++) {
			items.add(buffer.get(i));
		}
		return to - from;
	}

	
	@Override
	public final int put(final List<T> items) {
		return put(items, 0, items.size());
	}

	/**
	 @return the corresponding input
	 */
	@Override
	public ListInput<T> getInput() {
		return new ListInput<>(items);
	}

	/**
	 does nothing
	 */
	@Override
	public void close() {
	}

	
	@Override
	public String toString() {
		return "listOutput<" + items.hashCode() + ">";
	}
}
