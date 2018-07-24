package com.github.akurilov.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CircularArrayBufferTest {

	@Test
	public final void testBasicFunctionality()
	throws Exception {

		final int capacity = 10;

		final CircularBuffer<String> buff = new CircularArrayBuffer<>(capacity);

		// check the created buffer state
		assertEquals(capacity, buff.capacity());
		assertEquals(0, buff.offset());
		assertTrue(buff.isEmpty());
		assertEquals(0, buff.size());

		try {
			buff.get(0);
			fail("Buffer should be empty");
		} catch(final IndexOutOfBoundsException ok) {
		}

		// fill the buffer completely
		for(int i = 0; i < capacity; i ++) {
			buff.add(Integer.toString(i));
			assertEquals(0, buff.offset());
			assertEquals(i + 1, buff.size());
		}
		assertEquals(0, buff.offset());

		for(int i = 0; i < capacity; i ++) {
			assertEquals(Integer.toString(i), buff.get(i));
		}

		// check that the extra element is not added
		assertFalse(buff.add(Integer.toString(capacity)));
		assertEquals(capacity, buff.size());

		try {
			buff.get(capacity);
			fail("Buffer shouldn't contain the element with index #" + capacity);
		} catch(final IndexOutOfBoundsException ok) {
		}

		// remove 5 elements from the beginning
		final int additionalCount = 5;
		for(int i = 0; i < additionalCount; i ++) {
			buff.remove(0);
			assertEquals(capacity - i - 1, buff.size());
			assertEquals(i + 1, buff.offset());
		}
		assertEquals(additionalCount, buff.offset());

		for(int i = 0; i < capacity - additionalCount; i ++) {
			assertEquals(Integer.toString(additionalCount + i), buff.get(i));
		}

		// add 5 elements to the end
		for(int i = 0; i < additionalCount; i ++) {
			buff.add(Integer.toString(capacity + i));
			assertEquals(capacity - additionalCount + i + 1, buff.size());
		}
		assertEquals(additionalCount, buff.offset());

		for(int i = 0; i < capacity; i ++) {
			assertEquals(Integer.toString(additionalCount + i), buff.get(i));
		}

		// remove 3 elements from the beginning
		final int yetMoreCount = 3;
		buff.removeFirst(3);
		assertEquals(capacity - yetMoreCount, buff.size());
		assertEquals(additionalCount + yetMoreCount, buff.offset());

		for(int i = 0; i < capacity - yetMoreCount; i ++) {
			assertEquals(Integer.toString(additionalCount + yetMoreCount + i), buff.get(i));
		}

		// remove 3 more elements from the end
		buff.removeLast(3);
		assertEquals(capacity - 2 * yetMoreCount, buff.size());

		for(int i = 0; i < capacity - 2 * yetMoreCount; i ++) {
			assertEquals(Integer.toString(additionalCount + yetMoreCount + i), buff.get(i));
		}

		final Collection<String> elementsToAdd = Arrays.asList("a", "b", "c", "d");
		assertTrue(buff.addAll(elementsToAdd));

		assertEquals("8", buff.get(0));
		assertEquals("9", buff.get(1));
		assertEquals("10", buff.get(2));
		assertEquals("11", buff.get(3));
		assertEquals("a", buff.get(4));
		assertEquals("b", buff.get(5));
		assertEquals("c", buff.get(6));
		assertEquals("d", buff.get(7));
		try {
			buff.get(8);
			fail();
		} catch(final IndexOutOfBoundsException ok) {
		}

		buff.clear();
		assertEquals(-1, buff.end());
	}

	@Test
	public final void testBatchExchangeRate()
	throws Exception {
		final int batchSize = 0x1000;
		final CircularBuffer<Long> buff = new CircularArrayBuffer<>(batchSize);
		final Lock buffLock = new ReentrantLock();
		final ExecutorService executor = Executors.newFixedThreadPool(2);
		final LongAdder producedCounter = new LongAdder();
		final LongAdder consumedCounter = new LongAdder();
		executor.submit(
			() -> {
				int n;
				final Thread currentThread = Thread.currentThread();
				final List<Long> items = new ArrayList<>(batchSize);
				for(int i = 0; i < batchSize; i ++) {
					items.add(System.nanoTime());
				}
				while(!currentThread.isInterrupted()) {
					if(buffLock.tryLock()) {
						try {
							n = (int) (System.nanoTime() % (batchSize - buff.size()));
							if(!buff.addAll(items.subList(0, n))) {
								throw new AssertionError();
							}
							producedCounter.add(n);
						} finally {
							buffLock.unlock();
						}
					}
				}
			}
		);
		executor.submit(
			() -> {
				int n;
				final Thread currentThread = Thread.currentThread();
				while(!currentThread.isInterrupted()) {
					if(buffLock.tryLock()) {
						try {
							n = (int) (System.nanoTime() % buff.size());
							buff.removeFirst(n);
							consumedCounter.add(n);
						} finally {
							buffLock.unlock();
						}
					}
				}
			}
		);
		final int timeoutSeconds = 50;
		TimeUnit.SECONDS.sleep(timeoutSeconds);
		executor.shutdownNow();
		assertEquals(producedCounter.sum(), consumedCounter.sum(), batchSize);
		final double rate = consumedCounter.sum() / timeoutSeconds;
		assertTrue(rate > 0);
		System.out.println("Exchange rate: " + rate);
	}

	@Test
	public final void testExchangeRate()
	throws Exception {
		final int batchSize = 0x1000;
		final CircularBuffer<Long> buff = new CircularArrayBuffer<>(batchSize);
		final Lock buffLock = new ReentrantLock();
		final ExecutorService executor = Executors.newFixedThreadPool(2);
		final LongAdder producedCounter = new LongAdder();
		final LongAdder consumedCounter = new LongAdder();
		executor.submit(
			() -> {
				final Thread currentThread = Thread.currentThread();
				while(!currentThread.isInterrupted()) {
					if(buffLock.tryLock()) {
						try {
							if(buff.add(System.nanoTime())) {
								producedCounter.increment();
							}
						} finally {
							buffLock.unlock();
						}
					}
				}
			}
		);
		executor.submit(
			() -> {
				int n;
				final Thread currentThread = Thread.currentThread();
				while(!currentThread.isInterrupted()) {
					if(buffLock.tryLock()) {
						try {
							n = buff.size();
							buff.clear();
							consumedCounter.add(n);
						} finally {
							buffLock.unlock();
						}
					}
				}
			}
		);
		final int timeoutSeconds = 50;
		TimeUnit.SECONDS.sleep(timeoutSeconds);
		executor.shutdownNow();
		assertEquals(producedCounter.sum(), consumedCounter.sum(), batchSize);
		final double rate = consumedCounter.sum() / timeoutSeconds;
		assertTrue(rate > 0);
		System.out.println("Exchange rate: " + rate);
	}
}
