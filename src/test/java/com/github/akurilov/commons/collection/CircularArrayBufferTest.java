package com.github.akurilov.commons.collection;

import java.util.Arrays;
import java.util.Collection;

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

		// check the created buffer state
		assertEquals(capacity, buff.capacity());
		assertEquals(0, buff.offset());
		assertTrue(buff.isEmpty());
		assertEquals(0, buff.size());

		// fill the buffer completely
		for(int i = 0; i < capacity; i ++) {
			buff.add(Integer.toString(i));
			assertEquals(0, buff.offset());
			assertEquals(i + 1, buff.size());
		}
		assertEquals(0, buff.offset());

		// check that the extra element is not added
		assertFalse(buff.add(Integer.toString(capacity)));
		assertEquals(capacity, buff.size());

		// remove 5 elements from the beginning
		final int additionalCount = 5;
		for(int i = 0; i < additionalCount; i ++) {
			buff.remove(0);
			assertEquals(capacity - i - 1, buff.size());
			assertEquals(i + 1, buff.offset());
		}
		assertEquals(additionalCount, buff.offset());

		// add 5 elements to the end
		for(int i = 0; i < additionalCount; i ++) {
			buff.add(Integer.toString(capacity + i));
			assertEquals(capacity - additionalCount + i + 1, buff.size());
		}
		assertEquals(additionalCount, buff.offset());

		// remove 3 elements from the beginning
		final int yetMoreCount = 3;
		buff.removeFirst(3);
		assertEquals(capacity - yetMoreCount, buff.size());
		assertEquals(additionalCount + yetMoreCount, buff.offset());
		// remove 3 more elements from the end
		buff.removeLast(3);
		assertEquals(capacity - 2 * yetMoreCount, buff.size());

		final Collection<String> elementsToAdd = Arrays.asList("a", "b", "c", "d");
		assertTrue(buff.addAll(elementsToAdd));

		for(final String s: buff) {
			System.out.println(s);
		}
	}
}
