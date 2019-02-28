package com.github.akurilov.commons.io;

import java.util.List;
import java.util.function.Supplier;

/**
 * The Java objects input supporting the batch calls
 */
public interface Input<I>
extends AutoCloseable, Supplier<I> {

	String DELIMITER = ";";

	/**
	 Get next item
	 @return next item or null if no items available more
	 @throws java.io.EOFException if no item available more
	 @throws java.io.IOException if failed to get some-why
	 */
	I get();

	/**
	 Bulk items get.
	 @param buffer buffer for the items
	 @param limit max count of the items to put into the buffer
	 @return count of the items have been get and put into the buffer actually
	 @throws java.io.EOFException if no item available more
	 @throws java.io.IOException if failed to get some-why
	 */
	int get(final List<I> buffer, final int limit);
	
	/**
	 * Skip some items.
	 * @param count count of items should be skipped from the input stream
	 * @throws java.io.IOException if failed to skip such amount of bytes
	 */
	long skip(final long count);
	
	/**
	 Reset this input making this readable from the beginning
	 @throws java.io.IOException
	 */
	void reset();
}
