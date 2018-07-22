package com.github.akurilov.commons.collection;

import java.nio.BufferUnderflowException;
import java.nio.InvalidMarkException;

public final class ObjectBufferImpl<T extends Comparable<T>>
implements ObjectBuffer<T> {

	private final T[] buff;

	private int mark = -1;
	private int position = 0;
	private int limit;
	private int capacity;

	@SuppressWarnings("unchecked")
	ObjectBufferImpl(final int capacity)
	throws IllegalArgumentException {
		if(capacity < 0) {
			throw new IllegalArgumentException("Negative capacity: " + capacity);
		}
		this.capacity = capacity;
		buff = (T[]) new Object[capacity];
		limit = capacity;
	}

	final int nextGetIndex() {
		if (position >= limit)
			throw new BufferUnderflowException();
		return position++;
	}

	@Override
	public final T get() {
		return buff[nextGetIndex()];
	}

	@Override
	public final T get(final int i) {
		return buff[checkIndex(i)];
	}

	final int checkIndex(int i) {
		if((i < 0) || (i >= limit)) {
			throw new IndexOutOfBoundsException();
		}
		return i;
	}

	@Override
	public final ObjectBufferImpl<T> get(final T[] dst) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> get(final T[] dst, final int offset, final int length) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> put(final T val) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> put(final T[] vals) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> put(final byte[] src, final int offset, final int length) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> put(final ObjectBuffer<T> src) {
		return null;
	}

	@Override
	public final ObjectBufferImpl<T> put(final int index, final T val) {
		return null;
	}

	@Override
	public final T[] array() {
		return buff;
	}

	@Override
	public final int capacity() {
		return capacity;
	}

	@Override
	public final ObjectBufferImpl<T> clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public final ObjectBufferImpl<T> flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public final boolean hasRemaining() {
		return position < limit;
	}

	@Override
	public final int limit() {
		return limit;
	}

	@Override
	public final ObjectBuffer<T> limit(final int newLimit) {
		if((newLimit > capacity) || (newLimit < 0)) {
			throw new IllegalArgumentException();
		}
		this.limit = newLimit;
		if(position > newLimit) {
			position = newLimit;
		}
		if(mark > newLimit) {
			mark = -1;
		}
		return this;
	}

	@Override
	public final ObjectBufferImpl<T> mark() {
		mark = position;
		return this;
	}

	@Override
	public final int position() {
		return position;
	}

	@Override
	public final ObjectBufferImpl<T> position(final int newPosition) {
		if((newPosition > limit) || (newPosition < 0)) {
			throw new IllegalArgumentException();
		}
		position = newPosition;
		if(mark > position) {
			mark = -1;
		}
		return this;
	}

	@Override
	public final int remaining() {
		return limit - position;
	}

	@Override
	public final ObjectBufferImpl<T> reset() {
		if(mark < 0) {
			throw new InvalidMarkException();
		}
		position = mark;
		return this;
	}

	@Override
	public final ObjectBufferImpl<T> rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public final int compareTo(final ObjectBuffer<T> o) {
		final int n = this.position() + Math.min(this.remaining(), o.remaining());
		int cmp;
		for(int i = this.position(), j = o.position(); i < n; i ++, j ++) {
			cmp = this.get(i).compareTo(o.get(j));
			if(cmp != 0) {
				return cmp;
			}
		}
		return this.remaining() - o.remaining();
	}
}
