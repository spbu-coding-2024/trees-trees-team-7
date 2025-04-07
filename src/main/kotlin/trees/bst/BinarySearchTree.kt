package trees.bst

import Tree

class BinarySearchTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: BSTNode<K, V>? = null

    private fun insertRecursion(node: BSTNode<K, V>?, key: K, value: V): BSTNode<K, V> {
        if (node == null) {
            return BSTNode(key, value)
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

    private fun searchRecursion(node: BSTNode<K, V>?, key: K): V? {
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

    private fun deleteRecursion(node: BSTNode<K, V>?, key: K): BSTNode<K, V>? {
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

    private fun minValue(node: BSTNode<K, V>): K {
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
        val queue = ArrayDeque<BSTNode<K, V>?>()

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

    fun contains(key: K): Boolean {
        return containsRecursive(root, key)
    }

    private fun containsRecursive(node: BSTNode<K, V>?, key: K): Boolean {
        if (node == null) {
            return false
        }

        return when {
            key < node.key -> containsRecursive(node.left, key)
            key > node.key -> containsRecursive(node.right, key)
            else -> true
        }
    }
}
