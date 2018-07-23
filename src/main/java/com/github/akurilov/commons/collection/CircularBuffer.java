package com.github.akurilov.commons.collection;

import java.util.Collection;
import java.util.List;

public interface CircularBuffer<E>
extends List<E> {

	/**
	 * @return the buffer capacity
	 */
	int capacity();

	/**
	 * @return the index of the 1st element
	 */
	int offset();

	/** set the 1st element index
	 * @param i the new 1st element index
	 * @return this
	 * @throws IndexOutOfBoundsException if the specified index is out of capacity bounds
	 */
	CircularBuffer<E> offset(final int i)
	throws IndexOutOfBoundsException;

	/**
	 * @param e element to add to the end of the buffer
	 * @return false if the buffer is full
	 */
	@Override
	boolean add(final E e);

	/**
	 * @param elements
	 * @return true if all elements have been added, false if not enough capacity
	 */
	@Override
	boolean addAll(final Collection<? extends E> elements);

	/**
	 * @param i element index
	 * @return element
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	@Override
	E get(final int i)
	throws IndexOutOfBoundsException;

	/**
	 * Replace the element value
	 * @param i element index
	 * @param e new element value
	 * @return previous element value
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	@Override
	E set(final int i, final E e)
	throws IndexOutOfBoundsException;

	/**
	 * Remove the element. The implementation is able to remove only 1st either last element.
	 * @param i element index
	 * @return previous element value
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 * @throws UnsupportedOperationException if the index points to not 1st neither last element
	 */
	@Override
	E remove(final int i)
	throws IndexOutOfBoundsException, UnsupportedOperationException;

	CircularBuffer<E> removeFirst(final int n);

	/**
	 * Not supported
	 * @throws UnsupportedOperationException always
	 */
	@Override
	List<E> subList(final int fromIndex, final int toIndex)
	throws UnsupportedOperationException;
}
