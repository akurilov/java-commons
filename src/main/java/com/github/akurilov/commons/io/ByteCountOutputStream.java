package com.github.akurilov.commons.io;

import java.io.OutputStream;
import java.util.concurrent.atomic.LongAdder;

public class ByteCountOutputStream
extends OutputStream {

	protected final LongAdder byteCount = new LongAdder();

	public final long byteCount() {
		return byteCount.sum();
	}

	@Override
	public void write(final int b) {
		byteCount.increment();
	}

	@Override
	public void write(final byte buff[], final int off, final int len) {
		byteCount.add(len);
	}

	@Override
	public void write(final byte buff[]) {
		byteCount.add(buff.length);
	}
}
