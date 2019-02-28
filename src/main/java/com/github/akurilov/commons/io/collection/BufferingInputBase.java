package com.github.akurilov.commons.io.collection;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

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

	private int loadMore() {
		final var lastItem = size > 0 ? items.get(size - 1) : null;
		i = 0;
		items.clear();
		try {
			return size = loadMoreItems(lastItem);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return 0;
	}

	/**
	 Called when the elements buffer is exhausted. Should put more (but not more than "capacity")
	 new elements into the empty buffer.
	 @return the count of the items was actually loaded into the "items" buffer or 0 if no more items are available.
	 */
	protected abstract int loadMoreItems(final T lastItem)
	throws IOException;

	@Override
	public final T get() {
		if(i == size) {
			if(loadMore() <= 0) {
				throwUnchecked(new EOFException());
			}
		}
		return items.get(i ++);
	}

	@Override
	public final int get(final List<T> buffer, final int maxCount) {
		var n = size - i;
		if(n == 0) {
			if(loadMore() <= 0) {
				throwUnchecked(new EOFException());
			}
		}
		n = Math.min(size - i, maxCount);
		for(var j = i; j < i + n; j ++) {
			buffer.add(items.get(j));
		}
		i += n;
		return n;
	}

	@Override
	public void close() {
		items.clear();
		super.close();
	}
}
