package com.github.akurilov.commons.io;

import java.util.List;
import java.util.function.Consumer;

/**
 * The Java objects output supporting the batch calls
 */
public interface Output<I>
extends AutoCloseable, Consumer<I> {

	@Override
	default void accept(final I item) {
		put(item);
	}

	/**
	 Write the data item
	 @param item the item to put
	 @throws java.io.IOException if fails some-why
	 */
	boolean put(final I item);

	/**
	 Bulk put method for the items from the specified buffer
	 @param buffer the buffer containing the items to put
	 @return the count of the items successfully written
	 @throws java.io.IOException
	 */
	int put(final List<I> buffer, final int from, final int to);

	int put(final List<I> buffer);

	/**
	 Make a {@link Input} instance from this.
	 @return {@link Input} instance containing the items which had been written to this output.
	 @throws java.io.IOException
	 */
	Input<I> getInput();
}
