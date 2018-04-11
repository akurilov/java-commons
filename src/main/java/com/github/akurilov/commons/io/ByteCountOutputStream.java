package com.github.akurilov.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.LongAdder;

public class ByteCountOutputStream
extends OutputStream {

	protected final LongAdder byteCount = new LongAdder();

	public final long byteCount() {
		return byteCount.sum();
	}

	@Override
	public void write(final int b)
	throws IOException {
		byteCount.increment();
	}

	@Override
	public void write(final byte buff[], final int off, final int len)
	throws IOException {
		byteCount.add(len);
	}

	@Override
	public void write(final byte buff[])
	throws IOException {
		byteCount.add(buff.length);
	}
}
