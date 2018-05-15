package com.github.akurilov.commons.collection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface TreeUtil {

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

	/**
	 Merge the tree by reducing the given forest. Any leaf in any next tree is added to the
	 resulting tree, overwriting already existing leaf if any
	 @param forest the given forest (list of trees)
	 @return the resulting tree (may be empty)
	 */
	static Map<String, Object> reduceForest(final List<Map<String, Object>> forest) {
		return forest
			.stream()
			.reduce(TreeUtil::deepAddEntries)
			.orElseGet(Collections::emptyMap);
	}

	@SuppressWarnings("unchecked")
	static Map<String, Object> deepAddEntries(
		final Map<String, Object> dst, final Map<String, Object> src
	) {
		src.forEach(
			(k, v) -> {
				if(v instanceof Map) {
					final Object dstNode = dst.get(k);
					if(dstNode instanceof Map) {
						deepAddEntries((Map<String, Object>) dstNode, (Map<String, Object>) v);
					} else {
						dst.put(k, v);
					}
				} else {
					dst.put(k, v);
				}
			}
		);
		return dst;
	}
}
