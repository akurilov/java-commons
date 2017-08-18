package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Input;
import com.github.akurilov.commons.io.Output;

/**
 * An items buffer acting as input either output
 */
public interface IoBuffer<T>
extends Input<T>, Output<T> {

	/**
	 * @return true if the buffer is empty
	 */
	boolean isEmpty();

	/**
	 * @return count of the items in the buffer
	 */
	int size();
}
