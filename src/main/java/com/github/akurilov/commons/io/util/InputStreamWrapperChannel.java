package com.github.akurilov.commons.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class InputStreamWrapperChannel
extends BufferedByteChannelBase
implements BufferedReadableByteChannel {

	private final InputStream in;

	public InputStreamWrapperChannel(final InputStream in, final int buffSize) {
		super(buffSize);
		this.in = in;
	}

	@Override
	public final int read(final ByteBuffer dst)
		throws IOException {
		final int n = in.read(bb, 0, Math.min(bb.length, dst.remaining()));
		dst.put(bb, 0, n);
		return n;
	}

	private final static ThreadLocal<BufferedReadableByteChannel[]>
		REUSABLE_INPUT_CHANNELS = ThreadLocal.withInitial(
			() -> {
				final int count = (int) (
					Math.log(REUSABLE_BUFF_SIZE_MAX / REUSABLE_BUFF_SIZE_MIN) / Math.log(2) + 1
				);
				return new BufferedReadableByteChannel[count];
			}
		);

	/**
	 * Wraps the given input stream into the special stateless and reusable (thread local) byte
	 * channel
	 * @param in the input stream to wrap
	 * @param remainingSize the estimated size of the data to be read from the given input
	 * @return readable byte channel
	 * @throws IllegalStateException if negative remaining size is specified
	 */
	public static BufferedReadableByteChannel getThreadLocalInstance(
		final InputStream in, final long remainingSize
	) throws IllegalStateException {

		if(remainingSize < 0) {
			throw new IllegalArgumentException("Requested negative size: " + remainingSize);
		}

		final BufferedReadableByteChannel[] threadLocalReusableChannels = REUSABLE_INPUT_CHANNELS.get();
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
		final int i = Long.numberOfTrailingZeros(currBuffSize);
		BufferedReadableByteChannel chan = threadLocalReusableChannels[i];

		if(chan == null) {
			chan = new InputStreamWrapperChannel(in, (int) currBuffSize);
			threadLocalReusableChannels[i] = chan;
		}

		return chan;
	}
}