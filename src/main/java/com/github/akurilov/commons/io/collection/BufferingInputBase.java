package com.github.akurilov.commons.io.collection;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract input which is designed to load more items when it's needed to get more items.
 * <b>Not thread safe</b>.
 */
public abstract class BufferingInputBase<T>
extends ListInput<T> {

	protected final int capacity;

	public BufferingInputBase(final int capacity) {
		super(new ArrayList<>(capacity));
		this.capacity = capacity;
	}

	private int loadMore()
	throws IOException {
		final T lastItem = size > 0 ? items.get(size - 1) : null;
		i = 0;
		items.clear();
		return size = loadMoreItems(lastItem);
	}

	/**
	 Called when the elements buffer is exhausted. Should put more (but not more than "capacity")
	 new elements into the empty buffer.
	 @return the count of the items was actually loaded into the "items" buffer or 0 if no more items are available.
	 */
	protected abstract int loadMoreItems(final T lastItem)
	throws IOException;

	@Override
	public final T get()
	throws IOException {
		if(i == size) {
			if(loadMore() <= 0) {
				throw new EOFException();
			}
		}
		return items.get(i ++);
	}

	@Override
	public final int get(final List<T> buffer, final int maxCount)
	throws IOException {
		int n = size - i;
		if(n == 0) {
			if(loadMore() <= 0) {
				throw new EOFException();
			}
		}
		n = Math.min(size - i, maxCount);
		for(int j = i; j < i + n; j ++) {
			buffer.add(items.get(j));
		}
		i += n;
		return n;
	}

	@Override
	public void close()
	throws IOException {
		super.close();
		items.clear();
	}
}
