package com.github.akurilov.commons.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TreeUtil {

	/**
	 Deep copy the tree implemented as map of maps/leafs
	 @param srcTree the source tree
	 @return the tree copy
	 */
	@SuppressWarnings("unchecked")
	static Map<String, Object> copyTree(final Map<String, Object> srcTree) {
		final Map<String, Object> dstTree = new HashMap<>(srcTree.size());
		srcTree.forEach(
			(k, v) -> dstTree.put(k, v instanceof Map ? copyTree((Map<String, Object>) v) : v)
		);
		return dstTree;
	}

	@SuppressWarnings("unchecked")
	static Map<String, Object> addBranches(
		final Map<String, Object> dst, final Map<String, Object> src
	) {
		src.forEach(
			(k, v) -> {
				if(v instanceof Map) {
					final Object dstNode = dst.get(k);
					if(dstNode instanceof Map) {
						addBranches((Map<String, Object>) dstNode, (Map<String, Object>) v);
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

	/**
	 Merge the tree by reducing the given forest. Any leaf in any next tree is added to the
	 resulting tree, overwriting already existing leaf if any
	 @param forest the given forest (list of trees)
	 @return the resulting tree (may be empty)
	 */
	static Map<String, Object> reduceForest(final List<Map<String, Object>> forest) {
		return forest
			.stream()
			.reduce(TreeUtil::addBranches)
			.orElseGet(Collections::emptyMap);
	}
}
