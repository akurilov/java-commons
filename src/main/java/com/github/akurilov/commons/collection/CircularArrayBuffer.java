package com.github.akurilov.commons.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class CircularArrayBuffer<E>
extends AbstractList<E>
implements CircularBuffer<E>, Cloneable, RandomAccess, Serializable {

	private volatile int offset = 0;
	private volatile int end = -1;

	protected final E[] array;
	protected final int capacity;

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
	public final int end() {
		return end;
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
	public final void clear() {
		offset = 0;
		end = -1;
	}

	@Override
	public final boolean add(final E e) {
		if(isEmpty()) {
			array[offset] = e;
			end = increaseIndex(offset, 1);
			return true;
		} else if(size() < capacity) {
			array[end] = e;
			end = increaseIndex(end, 1);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final boolean addAll(final Collection<? extends E> elements) {
		final int elementsCount = elements.size();
		if(capacity - size() < elementsCount) {
			return false;
		} else {
			for(final E e: elements) {
				if(isEmpty()) {
					array[offset] = e;
					end = increaseIndex(offset, 1);
				} else if(size() < capacity) {
					array[end] = e;
					end = increaseIndex(end, 1);
				}
			}
			return true;
		}
	}

	@Override
	public E get(final int i) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		return array[translateIndex(i)];
	}

	@Override
	public E set(final int i, final E e) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException(Integer.toString(i));
		}
		final int j = translateIndex(i);
		final E prev = array[j];
		array[j] = e;
		return prev;
	}

	@Override
	public final E remove(final int i) {
		if(isEmpty()) {
			throw new IndexOutOfBoundsException(Integer.toString(i));
		}
		final E e;
		if(size() == 1) {
			e = array[offset];
			clear();
		} else {
			final int j = translateIndex(i);
			e = array[j];
			if(j == offset) {
				offset = increaseIndex(offset, 1);
			} else if(end > 0 && j == end - 1) {
				end --;
			} else if(end == 0 && j == capacity - 1) {
				end = capacity - 1;
			} else {
				throw new UnsupportedOperationException("Able to remove only from the beginning either end");
			}
		}
		return e;
	}

	@Override
	public final CircularArrayBuffer<E> removeFirst(final int n) {
		if(size() < n) {
			throw new IndexOutOfBoundsException();
		} else if(size() == n) {
			clear();
		} else {
			offset = increaseIndex(offset, n);
		}
		return this;
	}

	@Override
	public final CircularArrayBuffer<E> removeLast(final int n) {
		if(size() < n) {
			throw new IndexOutOfBoundsException();
		} else if(size() == n) {
			clear();
		} else {
			if(n > end) {
				end = capacity + end - n;
			} else {
				end -= n;
			}
		}
		return this;
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		return new ListIteratorImpl<>(this, 0);
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ListIteratorImpl<>(this, 0);
	}

	@Override
	public ListIterator<E> listIterator(final int i) {
		return new ListIteratorImpl<>(this, i);
	}

	protected static class ListIteratorImpl<E>
	implements ListIterator<E> {

		protected final CircularBuffer<E> buff;
		protected final int size;
		protected final int startIndex;

		private int i;

		protected ListIteratorImpl(final CircularBuffer<E> buff, final int startIndex) {
			this.buff = buff;
			this.size = buff.size();
			this.startIndex = startIndex;
			this.i = startIndex;
		}

		@Override
		public boolean hasNext() {
			return i < size;
		}

		@Override
		public final E next() {
			try {
				return buff.get(i ++);
			} catch(final IndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}
		}

		/**
		 @throws UnsupportedOperationException always, as far as remove is not supported
		 */
		@Override
		public final void remove() {
			throw new UnsupportedOperationException("Removing an element is not supported");
		}

		@Override
		public boolean hasPrevious() {
			return i > startIndex;
		}

		@Override
		public final E previous() {
			if(hasPrevious()) {
				return buff.get(-- i);
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public int nextIndex() {
			return i;
		}

		@Override
		public int previousIndex() {
			return i - 1;
		}

		@Override
		public void set(final E e)
		throws IndexOutOfBoundsException {
			buff.set(i, e);
		}

		/**
		 @throws UnsupportedOperationException always, as far as inserting an element is not supported
		 */
		@Override
		public final void add(final E e)
		throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Inserting an element is not supported");
		}
	}

	private int translateIndex(final int i)
	throws IndexOutOfBoundsException {
		if(i < size()) {
			return (offset + i) % capacity;
		} else {
			throw new IndexOutOfBoundsException(Integer.toString(i));
		}
	}

	private int increaseIndex(final int i, final int n) {
		return (i + n) % capacity;
	}
}
