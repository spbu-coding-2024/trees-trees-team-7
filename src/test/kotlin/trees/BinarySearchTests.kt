package trees

import bst.BinarySearchTree
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





}
