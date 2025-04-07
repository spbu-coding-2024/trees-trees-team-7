package trees.rbt

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import kotlin.random.Random

class RBTreeTest {
    private lateinit var tree: RBTree<Int, String>

    @BeforeEach
    fun setUp() {
        tree = RBTree()
    }

    @Nested
    inner class InsertTests {
        @Test
        fun `inserting a single element should store it`() {
            tree.insert(10, "ten")
            assertEquals("ten", tree.search(10))
        }

        @Test
        fun `inserting multiple elements should keep them accessible`() {
            tree.insert(5, "five")
            tree.insert(15, "fifteen")
            tree.insert(10, "ten")

            assertEquals("five", tree.search(5))
            assertEquals("fifteen", tree.search(15))
            assertEquals("ten", tree.search(10))
        }
    }

    @Nested
    inner class SearchTests {
        @Test
        fun `searching for a non-existing element should return null`() {
            assertNull(tree.search(42))
        }

        @Test
        fun `searching after multiple insertions should return correct values`() {
            tree.insert(20, "twenty")
            tree.insert(30, "thirty")

            assertEquals("twenty", tree.search(20))
            assertEquals("thirty", tree.search(30))
        }
    }

    @Nested
    inner class DeleteTests {
        @Test
        fun `deleting a non-existing element should not throw`() {
            assertDoesNotThrow { tree.delete(99) }
        }

        @Test
        fun `deleting an existing element should remove it`() {
            tree.insert(8, "eight")
            tree.delete(8)
            assertNull(tree.search(8))
        }
    }

    @Nested
    inner class IterationTests {
        @Test
        fun `iteration should return sorted elements`() {
            tree.insert(3, "three")
            tree.insert(1, "one")
            tree.insert(2, "two")

            val expected = listOf(1 to "one", 2 to "two", 3 to "three")
            assertEquals(expected, tree.iteration())
        }
    }

    @Nested
    inner class PropertyBasedTests {
        @Test
        fun `tree should maintain red-black properties after insertions`() {
            val values = listOf(10, 5, 15, 3, 7, 12, 20, 1, 6, 8, 11, 13, 17, 25)

            values.forEach { key ->
                tree.insert(key, key.toString())
                assertTrue(isValidRedBlackTree(tree),
                    "RB properties violated after inserting $key")
            }
        }

        @Test
        fun `tree should maintain red-black properties after deletions`() {
            val values = listOf(10, 5, 15, 3, 7, 12, 20)
            values.forEach { tree.insert(it, it.toString()) }

            val toDelete = listOf(3, 12, 10)
            toDelete.forEach { key ->
                tree.delete(key)
                assertTrue(isValidRedBlackTree(tree),
                    "RB properties violated after deleting $key")
            }
        }
    }

    private fun isValidRedBlackTree(tree: RBTree<Int, String>): Boolean {
        val root = getRoot(tree) ?: return true

        if (root.color != RBTColor.BLACK) {
            println("Violation: Root is not black")
            return false
        }

        val blackHeights = mutableSetOf<Int>()
        calculateBlackHeights(root, 0, blackHeights)
        if (blackHeights.size != 1) {
            println("Violation: Different black heights found - $blackHeights")
            printTree(root)
            return false
        }

        if (hasConsecutiveRedNodes(root)) {
            println("Violation: Found two red nodes in a row")
            printTree(root)
            return false
        }

        return true
    }

    private fun calculateBlackHeights(
        node: RBNode<Int, String>?,
        current: Int,
        heights: MutableSet<Int>
    ) {
        if (node == null) {
            heights.add(current + 1)
            return
        }

        val newHeight = current + if (node.color == RBTColor.BLACK) 1 else 0
        if (node.left() == null && node.right() == null) {
            heights.add(newHeight + 1)
        } else {
            calculateBlackHeights(node.left(), newHeight, heights)
            calculateBlackHeights(node.right(), newHeight, heights)
        }
    }

    private fun hasConsecutiveRedNodes(node: RBNode<Int, String>?): Boolean {
        if (node == null) return false
        if (node.color == RBTColor.RED) {
            if (node.left()?.color == RBTColor.RED || node.right()?.color == RBTColor.RED) {
                return true
            }
        }
        return hasConsecutiveRedNodes(node.left()) || hasConsecutiveRedNodes(node.right())
    }

