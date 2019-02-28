package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Input;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 * Readable collection of the items. Not thread safe.
 */
public class ListInput<T>
implements Input<T> {
	
	protected final List<T> items;
	protected int size;
	protected int i = 0;
	
	public ListInput(final List<T> items) {
		this.items = items;
		this.size = items == null ? 0 : items.size();
	}

	/**
	 @return next item
	 @throws EOFException if there's nothing to get more
	 */
	@Override
	public T get() {
		if(i < size) {
			return items.get(i++);
		} else {
			throwUnchecked(new EOFException());
		}
		return null;
	}

	/**
	 Bulk get into the specified buffer
	 @param buffer buffer for the items
	 @param maxCount the count limit
	 @return the count of the items been get
	 @throws EOFException if there's nothing to get more
	 */
	@Override
	public int get(final List<T> buffer, final int maxCount) {
		int n = size - i;
		if(n > 0) {
			n = Math.min(n, maxCount);
			for(final T item : items.subList(i, i + n)) {
				buffer.add(item);
			}
		} else {
			throwUnchecked(new EOFException());
		}
		i += n;
		return n;
	}

	@Override
	public void reset() {
		i = 0;
	}

	@Override
	public long skip(final long itemsCount) {
		final int remainingCount = size - i;
		if(itemsCount > remainingCount) {
			i = 0;
			return remainingCount;
		} else {
			i += (int) itemsCount;
			return itemsCount;
		}
	}

	/**
	 Does nothing
	 */
	@Override
	public void close() {
	}

	@Override
	public String toString() {
		return "listInput<" + items.hashCode() + ">";
	}
}
