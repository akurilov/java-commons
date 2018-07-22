package com.github.akurilov.commons.collection;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.InvalidMarkException;

public final class ObjectBufferImpl<T extends Comparable<T>>
implements ObjectBuffer<T> {

	private final T[] buff;
	private final int offset;

	private int mark = -1;
	private int position = 0;
	private int limit;
	private int capacity;

	@SuppressWarnings("unchecked")
	ObjectBufferImpl(final int capacity) {
		this((T[]) new Object[capacity]);
	}

	/**
	 Wrap constructor
	 */
	ObjectBufferImpl(final T[] buff) {
		this(buff, 0, buff.length);
	}

	/**
	 Wrap slice constructor
	 @param buff
	 @param offset
	 @param length
	 */
	ObjectBufferImpl(final T[] buff, final int offset, final int length) {
		this.buff = buff;
		this.offset = offset;
		this.capacity = buff.length;
		this.limit = capacity;
	}

	private int nextGetIndex() {
		if(position >= limit) {
			throw new BufferUnderflowException();
		}
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

	private int checkIndex(int i) {
		if((i < 0) || (i >= limit)) {
			throw new IndexOutOfBoundsException();
		}
		return i;
	}

	@Override
	public final ObjectBufferImpl<T> get(final T[] dst) {
		return get(dst, 0, dst.length);
	}

	@Override
	public final ObjectBufferImpl<T> get(final T[] dst, final int offset, final int length) {
		checkBounds(length, dst.length);
		if (length > remaining())
			throw new BufferUnderflowException();
		System.arraycopy(buff, position(), dst, offset, length);
		position(position() + length);
		return this;
	}

	private static void checkBounds(final int len, final int size) {
		if((len | (size - len)) < 0) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public final ObjectBufferImpl<T> put(final T val) {
		buff[nextPutIndex()] = val;
		return this;
	}

	private int nextPutIndex() {
		if(position >= limit) {
			throw new BufferOverflowException();
		}
		return position ++;
	}

	@Override
	public final ObjectBufferImpl<T> put(final T[] vals) {
		return put(vals, 0, vals.length);
	}

	@Override
	public final ObjectBufferImpl<T> put(final T[] src, final int offset, final int length) {
		checkBounds(length, src.length);
		if(length > remaining()) {
			throw new BufferOverflowException();
		}
		System.arraycopy(src, offset, buff, position(), length);
		position(position() + length);
		return this;
	}

	@Override
	public final ObjectBufferImpl<T> put(final ObjectBuffer<T> src) {
		if(src == this) {
			throw new IllegalArgumentException();
		}
		final int n = src.remaining();
		if(n > remaining()) {
			throw new BufferOverflowException();
		}
		src.get(buff, position(), n);
		position(position() + n);
		return this;
	}

	@Override
	public final ObjectBufferImpl<T> put(final int i, final T val) {
		buff[checkIndex(i)] = val;
		return this;
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

	@Override
	public final int hashCode() {
		int h = 1;
		final int p = position();
		for(int i = limit() - 1; i >= p; i --) {
			h = 31 * h + (int) get(i);
		}
		return h;
	}

	@SuppressWarnings("unchecked") @Override
	public final boolean equals(final Object o) {
		if(this == o) {
			return true;
		}
		if(!(o instanceof ObjectBufferImpl)) {
			return false;
		}
		final ObjectBufferImpl<T> that = (ObjectBufferImpl<T>) o;
		if(this.remaining() != that.remaining()) {
			return false;
		}
		final int p = this.position();
		for(int i = this.limit() - 1, j = that.limit() - 1; i >= p; i --, j --) {
			if(this.get(i) != that.get(j)) {
				return false;
			}
		}
		return true;
	}
}
