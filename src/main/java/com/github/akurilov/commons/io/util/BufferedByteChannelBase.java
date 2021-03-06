package com.github.akurilov.commons.io.util;

import java.nio.ByteBuffer;

public abstract class BufferedByteChannelBase
implements BufferedByteChannel {

	protected static final int REUSABLE_BUFF_SIZE_MIN = 1;
	protected static final int REUSABLE_BUFF_SIZE_MAX = 0x1000000; // 16MB

	protected final byte[] bb;
	protected final ByteBuffer buffer;

	protected BufferedByteChannelBase(final int size) {
		this.bb = new byte[size];
		this.buffer = ByteBuffer.wrap(bb);
	}

	@Override
	public final ByteBuffer buffer() {
		return buffer;
	}

	@Override
	public boolean isOpen() {
		return true;
	}
}
