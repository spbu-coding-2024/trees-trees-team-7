package trees.bst

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random

class BinarySearchTreePropertyTests {

	@RepeatedTest(100)
	fun `all inserted elements must be found in the tree`() {
		val tree = BinarySearchTree<Int, String>()
		val elements = List(10) { Random.nextInt(-1000, 1000) to Random.nextInt().toString() }

		elements.forEach { (key, value) -> tree.insert(key, value) }

		elements.forEach { (key, value) ->
			assertTrue(tree.contains(key), "Tree should contain key: $key")
			assertEquals(value, tree.search(key), "Search should return correct value for key: $key")
		}
	}

	@RepeatedTest(100)
	fun `inserting the same key updates the value`() {
		val tree = BinarySearchTree<Int, String>()
		val key = Random.nextInt(-1000, 1000)
		val value1 = Random.nextInt().toString()
		val value2 = Random.nextInt().toString()

		tree.insert(key, value1)
		tree.insert(key, value2)

		assertEquals(value2, tree.search(key), "Value should be updated for key: $key")
	}

	@RepeatedTest(100)
	fun `deleted elements should not be found in the tree`() {
		val tree = BinarySearchTree<Int, String>()
		val elements = List(10) { Random.nextInt(-1000, 1000) to Random.nextInt().toString() }

		elements.forEach { (key, value) -> tree.insert(key, value) }
		elements.shuffled().forEach { (key, _) ->
			tree.delete(key)
			assertFalse(tree.contains(key), "Tree should not contain deleted key: $key")
			assertNull(tree.search(key), "Search should return null for deleted key: $key")
		}
	}

	@RepeatedTest(100)
	fun `tree should not contain elements that were never inserted`() {
		val tree = BinarySearchTree<Int, String>()
		val key = Random.nextInt(-1000, 1000)

		assertFalse(tree.contains(key), "Tree should not contain key: $key")
		assertNull(tree.search(key), "Search should return null for key: $key")
	}
}
