package com.github.akurilov.commons.collection;

import java.util.Map;
import java.util.stream.Collectors;

public interface Util {

	/**
	 Deep copy the tree implemented as map of maps/leafs
	 @param srcTree the source tree
	 @return the tree copy
	 */
	static Map<String, Object> deepCopyTree(final Map<String, Object> srcTree) {
		return srcTree
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(
					Map.Entry::getKey,
					entry -> {
						final Object value = entry.getValue();
						return value instanceof Map ?
							deepCopyTree((Map<String, Object>) value) :
							value;
					}
				)
			);
	}
}
