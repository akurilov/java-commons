package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Output;

import java.io.IOException;
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
	 @throws IOException if the destination collection fails to add the item
	 (due to capacity reasons for example)
	 */
	@Override
	public boolean put(final T item)
	throws IOException {
		return items.add(item);
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
		for(var i = from; i < to; i ++) {
			items.add(buffer.get(i));
		}
		return to - from;
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
	public ListInput<T> getInput()
	throws IOException {
		return new ListInput<>(items);
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
		return "listOutput<" + items.hashCode() + ">";
	}
}
