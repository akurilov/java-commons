package com.github.akurilov.commons.collection;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtilTest {

	@Test
	public final void copyTreeTest()
	throws Exception {

		final Map<String, Object> srcTree = new HashMap<String, Object>() {{
			put("a", 0);
			put(
				"b",
				new HashMap<String, Object>() {{
					put("aa", Arrays.asList(1, 2, 3));
					put(
						"bb",
						new HashMap<String, Object>() {{
							put("aaa", "foo");
							put("bbb", "bar");
							put("ccc", null);
						}}
					);
				}}
			);
		}};

		final Map<String, Object> dstTree = TreeUtil.copyTree(srcTree);

		assertEquals(0, dstTree.get("a"));
		assertEquals(Arrays.asList(1, 2, 3), ((Map<String, Object>) dstTree.get("b")).get("aa"));
		assertEquals("foo", ((Map<String, Object>) ((Map<String, Object>) dstTree.get("b")).get("bb")).get("aaa"));
		assertEquals("bar", ((Map<String, Object>) ((Map<String, Object>) dstTree.get("b")).get("bb")).get("bbb"));
		assertNull(((Map<String, Object>) ((Map<String, Object>) dstTree.get("b")).get("bb")).get("ccc"));
	}

	@Test
	public final void addBranchesTest()
	throws Exception {

		final Map<String, Object> tree1 = new HashMap<String, Object>() {{
			put("a", 0);
			put(
				"b",
				new HashMap<String, Object>() {{
					put("aa", Arrays.asList(1, 2, 3));
				}}
			);
		}};

		final Map<String, Object> tree2 = new HashMap<String, Object>() {{
			put("a", 1);
			put(
				"b",
				new HashMap<String, Object>() {{
					put(
						"bb",
						new HashMap<String, Object>() {{
							put("aaa", "foo");
							put("bbb", "bar");
							put("ccc", null);
						}}
					);
				}}
			);
		}};
		
		TreeUtil.addBranches(tree1, tree2);

		assertEquals(1, tree1.get("a"));
		assertEquals(Arrays.asList(1, 2, 3), ((Map<String, Object>) tree1.get("b")).get("aa"));
		assertEquals("foo", ((Map<String, Object>) ((Map<String, Object>) tree1.get("b")).get("bb")).get("aaa"));
		assertEquals("bar", ((Map<String, Object>) ((Map<String, Object>) tree1.get("b")).get("bb")).get("bbb"));
		assertNull(((Map<String, Object>) ((Map<String, Object>) tree1.get("b")).get("bb")).get("ccc"));
	}

	@Test
	public final void reduceForestTest()
	throws Exception {

		final List<Map<String, Object>> forest = Arrays.asList(
			new HashMap<String, Object>() {{
				put("a", 1);
				put(
					"b",
					new HashMap<String, Object>() {{
						put(
							"bb",
							new HashMap<String, Object>() {{
								put("aaa", "foo");
								put("bbb", "bar");
								put("ccc", null);
							}}
						);
					}}
				);
			}},
			new HashMap<String, Object>() {{
				put("a", 0);
				put(
					"b",
					new HashMap<String, Object>() {{
						put("aa", Arrays.asList(1, 2, 3));
					}}
				);
			}},
			new HashMap<String, Object>() {{
				put(
					"a",
					new HashMap<String, Object>() {{
						put("aa", "yohoho");
						put("bb", "whatever");
					}}
				);
				put(
					"b",
					new HashMap<String, Object>() {{
						put("bb", null);
					}}
				);
			}}
		);
		
		final Map<String, Object> tree = TreeUtil.reduceForest(forest);

		assertEquals("yohoho", ((Map<String, Object>) tree.get("a")).get("aa"));
		assertEquals("whatever", ((Map<String, Object>) tree.get("a")).get("bb"));
		assertEquals(Arrays.asList(1, 2, 3), ((Map<String, Object>) tree.get("b")).get("aa"));
		assertNull(((Map<String, Object>) tree.get("b")).get("bb"));
	}
}
