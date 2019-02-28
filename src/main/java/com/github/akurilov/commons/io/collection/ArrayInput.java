package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Input;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 Java objects array based input. Not thread safe.
 */
public class ArrayInput<T>
implements Input<T> {

	protected final T[] items;
	protected int size;
	protected int i = 0;

	public ArrayInput(final T[] items) {
		this.items = items;
		this.size = items.length;
	}

	/**
	 @return next item
	 @throws EOFException if there's nothing to get more
	 */
	@Override
	public T get() {
		if(i < size) {
			return items[i ++];
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
		var n = size - i;
		if(n > 0) {
			n = Math.min(n, maxCount);
			for(var j = i; j < i + n; j ++) {
				buffer.add(items[j]);
			}
		} else {
			throwUnchecked(new EOFException());
		}
		i += n;
		return n;
	}

	@Override
	public long skip(final long itemsCount) {
		final var remainingCount = size - i;
		if(itemsCount > remainingCount) {
			i = 0;
			return remainingCount;
		} else {
			i += (int) itemsCount;
			return itemsCount;
		}
	}

	/**
	 @throws IOException doesn't throw
	 */
	@Override
	public void reset() {
		i = 0;
	}

	/**
	 Does nothing
	 @throws IOException doesn't throw
	 */
	@Override
	public void close() {
	}

	@Override
	public String toString() {
		return "arrayInput<" + items.hashCode() + ">";
	}
}
