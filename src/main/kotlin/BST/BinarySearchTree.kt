package BST

import Node
import Tree

class BinarySearchTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: Node<K, V>? = null

    private fun insertRecursion(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) {
            return Node(key, value)
        }
        when {
            key < node.key -> node.left = insertRecursion(node.left, key, value)
            key > node.key -> node.right = insertRecursion(node.right, key, value)
            else -> node.value = value
        }
        return node
    }

    override fun insert(key: K, value: V) {
        root = insertRecursion(root, key, value)
    }

    private fun searchRecursion(node: Node<K, V>?, key: K): V? {
        if (node == null || node.key == key) {
            return node?.value
        }
        return if (key < node.key) {
            searchRecursion(node.left, key)
        } else {
            searchRecursion(node.right, key)
        }
    }

    override fun search(key: K): V? {
        return searchRecursion(root, key)
    }

    override fun delete(key: K) {
        root = deleteRecursion(root, key)
    }

    private fun deleteRecursion(node: Node<K, V>?, key: K): Node<K, V>? {
        if (node == null) {
            return null
        }

        when {
            key < node.key -> node.left = deleteRecursion(node.left, key)
            key > node.key -> node.right = deleteRecursion(node.right, key)
            else -> {
                if (node.left == null) {
                    return node.right
                } else if (node.right == null) {
                    return node.left
                }

                node.key = minValue(node.right!!)
                node.right = deleteRecursion(node.right, node.key)
            }
        }
        return node
    }

    private fun minValue(node: Node<K, V>): K {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current.key
    }

    override fun iteration(): List<Pair<K, V>> {
        return breadthFirstTraversal()
    }

    private fun breadthFirstTraversal(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        val queue = ArrayDeque<Node<K, V>?>()

        if (root != null) {
            queue.add(root)
        }

        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()
            if (currentNode != null) {
                result.add(currentNode.key to currentNode.value)

                if (currentNode.left != null) {
                    queue.add(currentNode.left)
                }
                if (currentNode.right != null) {
                    queue.add(currentNode.right)
                }
            }
        }

        return result
    }
}
