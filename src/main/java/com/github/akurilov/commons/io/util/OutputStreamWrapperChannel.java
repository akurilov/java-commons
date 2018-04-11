package com.github.akurilov.commons.io.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class OutputStreamWrapperChannel
extends BufferedByteChannelBase
implements BufferedWritableByteChannel {

	private OutputStream out = null;

	public OutputStreamWrapperChannel(final OutputStream out, final int buffSize) {
		super(buffSize);
		this.out = out;
	}

	@Override
	public final int write(final ByteBuffer src)
	throws IOException {
		final var n = Math.min(bb.length, src.remaining());
		if(n > 0) {
			src.get(bb, 0, n);
			out.write(bb, 0, n);
		}
		return n;
	}

	private static final ThreadLocal<OutputStreamWrapperChannel[]>
		REUSABLE_OUTPUT_CHANNELS = ThreadLocal.withInitial(
			() -> {
				final var count = (int) (
					Math.log(REUSABLE_BUFF_SIZE_MAX / REUSABLE_BUFF_SIZE_MIN) / Math.log(2) + 1
				);
				return new OutputStreamWrapperChannel[count];
			}
		);

	/**
	 * Wraps the given output stream into the special stateless and reusable (thread local) byte
	 * channel
	 * @param out the output stream to wrap into the byte channel
	 * @param remainingSize the estimated size of the data to be written to the given output
	 * @return writable byte channel
	 * @throws IllegalStateException if negative remaining size is specified
	 */
	public static BufferedWritableByteChannel getThreadLocalInstance(
		final OutputStream out, final long remainingSize
	) throws IllegalStateException {

		if(remainingSize < 0) {
			throw new IllegalArgumentException("Requested negative size: " + remainingSize);
		}

		final var threadLocalReusableChannels = REUSABLE_OUTPUT_CHANNELS.get();
		long currBuffSize = Long.highestOneBit(remainingSize);
		if(currBuffSize > REUSABLE_BUFF_SIZE_MAX) {
			currBuffSize = REUSABLE_BUFF_SIZE_MAX;
		} else if(currBuffSize < REUSABLE_BUFF_SIZE_MAX) {
			if(currBuffSize < REUSABLE_BUFF_SIZE_MIN) {
				currBuffSize = REUSABLE_BUFF_SIZE_MIN;
			} else if(currBuffSize < remainingSize) {
				currBuffSize <<= 1;
			}
		}
		final var i = Long.numberOfTrailingZeros(currBuffSize);
		var chan = threadLocalReusableChannels[i];

		if(chan == null) {
			chan = new OutputStreamWrapperChannel(out, (int) currBuffSize);
			threadLocalReusableChannels[i] = chan;
		}
		chan.out = out;

		return chan;
	}

	@Override
	public void close()
	throws IOException {
		out.flush();
		out = null;
	}
}
