package com.github.akurilov.commons.concurrent;

import java.io.Closeable;
import java.io.IOException;

/**
 * A runnable task which can be stopped by the {@link Closeable#close()} method
 */
public interface StoppableTask
extends Closeable, Runnable {

	/**
	 * Stops the task
	 * @throws IOException if some kind of failure occured
	 */
	@Override
	void close()
	throws IOException;

	/**
	 * @return true if the task was once stopped, false otherwise
	 */
	boolean isClosed();
}
