package trees.avl

import Tree

class AVLTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: AVLNode<K, V>? = null

    override fun insert(key: K, value: V) {
        root = insert(root, key, value)
    }

    override fun search(key: K): V? = searchNode(key)?.value

    override fun delete(key: K) {
        root = delete(root, key)
    }

    override fun iteration(): List<Pair<K, V>> = dfsInOrder()

    private fun insert(node: AVLNode<K, V>?, key: K, value: V): AVLNode<K, V>? {
        if (node == null) return AVLNode(key, value)
        
        when {
            key < node.key -> node.left = insert(node.left, key, value)
            key > node.key -> node.right = insert(node.right, key, value)
            else -> node.value = value
        }
        
        return balance(node)
    }

    private fun searchNode(key: K): AVLNode<K, V>? {
        var current = root
        while (current != null) {
            current = when {
                key < current.key -> current.left
                key > current.key -> current.right
                else -> return current
            }
        }
        return null
    }

    fun getRoot(): AVLNode<K, V>? = root

    private fun delete(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        node ?: return null

        when {
            key < node.key -> node.left = delete(node.left, key)
            key > node.key -> node.right = delete(node.right, key)
            else -> {
                node.left ?: return node.right?.also { updateHeight(it) }
                node.right ?: return node.left?.also { updateHeight(it) }

                val minNode = findMin(node.right)
                node.key = minNode.key
                node.value = minNode.value
                node.right = delete(node.right, node.key)
            }
        }
        
        return balance(node)
    }

    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        updateHeight(node)
        return when (val balanceFactor = getBalanceFactor(node)) {
            2 -> balanceRightHeavy(node)
            -2 -> balanceLeftHeavy(node)
            else -> node
        }
    }

    private fun balanceRightHeavy(node: AVLNode<K, V>): AVLNode<K, V> {
        node.left?.let { 
            if (getBalanceFactor(it) < 0) node.left = rotateLeft(it)
        }
        return rotateRight(node)
    }

    private fun balanceLeftHeavy(node: AVLNode<K, V>): AVLNode<K, V> {
        node.right?.let { 
            if (getBalanceFactor(it) > 0) node.right = rotateRight(it)
        }
        return rotateLeft(node)
    }

    private fun rotateRight(y: AVLNode<K, V>): AVLNode<K, V> {
        val x = y.left ?: return y
        val t2 = x.right
        
        x.right = y
        y.left = t2
        
        updateHeight(y)
        updateHeight(x)
        
        return x
    }

    private fun rotateLeft(x: AVLNode<K, V>): AVLNode<K, V> {
        val y = x.right ?: return x
        val t2 = y.left
        
        y.left = x
        x.right = t2
        
        updateHeight(x)
        updateHeight(y)
        
        return y
    }

    private fun updateHeight(node: AVLNode<K, V>) {
        node.height = maxOf(height(node.left), height(node.right)) + 1
    }

    private fun height(node: AVLNode<K, V>?): Int = node?.height ?: 0

    private fun getBalanceFactor(node: AVLNode<K, V>) = 
        height(node.left) - height(node.right)

    private fun findMin(node: AVLNode<K, V>?): AVLNode<K, V> {
        var current = node ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left ?: break
        }
        return current
    }

    fun dfsInOrder(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        fun traverse(n: AVLNode<K, V>?) {
            n?.let {
                traverse(it.left)
                result.add(it.key to it.value)
                traverse(it.right)
            }
        }
        traverse(root)
        return result
    }

    fun dfsPreOrder(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        fun traverse(n: AVLNode<K, V>?) {
            n?.let {
                result.add(it.key to it.value)
                traverse(it.left)
                traverse(it.right)
            }
        }
        traverse(root)
        return result
    }

    fun dfsPostOrder(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        fun traverse(n: AVLNode<K, V>?) {
            n?.let {
                traverse(it.left)
                traverse(it.right)
                result.add(it.key to it.value)
            }
        }
        traverse(root)
        return result
    }
}