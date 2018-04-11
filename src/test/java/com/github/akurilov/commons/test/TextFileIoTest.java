package com.github.akurilov.commons.test;

import com.github.akurilov.commons.io.file.FileInput;
import com.github.akurilov.commons.io.file.FileOutput;
import com.github.akurilov.commons.io.file.TextFileInput;
import com.github.akurilov.commons.io.file.TextFileOutput;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.EOFException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextFileIoTest {

	@Test
	public final void testInputAfterAppend()
	throws Exception {

		Path itemsFilePath = null;

		try(final FileOutput<String> itemsOutput = new TextFileOutput()) {
			for(int i = 0; i < 123; i ++) {
				itemsOutput.put(Integer.toString(i));
			}
			itemsFilePath = itemsOutput.getFilePath();
		}

		assertNotNull(itemsFilePath);
		System.out.println(itemsFilePath);

		try(final FileOutput<String> itemsOutput = new TextFileOutput(itemsFilePath)) {
			for(int i = 123; i < 456; i ++) {
				itemsOutput.put(Integer.toString(i));
			}
		}

		final List<String> items = new ArrayList<>(500);
		int n = 0;
		try(final FileInput<String> itemsInput = new TextFileInput(itemsFilePath)) {
			int m;
			while(n < 500) {
				m = itemsInput.get(items, 500 - n);
				if(m < 0) {
					break;
				} else {
					n += m;
				}
			}
		} catch(final EOFException ignore) {
		}

		assertEquals(456, n);

		String nextItem;
		for(int i = 0; i < 456; i ++) {
			nextItem = items.get(i);
			assertEquals(Integer.parseInt(nextItem), i);
		}
	}
}
