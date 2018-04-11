package com.github.akurilov.commons.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * The input implementation designed to write the text lines using {@link BufferedWriter}
 */
public abstract class TextStreamOutput
implements Output<String> {
	
	private static final ThreadLocal<StringBuilder> THRLOC_STRB = ThreadLocal.withInitial(
		StringBuilder::new
	);
	private static final String LINE_SEP = System.getProperty("line.separator");
	
	protected final BufferedWriter writer;

	public TextStreamOutput(final OutputStream out)
	throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(out));
	}

	public TextStreamOutput(final OutputStream out, final int buffSize)
	throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(out), buffSize);
	}
	
	@Override
	public boolean put(final String line)
	throws IOException {
		writer.write(line + LINE_SEP);
		return true;
	}

	/**
	 * Uses the thread local string builder to concatenate the lines into the single text block for better performance
	 * @throws IOException
	 */
	@Override
	public int put(final List<String> lines, final int from, final int to)
	throws IOException {
		final var strb = THRLOC_STRB.get();
		strb.setLength(0);
		for(var i = from; i < to; i ++) {
			strb.append(lines.get(i));
			strb.append(LINE_SEP);
		}
		writer.write(strb.toString());
		return to - from;
	}

	/**
	 * Uses the thread local string builder to concatenate the lines into the single text block for better performance
	 * @throws IOException
	 */
	@Override
	public int put(final List<String> lines)
	throws IOException {
		final var strb = THRLOC_STRB.get();
		strb.setLength(0);
		for(final var line : lines) {
			strb.append(line);
			strb.append(LINE_SEP);
		}
		writer.write(strb.toString());
		return lines.size();
	}
	
	@Override
	public void close()
	throws IOException {
		writer.flush();
		writer.close();
	}
}
