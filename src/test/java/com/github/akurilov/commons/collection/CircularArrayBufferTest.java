package com.github.akurilov.commons.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CircularArrayBufferTest {

	@Test
	public final void test()
	throws Exception {

		final int capacity = 10;

		final CircularBuffer<String> buff = new CircularArrayBuffer<>(capacity);

		assertEquals(capacity, buff.capacity());
		assertEquals(0, buff.offset());
		assertTrue(buff.isEmpty());
		assertEquals(0, buff.size());

		for(int i = 0; i < capacity; i ++) {
			buff.add(Integer.toString(i));
			assertEquals(0, buff.offset());
			assertEquals(i + 1, buff.size());
		}

		assertFalse(buff.add(Integer.toString(capacity)));
		assertEquals(capacity, buff.size());

		final int additionalCount = 5;
		for(int i = 0; i < additionalCount; i ++) {
			buff.remove(0);
			assertEquals(capacity - i - 1, buff.size());
			assertEquals(i + 1, buff.offset());
		}

		for(int i = 0; i < additionalCount; i ++) {
			buff.add(Integer.toString(capacity + i));
			assertEquals(capacity - additionalCount + i + 1, buff.size());
		}
	}
}
