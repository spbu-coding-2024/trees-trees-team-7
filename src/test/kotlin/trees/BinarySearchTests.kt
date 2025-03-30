package trees

import BST.BinarySearchTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class BinarySearchTests {

    @Nested
    inner class InsertsTests {

        @Test
        fun `insertion of one element`() {
            val tree =  BinarySearchTree<Int, String>()
            tree.insert(10)
            assertTrue(tree.contains(10))

        }
    }





}
