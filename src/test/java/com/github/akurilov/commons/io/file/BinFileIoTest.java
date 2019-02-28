package com.github.akurilov.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.EOFException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BinFileIoTest {

	@Test
	public final void testInputAfterOutput()
	throws Exception {

		Item nextItem;

		Path itemsFilePath = null;

		try(final FileOutput<Item> itemsOutput = new BinFileOutput<>()) {
			for(int i = 0; i < 123; i ++) {
				nextItem = new Item();
				nextItem.field1 = i;
				nextItem.field2 = Integer.toString(i);
				itemsOutput.put(nextItem);
			}
			itemsFilePath = itemsOutput.getFilePath();
		}

		assertNotNull(itemsFilePath);
		System.out.println(itemsFilePath);

		final List<Item> items = new ArrayList<>(200);
		int n = 0;
		try(final FileInput<Item> itemsInput = new BinFileInput<>(itemsFilePath)) {
			int m;
			while(n < 200) {
				m = itemsInput.get(items, 200 - n);
				if(m < 0) {
					break;
				} else {
					n += m;
				}
			}
		} catch(final EOFException ignore) {
		}

		assertEquals(123, n);
		for(int i = 0; i < 123; i ++) {
			nextItem = items.get(i);
			assertEquals(nextItem.field1, i);
			assertEquals(nextItem.field2, Integer.toString(i));
		}
	}
}
