package rbt

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
    inner class BasicOperations {
        @Test
        fun `insert and search single element`() {
            tree.insert(10, "Value10")
            assertEquals("Value10", tree.search(10))
        }

        @Test
        fun `search in empty tree`() {
            assertNull(tree.search(1))
        }

        @Test
        fun `search non-existent element`() {
            tree.insert(10, "Value10")
            assertNull(tree.search(20))
        }

        @Test
        fun `insert duplicate key updates value`() {
            tree.insert(10, "OldValue")
            tree.insert(10, "NewValue")
            assertEquals("NewValue", tree.search(10))
        }

        @Test
        fun `delete from empty tree`() {
            assertDoesNotThrow { tree.delete(1) }
        }

        @Test
        fun `delete non-existent element`() {
            tree.insert(10, "Value10")
            assertDoesNotThrow { tree.delete(20) }
        }
    }

    @Nested
    inner class RedBlackTreeProperties {
        @Test
        fun `root is black after insertion`() {
            tree.insert(10, "Value10")
            // Проверка цвета корня через reflection, так как цвет не публичный
            val rootField = tree.javaClass.getDeclaredField("root")
            rootField.isAccessible = true
            val root = rootField.get(tree) as RBNode<*, *>
            assertEquals(Color.BLACK, root.color)
        }

        @Test
        fun `all paths have same black height`() {
            val keys = listOf(10, 5, 15, 3, 7, 12, 20)
            keys.forEach { tree.insert(it, "Value$it") }

            val blackHeights = mutableSetOf<Int>()
            calculateBlackHeights(tree, blackHeights)
            assertEquals(1, blackHeights.size)
        }

        private fun calculateBlackHeights(tree: RBTree<Int, String>, heights: MutableSet<Int>, node: RBNode<Int, String>? = null, currentHeight: Int = 0) {
            val currentNode = node ?: getRoot(tree)
            if (currentNode == null) {
                heights.add(currentHeight + 1) // +1 для NIL узлов (черные)
                return
            }

            val newHeight = currentHeight + if (currentNode.color == Color.BLACK) 1 else 0
            if (currentNode.left() == null && currentNode.right() == null) {
                heights.add(newHeight + 1) // +1 для NIL узлов (черные)
            } else {
                calculateBlackHeights(tree, heights, currentNode.left(), newHeight)
                calculateBlackHeights(tree, heights, currentNode.right(), newHeight)
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun getRoot(tree: RBTree<Int, String>): RBNode<Int, String>? {
            val rootField = tree.javaClass.getDeclaredField("root")
            rootField.isAccessible = true
            return rootField.get(tree) as? RBNode<Int, String>
        }
    }

    @Nested
    inner class IterationTests {
        @Test
        fun `iteration on empty tree`() {
            assertEquals(emptyList<Pair<Int, String>>(), tree.iteration())
        }

        @Test
        fun `iteration returns all elements`() {
            val elements = listOf(3 to "Three", 1 to "One", 4 to "Four", 2 to "Two")
            elements.forEach { (k, v) -> tree.insert(k, v) }

            val result = tree.iteration()
            assertEquals(elements.sortedBy { it.first }, result)
        }
    }

    @Nested
    inner class PropertyBasedTests {
        @Test
        fun `insert and search random elements`() {
            val random = Random(42)
            val testData = (1..100).associate { random.nextInt() to "Value$it" }

            testData.forEach { (k, v) -> tree.insert(k, v) }
            testData.forEach { (k, v) -> assertEquals(v, tree.search(k)) }
        }

        @Test
        fun `insert delete and search random elements`() {
            val random = Random(42)
            val testData = (1..100).associate { it to "Value$it" }

            // Вставляем все элементы
            testData.forEach { (k, v) -> tree.insert(k, v) }

            // Удаляем половину случайных элементов
            val keysToDelete = testData.keys.shuffled(random).take(50)
            keysToDelete.forEach { tree.delete(it) }

            // Проверяем оставшиеся
            testData.forEach { (k, v) ->
                if (k in keysToDelete) {
                    assertNull(tree.search(k))
                } else {
                    assertEquals(v, tree.search(k))
                }
            }
        }

        @Test
        fun `all paths have same black height after random operations`() {
            val random = Random(42)
            repeat(10) {
                val tree = RBTree<Int, String>()
                val operations = (1..1000).map {
                    if (random.nextBoolean()) "insert" else "delete"
                }

                val insertedKeys = mutableSetOf<Int>()
                operations.forEach { op ->
                    when (op) {
                        "insert" -> {
                            val key = random.nextInt(1000)
                            tree.insert(key, "Value$key")
                            insertedKeys.add(key)
                        }
                        "delete" -> {
                            if (insertedKeys.isNotEmpty()) {
                                val key = insertedKeys.random(random)
                                tree.delete(key)
                                insertedKeys.remove(key)
                            }
                        }
                    }

                    // Проверяем инварианты после каждой операции
                    if (insertedKeys.isNotEmpty()) {
                        val blackHeights = mutableSetOf<Int>()
                        calculateBlackHeights(tree, blackHeights)
                        assertEquals(1, blackHeights.size, "Black height violated after $op")
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun calculateBlackHeights(tree: RBTree<Int, String>, heights: MutableSet<Int>) {
            val rootField = tree.javaClass.getDeclaredField("root")
            rootField.isAccessible = true
            val root = rootField.get(tree) as? RBNode<Int, String>

            fun traverse(node: RBNode<Int, String>?, currentHeight: Int) {
                if (node == null) {
                    heights.add(currentHeight + 1) // +1 для NIL узлов
                    return
                }

                val newHeight = currentHeight + if (node.color == Color.BLACK) 1 else 0
                if (node.left() == null && node.right() == null) {
                    heights.add(newHeight + 1)
                } else {
                    traverse(node.left(), newHeight)
                    traverse(node.right(), newHeight)
                }
            }

            heights.clear()
            traverse(root, 0)
        }
    }

    @Nested
    inner class EdgeCases {
        @Test
        fun `insert maximum and minimum values`() {
            tree.insert(Int.MAX_VALUE, "Max")
            tree.insert(Int.MIN_VALUE, "Min")

            assertEquals("Max", tree.search(Int.MAX_VALUE))
            assertEquals("Min", tree.search(Int.MIN_VALUE))
        }

        @Test
        fun `insert same key multiple times`() {
            repeat(10) { tree.insert(1, "Value$it") }
            assertEquals("Value9", tree.search(1))
        }

        @Test
        fun `delete all elements`() {
            val keys = listOf(10, 5, 15, 3, 7, 12, 20)
            keys.forEach { tree.insert(it, "Value$it") }
            keys.forEach { tree.delete(it) }

            keys.forEach { assertNull(tree.search(it)) }
            assertEquals(emptyList<Pair<Int, String>>(), tree.iteration())
        }
    }
}