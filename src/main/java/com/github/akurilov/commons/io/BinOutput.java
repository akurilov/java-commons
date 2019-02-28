package com.github.akurilov.commons.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 * The item output implementation serializing something into the specified stream
 */
public abstract class BinOutput<T>
implements Output<T> {
	
	protected final ObjectOutputStream output;
	
	protected BinOutput(final ObjectOutputStream output) {
		this.output = output;
	}
	
	@Override
	public boolean put(final T item) {
		try {
			output.writeUnshared(item);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return true;
	}
	
	@Override
	public int put(final List<T> buffer, final int from, final int to) {
		try {
			output.writeUnshared(
				buffer
					.subList(from, to)
					.toArray(new Object[to - from])
			);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return to - from;
	}
	
	@Override
	public final int put(final List<T> items) {
		return put(items, 0, items.size());
	}

	@Override
	public void close() {
		try {
			output.close();
		} catch(final IOException e) {
			throwUnchecked(e);
		}
	}
	
	@Override
	public String toString() {
		return "binOutput<" + output + ">";
	}
}
