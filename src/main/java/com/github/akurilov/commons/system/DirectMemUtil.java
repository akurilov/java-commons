package com.github.akurilov.commons.system;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * Utility methods to work with direct (off-heap) memory
 */
public interface DirectMemUtil {

	/**
	 * Don't wait for GC to free the direct memory allocated by the given byte buffer instance.
	 * Free the direct memory immediately using hidden/proprietary JVM API.
	 * @return false if the specified buffer is null (needed for chained calls)
	 */
	static boolean free(final MappedByteBuffer buff) {
		if(buff == null) {
			return false;
		}
		((DirectBuffer) buff).cleaner().clean();
		return true;
	}

	int REUSABLE_BUFF_SIZE_MIN = 1;
	int REUSABLE_BUFF_SIZE_MAX = 0x1000000; // 16MB

	ThreadLocal<MappedByteBuffer[]> REUSABLE_BUFFS = ThreadLocal.withInitial(
		() -> {
			final int buffCount = (int) (
				Math.log(REUSABLE_BUFF_SIZE_MAX / REUSABLE_BUFF_SIZE_MIN) / Math.log(2) + 1
			);
			return new MappedByteBuffer[buffCount];
		}
	);

	/**
	 * Selects the thread local direct byte buffer fitting the requested buffer size. Useful for I/O.
	 * @param size the requested buffer size
	 * @return the buffer which size may be in the range of {@link DirectMemUtil#REUSABLE_BUFF_SIZE_MIN} and
	 * {@link DirectMemUtil#REUSABLE_BUFF_SIZE_MAX}
	 * @throws IllegalArgumentException if the requested size is less than 0
	 */
	static MappedByteBuffer getThreadLocalReusableBuff(final long size)
	throws IllegalArgumentException {

		if(size < 0) {
			throw new IllegalArgumentException("Requested negative buffer size: " + size);
		}

		final MappedByteBuffer[] threadLocalReusableBuffers = REUSABLE_BUFFS.get();
		long currBuffSize = Long.highestOneBit(size);
		if(currBuffSize > REUSABLE_BUFF_SIZE_MAX) {
			currBuffSize = REUSABLE_BUFF_SIZE_MAX;
		} else if(currBuffSize < REUSABLE_BUFF_SIZE_MAX) {
			if(currBuffSize < REUSABLE_BUFF_SIZE_MIN) {
				currBuffSize = REUSABLE_BUFF_SIZE_MIN;
			} else if(currBuffSize < size) {
				currBuffSize <<= 1;
			}
		}
		final int i = Long.numberOfTrailingZeros(currBuffSize);
		MappedByteBuffer buff = threadLocalReusableBuffers[i];

		if(buff == null) {
			buff = (MappedByteBuffer) ByteBuffer.allocateDirect((int) currBuffSize);
			/*long buffSizeSum = 0;
			for(final ByteBuffer ioBuff : ioBuffers) {
				if(ioBuff != null) {
					buffSizeSum += ioBuff.capacity();
				}
			}
			System.out.println(
				Thread.currentThread().getName() + ": allocated " + formatFixedSize(currBuffSize) +
				" of direct memory, total used by the thread: " + formatFixedSize(buffSizeSum)
			);*/
			threadLocalReusableBuffers[i] = buff;
		}

		buff
			.position(0)
			.limit(size < buff.capacity() ? Math.max(1, (int) size) : buff.capacity());
		return buff;
	}
}
