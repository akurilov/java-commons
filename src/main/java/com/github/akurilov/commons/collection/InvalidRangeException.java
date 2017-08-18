package com.github.akurilov.commons.collection;

/**
 Thrown in case of invalid range construction
 */
public final class InvalidRangeException
extends IllegalArgumentException {

	public InvalidRangeException(final String msg) {
		super(msg);
	}
}
