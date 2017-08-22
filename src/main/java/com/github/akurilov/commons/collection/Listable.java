package com.github.akurilov.commons.collection;

import java.util.Collection;

/**
 Created on 01.08.16.
 */
public interface Listable<T> {

	T list(final String afterObjectId, final Collection<T> outputBuffer, final int limit);
}
