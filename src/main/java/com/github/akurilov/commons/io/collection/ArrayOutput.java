package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Output;

import java.util.List;

/**
 A Java objects array output. Not thread safe.
 */
public class ArrayOutput<T>
implements Output<T> {

	protected final T[] items;
	protected int i = 0;

	public ArrayOutput(final T[] items) {
		this.items = items;
	}

	/**
	 @param item the item to put
	 (due to capacity reasons for example)
	 */
	@Override
	public boolean put(final T item) {
		if(i < items.length) {
			items[i ++] = item;
			return true;
		} else {
			return false;
		}
	}

	/**
	 Bulk put of the items from the specified buffer
	 @param buffer the buffer containing the items to put
	 @return the count of the items which have been written successfully
	 */
	@Override
	public int put(final List<T> buffer, final int from, final int to) {
		final var n = Math.min(items.length - i, to - from);
		for(var j = 0; j < n; j ++) {
			items[i + j] = buffer.get(from + j);
		}
		return n;
	}


	@Override
	public final int put(final List<T> items) {
		return put(items, 0, items.size());
	}

	/**
	 @return the corresponding input
	 */
	@Override
	public ArrayInput<T> getInput() {
		return new ArrayInput<>(items);
	}

	/**
	 does nothing
	 */
	@Override
	public void close() {
	}


	@Override
	public String toString() {
		return "arrayOutput<" + items.hashCode() + ">";
	}
}
