package trees.bst

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BinarySearchTests {


	@Nested
	inner class InsertsTests {

		@Test
		fun `insertion of one element`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(10, "10")
			assertTrue(tree.contains(10))
		}

		@Test
		fun `insertion of multiple elements`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(35, "35")
			tree.insert(100, "100")
			tree.insert(0, "0")
			assertTrue(tree.contains(35))
			assertTrue(tree.contains(100))
			assertTrue(tree.contains(0))
		}

		@Test
		fun `duplicate insert should update value`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(10, "Old")
			tree.insert(10, "New")
			assertEquals("New", tree.search(10))
		}
	}

	@Nested
	inner class EmptyCheckTests {

		@Test
		fun `search in empty tree returns null`() {
			val tree = BinarySearchTree<Int, String>()
			assertNull(tree.search(10))
		}

		@Test
		fun `delete from empty tree does nothing`() {
			val tree = BinarySearchTree<Int, String>()
			tree.delete(10) // There is nothing in it so it isnt a problem
			assertNull(tree.search(10))

		}

		@Test
		fun `contains in empty tree returns false`() {
			val tree = BinarySearchTree<Int, String>()
			assertFalse(tree.contains(10))
		}

		@Test
		fun `iteration on empty tree returns empty list`() {
			val tree = BinarySearchTree<Int, String>()
			assertTrue(tree.iteration().isEmpty())
		}
	}

	@Nested
	inner class SearchTests {
		@Test
		fun `search in empty tree`() {
			val tree = BinarySearchTree<Int, String>()
			assertNull(tree.search(10))
		}

		@Test
		fun `search for non-existent key`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(5, "Five")
			assertNull(tree.search(10))
		}

		@Test
		fun `search should return correct value`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(5, "Egor")
			tree.insert(3, "NeEgor")
			assertEquals("Egor", tree.search(5))
			assertEquals("NeEgor", tree.search(3))
		}
	}

	@Nested
	inner class DeleteTests {

		@Test
		fun `delete from empty tree`() {
			val tree = BinarySearchTree<Int, String>()
			tree.delete(10)
			assertNull(tree.search(10))
		}

		@Test
		fun `delete leaf node`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(10, "10")
			tree.insert(5, "5")
			tree.delete(5)
			assertNull(tree.search(5))
			assertEquals("10", tree.search(10))
		}

		@Test
		fun `delete node with one child`() {
			val tree = BinarySearchTree<Int, String>()
			tree.insert(10, "10")
			tree.insert(5, "5")
			tree.insert(2, "2")
			tree.delete(5)
			assertNull(tree.search(5))
			assertEquals("2", tree.search(2))
			assertEquals("10", tree.search(10))
		}


		@Nested
		inner class TraversalTests {

			@Test
			fun `empty tree traversal`() {
				val tree = BinarySearchTree<Int, String>()
				assertTrue(tree.iteration().isEmpty())
			}

			@Test
			fun `breadth-first traversal order`() {
				val tree = BinarySearchTree<Int, String>()
				tree.insert(4, "4")
				tree.insert(2, "2")
				tree.insert(6, "6")
				tree.insert(1, "1")
				tree.insert(3, "3")
				tree.insert(5, "5")
				tree.insert(7, "7")

				val expected = listOf(4 to "4", 2 to "2", 6 to "6", 1 to "1", 3 to "3", 5 to "5", 7 to "7")
				assertEquals(expected, tree.iteration())
			}

			@Nested
			inner class ContainsTests {
				@Test
				fun `contains in empty tree`() {
					val tree = BinarySearchTree<Int, String>()
					assertFalse(tree.contains(10))
				}

				@Test
				fun `contains for existing key`() {
					val tree = BinarySearchTree<Int, String>()
					tree.insert(10, "10")
					assertTrue(tree.contains(10))
				}

				@Test
				fun `contains for non-existent key`() {
					val tree = BinarySearchTree<Int, String>()
					tree.insert(10, "10")
					assertFalse(tree.contains(5))
				}
			}
		}
	}
}