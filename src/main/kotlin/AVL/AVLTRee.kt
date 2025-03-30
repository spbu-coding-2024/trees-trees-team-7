package AVL

import Tree
import AVLNode

class AVLTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: AVLNode<K, V>? = null

    override fun insert(key: K, value: V) {
        root = insert(root, key, value)
    }

    private fun insert(node: AVLNode<K, V>?, key: K, value: V): AVLNode<K, V>? {
        if (node == null) 
        return AVLNode(key, value)
        
        when {
            key < node.key -> node.left = insert(node.left, key, value)
            key > node.key -> node.right = insert(node.right, key, value)
            else -> node.value = value
        }
        
        return balance(node)
    }

    override fun search(key: K): V? {
        var current = root
        while (current != null) {
            current = when {
                key < current.key -> current.left
                key > current.key -> current.right
                else -> return current.value
            }
        }
        return null
    }

    override fun delete(key: K) {
        root = delete(root, key)
    }

    private fun delete(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        node ?: return null

        when {
            key < node.key -> node.left = delete(node.left, key)
            key > node.key -> node.right = delete(node.right, key)
            else -> {
                if (node.left == null) return node.right
                if (node.right == null) return node.left

                val minNode = findMin(node.right!!)
                node.key = minNode.key
                node.value = minNode.value
                node.right = delete(node.right, node.key)
            }
        }
        
        return balance(node)
    }

    override fun iteration(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        inOrderTraversal(root, result)
        return result
    }

    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        updateHeight(node)
        val balanceFactor = getBalanceFactor(node)

        return when {
            balanceFactor > 1 -> {
                if (getBalanceFactor(node.left!!) < 0) node.left = rotateLeft(node.left!!)
                rotateRight(node)
            }
            balanceFactor < -1 -> {
                if (getBalanceFactor(node.right!!) > 0) node.right = rotateRight(node.right!!)
                rotateLeft(node)
            }
            else -> node
        }
    }

    private fun rotateRight(y: AVLNode<K, V>): AVLNode<K, V> {
        val x = y.left!!
        val T2 = x.right
        
        x.right = y
        y.left = T2
        
        updateHeight(y)
        updateHeight(x)
        
        return x
    }

    private fun rotateLeft(x: AVLNode<K, V>): AVLNode<K, V> {
        val y = x.right!!
        val T2 = y.left
        
        y.left = x
        x.right = T2
        
        updateHeight(x)
        updateHeight(y)
        
        return y
    }

    private fun updateHeight(node: AVLNode<K, V>) {
        node.height = maxOf(height(node.left), height(node.right)) + 1
    }

    private fun height(node: AVLNode<K, V>?): Int = node?.height ?: 0

    private fun getBalanceFactor(node: AVLNode<K, V>) = height(node.left) - height(node.right)

    private fun findMin(node: AVLNode<K, V>): AVLNode<K, V> {
        var current = node
        while (current.left != null) current = current.left!!
        return current
    }

    private fun inOrderTraversal(node: AVLNode<K, V>?, result: MutableList<Pair<K, V>>) {
        node?.let {
            inOrderTraversal(it.left, result)
            result.add(Pair(it.key, it.value))
            inOrderTraversal(it.right, result)
        }
    }
}
