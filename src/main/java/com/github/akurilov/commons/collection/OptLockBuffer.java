package com.github.akurilov.commons.collection;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 Created by andrey on 31.03.17.
 */
public interface OptLockBuffer<T>
extends List<T>, Lock {

	void removeRange(int fromIndex, int toIndex);
}
