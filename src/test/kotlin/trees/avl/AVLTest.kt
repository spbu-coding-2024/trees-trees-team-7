package trees.avl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import java.util.concurrent.TimeUnit
import kotlin.random.Random

internal class AVLTreeTest {
    private lateinit var tree: AVLTree<Int, String>

    @BeforeEach
    fun setup() {
        tree = AVLTree()
    }

    @Test
    fun `insert root node`() {
        tree.insert(10, "A")
        assertThat(tree.getRoot()?.key).isEqualTo(10)
        assertBalanced()
    }

    @Test
    fun `search in empty tree`() {
        assertThat(tree.search(42)).isNull()
    }

    @Test
    fun `delete from empty tree`() {
        tree.delete(10)
        assertThat(tree.iteration()).isEmpty()
    }

    @Test
    fun `left-left rotation`() {
        listOf(30, 20, 10).forEach { tree.insert(it, "$it") }
        assertThat(tree.getRoot()?.key).isEqualTo(20)
        assertBalanced()
    }

    @Test
    fun `right-right rotation`() {
        listOf(10, 20, 30).forEach { tree.insert(it, "$it") }
        assertThat(tree.getRoot()?.key).isEqualTo(20)
        assertBalanced()
    }

    @Test
    fun `left-right rotation`() {
        listOf(30, 10, 20).forEach { tree.insert(it, "$it") }
        assertThat(tree.getRoot()?.key).isEqualTo(20)
        assertBalanced()
    }

    @Test
    fun `right-left rotation`() {
        listOf(10, 30, 20).forEach { tree.insert(it, "$it") }
        assertThat(tree.getRoot()?.key).isEqualTo(20)
        assertBalanced()
    }

    @Test
    fun `delete leaf node`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.delete(5)
        assertThat(tree.iteration()).containsExactly(10 to "A")
        assertBalanced()
    }

    @Test
    fun `delete node with one child`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.delete(5)
        assertThat(tree.iteration()).containsExactly(10 to "A", 15 to "C")
        assertBalanced()
    }

    @Test
    fun `delete node with two children`() {
        tree.insert(20, "A")
        tree.insert(10, "B")
        tree.insert(30, "C")
        tree.insert(5, "D")
        tree.insert(15, "E")
        tree.delete(10)
        assertThat(tree.iteration()).containsExactly(5 to "D", 15 to "E", 20 to "A", 30 to "C")
        assertBalanced()
    }

    @Test
    fun `delete root with two children`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        tree.delete(10)
        assertThat(tree.iteration()).containsExactly(5 to "B", 15 to "C")
        assertBalanced()
    }

    @Test
    fun `in-order traversal`() {
        listOf(5, 3, 7, 2, 4, 6, 8).forEach { tree.insert(it, "$it") }
        assertThat(tree.iteration().map { it.first })
            .containsExactly(2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun `pre-order traversal`() {
        tree.insert(5, "A")
        tree.insert(3, "B")
        tree.insert(7, "C")
        assertThat(tree.dfsPreOrder()).containsExactly(5 to "A", 3 to "B", 7 to "C")
    }

    @Test
    fun `post-order traversal`() {
        tree.insert(5, "A")
        tree.insert(3, "B")
        tree.insert(7, "C")
        assertThat(tree.dfsPostOrder()).containsExactly(3 to "B", 7 to "C", 5 to "A")
    }

    @Test
    fun `insert duplicate key updates value`() {
        tree.insert(5, "Old")
        tree.insert(5, "New")
        assertThat(tree.search(5)).isEqualTo("New")
    }

    @Test
    @Timeout(1, unit = TimeUnit.SECONDS)
    fun `stress test 10k elements`() {
        val keys = List(10_000) { Random.nextInt() }.distinct()
        keys.forEach { tree.insert(it, "value") }
        keys.shuffled().forEach { tree.delete(it) }
        assertThat(tree.iteration()).isEmpty()
    }

    @Test
    fun `complex structure balance`() {
        listOf(50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45).forEach { 
            tree.insert(it, "$it") 
        }
        assertBalanced()
        tree.delete(50)
        assertBalanced()
    }

    private fun assertBalanced() {
        fun check(node: AVLNode<Int, String>?): Boolean {
            node ?: return true
            val balance = node.balanceFactor
            return balance in -1..1 && 
                check(node.left) && 
                check(node.right)
        }
        assertThat(check(tree.getRoot())).isTrue()
    }

    @Test
    fun `height updates correctly`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        tree.insert(15, "C")
        assertThat(tree.getRoot()?.height).isEqualTo(2)
    }

    @Test
    fun `balance factor calculation`() {
        tree.insert(10, "A")
        tree.insert(5, "B")
        assertThat(tree.getRoot()?.balanceFactor).isEqualTo(1)
    }
}