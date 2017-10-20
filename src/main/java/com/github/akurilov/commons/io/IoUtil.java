package com.github.akurilov.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * The class contains 2 utility methods to wrap the stream into the reusable and stateless
 * thread local byte channel.
 */
public abstract class IoUtil {

	public static final int REUSABLE_BUFF_SIZE_MIN = 1;
	public static final int REUSABLE_BUFF_SIZE_MAX = 0x1000000; // 16MB

	private IoUtil() {}

	private static final ThreadLocal<ReadableByteChannel[]>
		REUSABLE_INPUT_CHANNELS = ThreadLocal.withInitial(
			() -> {
				final int count = (int) (
					Math.log(REUSABLE_BUFF_SIZE_MAX / REUSABLE_BUFF_SIZE_MIN) / Math.log(2) + 1
				);
				return new ReadableByteChannel[count];
			}
		);

	private final static class InputStreamWrapperChannel
	implements ReadableByteChannel {

		private final InputStream in;
		private final byte[] bb;

		private InputStreamWrapperChannel(final InputStream in, final int buffSize) {
			this.in = in;
			this.bb = new byte[buffSize];
		}

		@Override
		public final int read(final ByteBuffer dst)
		throws IOException {
			final int n = in.read(bb, 0, Math.min(bb.length, dst.remaining()));
			dst.put(bb, 0, n);
			return n;
		}

		@Override
		public final boolean isOpen() {
			return true;
		}

		@Override
		public final void close()
		throws IOException {
		}
	}

	/**
	 * Wraps the given input stream into the special stateless and reusable (thread local) byte
	 * channel
	 * @param in the input stream to wrap
	 * @param remainingSize the estimated size of the data to be read from the given input
	 * @return readable byte channel
	 * @throws IllegalStateException if negative remaining size is specified
	 */
	public static ReadableByteChannel getThreadLocalInputChannel(
		final InputStream in, final long remainingSize
	) throws IllegalStateException {

		if(remainingSize < 0) {
			throw new IllegalArgumentException("Requested negative size: " + remainingSize);
		}

		final ReadableByteChannel[] threadLocalReusableChannels = REUSABLE_INPUT_CHANNELS.get();
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
		ReadableByteChannel chan = threadLocalReusableChannels[i];

		if(chan == null) {
			chan = new InputStreamWrapperChannel(in, (int) currBuffSize);
			threadLocalReusableChannels[i] = chan;
		}

		return chan;
	}

	private static final ThreadLocal<WritableByteChannel[]>
		REUSABLE_OUTPUT_CHANNELS = ThreadLocal.withInitial(
			() -> {
				final int count = (int) (
					Math.log(REUSABLE_BUFF_SIZE_MAX / REUSABLE_BUFF_SIZE_MIN) / Math.log(2) + 1
				);
				return new WritableByteChannel[count];
			}
		);

	private final static class OutputStreamWrapperChannel
	implements WritableByteChannel {

		private final OutputStream out;
		private final byte[] bb;

		private OutputStreamWrapperChannel(final OutputStream out, final int buffSize) {
			this.out = out;
			this.bb = new byte[buffSize];
		}

		@Override
		public final int write(final ByteBuffer src)
		throws IOException {
			final int n = Math.min(bb.length, src.remaining());
			if(n > 0) {
				src.get(bb, 0, n);
				out.write(bb, 0, n);
			}
			return n;
		}

		@Override
		public final boolean isOpen() {
			return true;
		}

		@Override
		public final void close()
		throws IOException {
		}
	}

	/**
	 * Wraps the given output stream into the special stateless and reusable (thread local) byte
	 * channel
	 * @param out the output stream to wrap into the byte channel
	 * @param remainingSize the estimated size of the data to be written to the given output
	 * @return writable byte channel
	 * @throws IllegalStateException if negative remaining size is specified
	 */
	public static WritableByteChannel getThreadLocalOutputChannel(
		final OutputStream out, final long remainingSize
	) throws IllegalStateException {

		if(remainingSize < 0) {
			throw new IllegalArgumentException("Requested negative size: " + remainingSize);
		}

		final WritableByteChannel[] threadLocalReusableChannels = REUSABLE_OUTPUT_CHANNELS.get();
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
		WritableByteChannel chan = threadLocalReusableChannels[i];

		if(chan == null) {
			chan = new OutputStreamWrapperChannel(out, (int) currBuffSize);
			threadLocalReusableChannels[i] = chan;
		}

		return chan;
	}
}
