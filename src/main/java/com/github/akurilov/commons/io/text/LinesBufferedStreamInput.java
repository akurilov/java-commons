package com.github.akurilov.commons.io.text;

import com.github.akurilov.commons.io.Input;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * The input implementation designed to read the text lines from the given input stream using {@link BufferedReader}
 */
public class LinesBufferedStreamInput
implements Input<String> {

	private final BufferedReader reader;
	
	public LinesBufferedStreamInput(final InputStream input) {
		reader = new BufferedReader(new InputStreamReader(input));
	}

	public LinesBufferedStreamInput(final InputStream input, final int buffSize) {
		reader = new BufferedReader(new InputStreamReader(input), buffSize);
	}

	/**
	 * @return next text line from the given input stream
	 */
	@Override
	public String get()
	throws EOFException, IOException {
		return reader.readLine();
	}

	/**
	 * Get some lines
	 * @param buffer buffer for the text lines
	 * @param limit max count of the text lines to put into the buffer
	 * @return the count of the text lines was got
	 */
	@Override
	public int get(final List<String> buffer, final int limit)
	throws IOException {
		int i = 0;
		try {
			for(; i < limit; i ++) {
				buffer.add(reader.readLine());
			}
		} catch(final IOException e) {
			if(i == 0) {
				throw e;
			}
		}
		return i;
	}
	
	/**
	 * Skips characters instead of lines
	 */
	@Override
	public long skip(final long count)
	throws IOException {
		return reader.skip(count);
	}
	
	/**
	 * Most probably will cause an IOException
	 * @throws IOException
	 */
	@Override
	public void reset()
	throws IOException {
		reader.reset();
	}
	
	@Override
	public void close()
	throws IOException {
		reader.close();
	}
}
