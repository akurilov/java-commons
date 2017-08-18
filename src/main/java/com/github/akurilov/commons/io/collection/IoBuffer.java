package com.github.akurilov.commons.io.collection;

import com.github.akurilov.commons.io.Input;
import com.github.akurilov.commons.io.Output;

/**
 Created by kurila on 30.09.15.
 */
public interface IoBuffer<T>
extends Input<T>, Output<T> {
	
	boolean isEmpty();
	
	int size();
}
