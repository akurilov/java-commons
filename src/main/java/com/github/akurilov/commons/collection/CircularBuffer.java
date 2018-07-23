package com.github.akurilov.commons.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

	/**
	 Remove the elements from the beginning.
	 @param n the count of elements to remove
	 @return this
	 @throws IndexOutOfBoundsException if the count of elements to remove is more than the current size
	 */
	CircularBuffer<E> removeFirst(final int n)
	throws IndexOutOfBoundsException;

	/**
	 Remove the last elements
	 @param n the count of elements to remove
	 @return this
	 @throws IndexOutOfBoundsException if the count of elements to remove is more than the current size
	 */
	CircularBuffer<E> removeLast(final int n)
	throws IndexOutOfBoundsException;

	/**
	 * Not supported
	 * @throws UnsupportedOperationException always
	 */
	@Override
	List<E> subList(final int fromIndex, final int toIndex)
	throws UnsupportedOperationException;

	/**
	 @return the iterator instance
	 */
	@Override
	Iterator<E> iterator();

	/**
	 Not supported
	 @throws UnsupportedOperationException always
	 */
	@Override
	ListIterator<E> listIterator()
	throws UnsupportedOperationException;

	/**
	 Not supported
	 @throws UnsupportedOperationException always
	 */
	@Override
	ListIterator<E> listIterator(final int i)
	throws UnsupportedOperationException;
}
