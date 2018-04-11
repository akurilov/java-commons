package com.github.akurilov.commons.system;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * Utility methods to work with direct (off-heap) memory
 */
public interface DirectMemUtil {

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

		final var threadLocalReusableBuffers = REUSABLE_BUFFS.get();
		var currBuffSize = Long.highestOneBit(size);
		if(currBuffSize > REUSABLE_BUFF_SIZE_MAX) {
			currBuffSize = REUSABLE_BUFF_SIZE_MAX;
		} else if(currBuffSize < REUSABLE_BUFF_SIZE_MAX) {
			if(currBuffSize < REUSABLE_BUFF_SIZE_MIN) {
				currBuffSize = REUSABLE_BUFF_SIZE_MIN;
			} else if(currBuffSize < size) {
				currBuffSize <<= 1;
			}
		}
		final var i = Long.numberOfTrailingZeros(currBuffSize);
		var buff = threadLocalReusableBuffers[i];

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
