package com.github.akurilov.commons.io;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;

/**
 * The input implementation designed to read the text lines from the given input stream using {@link BufferedReader}
 */
public class TextStreamInput
implements Input<String> {

	private final BufferedReader reader;
	
	public TextStreamInput(final InputStream input) {
		reader = new BufferedReader(new InputStreamReader(input));
	}

	public TextStreamInput(final InputStream input, final int buffSize) {
		reader = new BufferedReader(new InputStreamReader(input), buffSize);
	}

	/**
	 * @return next text line from the given input stream
	 */
	@Override
	public String get() {
		String nextLine = null;
		try {
			nextLine = reader.readLine();
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		if(nextLine == null) {
			throwUnchecked(new EOFException());
		}
		return nextLine;
	}

	/**
	 * Get some lines
	 * @param buffer buffer for the text lines
	 * @param limit max count of the text lines to put into the buffer
	 * @return the count of the text lines was got
	 */
	@Override
	public int get(final List<String> buffer, final int limit) {
		var i = 0;
		try {
			String nextLine;
			for(; i < limit; i ++) {
				nextLine = reader.readLine();
				if(nextLine == null) {
					if(i == 0) {
						throw new EOFException();
					} else {
						break;
					}
				} else {
					buffer.add(nextLine);
				}
			}
		} catch(final IOException e) {
			if(i == 0) {
				throwUnchecked(e);
			}
		}
		return i;
	}
	
	/**
	 * Skips characters instead of lines
	 */
	@Override
	public long skip(final long count) {
		try {
			return reader.skip(count);
		} catch(final IOException e) {
			throwUnchecked(e);
		}
		return count;
	}
	
	/**
	 * Most probably will cause an IOException
	 * @throws IOException
	 */
	@Override
	public void reset() {
		try {
			reader.reset();
		} catch(final IOException e) {
			throwUnchecked(e);
		}
	}
	
	@Override
	public void close() {
		try {
			reader.close();
		} catch(final IOException e) {
			throwUnchecked(e);
		}
	}
}
