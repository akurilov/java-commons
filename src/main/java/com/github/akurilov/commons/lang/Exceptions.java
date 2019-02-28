package com.github.akurilov.commons.lang;

import java.io.IOException;

public interface Exceptions {

	@SuppressWarnings("unchecked")
	static <T extends Throwable> void throwUnchecked(final Throwable t)
	throws T {
		throw (T) t;
	}
}
