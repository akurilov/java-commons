package com.github.akurilov.commons.lang;

import org.junit.Test;

import java.io.IOException;

import static com.github.akurilov.commons.lang.Exceptions.throwUnchecked;
import static org.junit.Assert.fail;

public class ExceptionsTest {

	@Test
	public void test() {
		throwUnchecked(new IOException("try to catch me!"));
		fail();
	}
}
