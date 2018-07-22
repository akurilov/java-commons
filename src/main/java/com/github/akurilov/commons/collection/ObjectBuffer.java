package com.github.akurilov.commons.collection;

/**
 @param <T> element type
 */
public interface ObjectBuffer<T extends Comparable<T>>
extends Comparable<ObjectBuffer<T>> {

	static <T extends Comparable<T>> ObjectBuffer<T> allocate(final int capacity) {
		return new ObjectBufferImpl<>(capacity);
	}

	T get();

	ObjectBuffer<T> get(final T[] dst);

	ObjectBuffer<T> get(final T[] dst, int offset, int length);

	T get(final int index);

	ObjectBuffer<T> put(final T val);

	ObjectBuffer<T> put(final T[] vals);

	ObjectBuffer<T> put(byte[] src, int offset, int length);

	ObjectBuffer<T> put(final ObjectBuffer<T> src);

	ObjectBuffer<T> put(final int index, final T val);

	T[] array();

	int capacity();

	ObjectBuffer<T> clear();

	ObjectBuffer<T> flip();

	boolean hasRemaining();

	int limit();

	ObjectBuffer<T> limit(final int newLimit);

	ObjectBuffer<T> mark();

	int position();

	ObjectBuffer<T> position(final int newPosition);

	int remaining();

	ObjectBuffer<T> reset();

	ObjectBuffer<T> rewind();
}