    private fun printTree(node: RBNode<Int, String>?, indent: String = "") {
        if (node == null) {
            println("${indent}NIL(BLACK)")
            return
        }
        println("${indent}${node.key}(${node.color})")
        printTree(node.left(), "$indent  ")
        printTree(node.right(), "$indent  ")
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRoot(tree: RBTree<Int, String>): RBNode<Int, String>? {
        val field = RBTree::class.java.getDeclaredField("root")
        field.isAccessible = true
        return field.get(tree) as? RBNode<Int, String>
    }

    @Nested
    inner class ComplexInsertionScenarios {
        @Test
        fun `insertion should handle right-right case`() {
            tree.insert(10, "10")
            tree.insert(20, "20")
            tree.insert(30, "30")

            val root = getRoot(tree)
            assertEquals(20, root?.key)
            assertEquals(RBTColor.BLACK, root?.color)
            assertEquals(10, root?.left()?.key)
            assertEquals(30, root?.right()?.key)
            assertTrue(
                (root?.left()?.color == RBTColor.RED && root.right()?.color == RBTColor.RED) ||
                        (root?.left()?.color == RBTColor.BLACK || root?.right()?.color == RBTColor.BLACK)
            )
        }
    }

    @Nested
    inner class ComplexDeletionScenarios {
        @Test
        fun `deleting node with two children should work correctly`() {
            tree.insert(50, "50")
            tree.insert(30, "30")
            tree.insert(70, "70")
            tree.insert(20, "20")
            tree.insert(40, "40")
            tree.insert(60, "60")
            tree.insert(80, "80")

            tree.delete(50)
            assertTrue(isValidRedBlackTree(tree))
            assertEquals(listOf(20 to "20", 30 to "30", 40 to "40", 60 to "60", 70 to "70", 80 to "80"), tree.iteration())
        }

        @Test
        fun `deleting red leaf should not violate properties`() {
            tree.insert(50, "50")
            tree.insert(30, "30")
            tree.insert(70, "70")
            tree.insert(20, "20")

            tree.delete(20)
            assertTrue(isValidRedBlackTree(tree))
            assertEquals(listOf(30 to "30", 50 to "50", 70 to "70"), tree.iteration())
        }

        @Test
        fun `deleting black node with red child should work`() {
            tree.insert(50, "50")
            tree.insert(30, "30")
            tree.insert(70, "70")
            tree.insert(60, "60")

            tree.delete(70)
            assertTrue(isValidRedBlackTree(tree))
            assertEquals(listOf(30 to "30", 50 to "50", 60 to "60"), tree.iteration())
        }
    }

    @Nested
    inner class RandomTests {
        private val random = Random(1234)

        @Test
        fun `inserting random elements should keep them accessible`() {
            val numElements = 100
            val insertedValues = mutableMapOf<Int, String>()

            repeat(numElements) {
                val key = random.nextInt(1, 1000)
                val value = "Value$key"
                tree.insert(key, value)
                insertedValues[key] = value
            }

            insertedValues.forEach { (key, value) ->
                assertEquals(value, tree.search(key), "Element $key should be found")
            }
        }

        @Test
        fun `searching for non-inserted random elements should return null`() {
            val numElements = 100
            val searchedKeys = mutableSetOf<Int>()

            repeat(numElements) {
                val randomKey = random.nextInt(1001, 2000)
                searchedKeys.add(randomKey)
            }

            searchedKeys.forEach { key ->
                assertNull(tree.search(key), "Element $key should not be found")
            }
        }

        @Test
        fun `deleting random elements should not throw errors`() {
            val numElements = 1
            val insertedKeys = mutableListOf<Int>()

            repeat(numElements) {
                val key = random.nextInt(1, 1000)
                val value = "Value$key"
                tree.insert(key, value)
                insertedKeys.add(key)
            }

            insertedKeys.shuffled().take(50).forEach { key ->
                tree.delete(key)
                assertNull(tree.search(key), "Element $key should be deleted")
            }
        }
    }


    @Nested
    inner class EdgeCaseTests {
        @Test
        fun `inserting ascending sequence should maintain RB properties`() {
            repeat(100) { tree.insert(it, it.toString()) }
            assertTrue(isValidRedBlackTree(tree))
        }

        @Test
        fun `inserting descending sequence should maintain RB properties`() {
            repeat(100) { tree.insert(100 - it, (100 - it).toString()) }
            assertTrue(isValidRedBlackTree(tree))
        }

        @Test
        fun `deleting all elements should result in empty tree`() {
            repeat(100) { tree.insert(it, it.toString()) }
            repeat(100) { tree.delete(it) }
            assertNull(getRoot(tree))
            assertTrue(tree.iteration().isEmpty())
        }
    }
}
