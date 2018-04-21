package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Output;

import java.io.IOException;
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
	 @throws IOException if the destination collection fails to add the item
	 (due to capacity reasons for example)
	 */
	@Override
	public boolean put(final T item)
	throws IOException {
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
	 @throws IOException doesn't throw
	 */
	@Override
	public int put(final List<T> buffer, final int from, final int to)
	throws IOException {
		final int n = Math.min(items.length - i, to - from);
		for(int j = 0; j < n; j ++) {
			items[i + j] = buffer.get(from + j);
		}
		return n;
	}


	@Override
	public final int put(final List<T> items)
	throws IOException {
		return put(items, 0, items.size());
	}

	/**
	 @return the corresponding input
	 @throws IOException doesn't throw
	 */
	@Override
	public ArrayInput<T> getInput()
	throws IOException {
		return new ArrayInput<>(items);
	}

	/**
	 does nothing
	 @throws IOException doesn't throw
	 */
	@Override
	public void close()
	throws IOException {
	}


	@Override
	public String toString() {
		return "arrayOutput<" + items.hashCode() + ">";
	}
}
