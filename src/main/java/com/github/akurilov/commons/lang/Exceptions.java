package com.github.akurilov.commons.lang;

public interface Exceptions {

	@SuppressWarnings("unchecked")
	static <T extends Throwable> void throwUnchecked(final Throwable t)
	throws T {
		throw (T) t;
	}
}
