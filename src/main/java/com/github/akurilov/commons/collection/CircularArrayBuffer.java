package com.github.akurilov.commons.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public final class CircularArrayBuffer<E>
extends AbstractList<E>
implements CircularBuffer<E>, Cloneable, RandomAccess, Serializable {

	private volatile int offset = 0;
	private volatile int end = -1;

	private final E[] array;
	private final int capacity;

	@SuppressWarnings("unchecked")
	public CircularArrayBuffer(final int capacity) {
		this.array = (E[]) new Object[capacity];
		this.capacity = capacity;
	}

	@Override
	public final int capacity() {
		return capacity;
	}

	@Override
	public final int offset() {
		return offset;
	}

	@Override
	public final CircularArrayBuffer<E> offset(final int i) {
		if(i < 0 || i > capacity - 1) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public final int size() {
		if(isEmpty()) {
			return 0;
		} else if(offset < end) {
			return end - offset;
		} else {
			return capacity + end - offset;
		}
	}

	@Override
	public final boolean isEmpty() {
		return end < 0;
	}

	@Override
	public void clear() {
		offset = 0;
		end = -1;
	}

	@Override
	public final boolean add(final E e) {
		if(isEmpty()) {
			array[offset] = e;
			end = offset + 1;
			return true;
		} else if(size() < capacity) {
			array[end] = e;
			end = incrementIndex(end);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final E get(final int i) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		final int j = translateIndex(i);
		if(j < end) {
			return array[j];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public final E set(final int i, final E e) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		final E prev;
		final int j = translateIndex(i);
		if(j < end) {
			prev = array[j];
			array[j] = e;
		} else {
			throw new IndexOutOfBoundsException();
		}
		return prev;
	}

	@Override
	public E remove(final int i) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		final E e;
		if(size() == 1) {
			e = array[offset];
			clear();
		} else {
			final int j = translateIndex(i);
			e = array[j];
			if(j == offset) {
				offset = incrementIndex(offset);
			} else if(end > 0 && j == end - 1) {
				end --;
			} else if(end == 0 && j == capacity - 1) {
				end = capacity - 1;
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return e;
	}

	@Override
	public final List<E> subList(final int fromIndex, final int toIndex) {
		throw new UnsupportedOperationException();
	}

	private int translateIndex(final int i)
	throws IndexOutOfBoundsException {
		if(i < capacity - 1) {
			return (offset + i) % capacity;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	private int incrementIndex(final int i) {
		if(i < capacity - 1) {
			return i + 1;
		} else {
			return 0;
		}
	}

	private int decrementIndex(final int i) {
		if(i > 0) {
			return i - 1;
		} else {
			return capacity - 1;
		}
	}
}
