package trees.bst

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BinarySearchTests {
    private lateinit var tree: BinarySearchTree<Int, String>

    @BeforeEach
    fun setUp() {
        tree = BinarySearchTree()
    }

    @Nested
    inner class InsertTests {
        @Test
        fun `insertion of one element`() {
            tree.insert(10, "10")
            assertTrue(tree.contains(10))
            assertEquals("10", tree.search(10))
        }

        @Test
        fun `insertion of multiple elements`() {
            tree.insert(35, "35")
            tree.insert(100, "100")
            tree.insert(0, "0")
            assertTrue(tree.contains(35))
            assertTrue(tree.contains(100))
            assertTrue(tree.contains(0))
            assertEquals("35", tree.search(35))
            assertEquals("100", tree.search(100))
            assertEquals("0", tree.search(0))
        }

        @Test
        fun `duplicate insert should update value`() {
            tree.insert(10, "Old")
            tree.insert(10, "New")
            assertEquals("New", tree.search(10))
        }
    }

    @Nested
    inner class SearchTests {
        @Test
        fun `search in empty tree returns null`() {
            assertNull(tree.search(10))
        }

        @Test
        fun `search for non-existent key`() {
            tree.insert(5, "Five")
            assertNull(tree.search(10))
        }

        @Test
        fun `search should return correct value`() {
            tree.insert(5, "Egor")
            tree.insert(3, "NeEgor")
            assertEquals("Egor", tree.search(5))
            assertEquals("NeEgor", tree.search(3))
        }
    }

    @Nested
    inner class DeleteTests {
        @Test
        fun `delete from empty tree does nothing`() {
            tree.delete(10)
            assertNull(tree.search(10))
        }

        @Test
        fun `delete leaf node`() {
            tree.insert(10, "10")
            tree.insert(5, "5")
            tree.delete(5)
            assertNull(tree.search(5))
            assertEquals("10", tree.search(10))
        }

        @Test
        fun `delete node with one child`() {
            tree.insert(10, "10")
            tree.insert(5, "5")
            tree.insert(2, "2")
            tree.delete(5)
            assertNull(tree.search(5))
            assertEquals("2", tree.search(2))
            assertEquals("10", tree.search(10))
        }

        @Test
        fun `delete node with two children`() {
            tree.insert(10, "10")
            tree.insert(5, "5")
            tree.insert(15, "15")
            tree.insert(3, "3")
            tree.insert(7, "7")
            tree.insert(12, "12")
            tree.insert(17, "17")
            tree.delete(10)
            assertNull(tree.search(10))
            assertTrue(tree.contains(5))
            assertTrue(tree.contains(15))
            assertTrue(tree.contains(3))
            assertTrue(tree.contains(7))
            assertTrue(tree.contains(12))
            assertTrue(tree.contains(17))
        }
    }

    @Nested
    inner class ContainsTests {
        @Test
        fun `contains in empty tree returns false`() {
            assertFalse(tree.contains(10))
        }

        @Test
        fun `contains for existing key returns true`() {
            tree.insert(10, "10")
            assertTrue(tree.contains(10))
        }

        @Test
        fun `contains for non-existent key returns false`() {
            tree.insert(10, "10")
            assertFalse(tree.contains(5))
        }
    }

    @Nested
    inner class IterationTests {
        @Test
        fun `iteration on empty tree returns empty list`() {
            assertTrue(tree.iteration().isEmpty())
        }

        @Test
        fun `breadth-first iteration order`() {
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
    }
}
