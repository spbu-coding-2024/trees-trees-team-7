package rbt

import Tree

class RBTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: RBNode<K, V>? = null

    override fun insert(key: K, value: V) {
        val newNode = RBNode(key, value)
        if (root == null) {
            root = newNode
        } else {
            insertNode(root!!, newNode)
        }
        fixAfterInsertion(newNode)
    }

    private fun insertNode(current: RBNode<K, V>, newNode: RBNode<K, V>) {
        when {
            newNode.key < current.key -> {
                if (current.left() == null) {
                    current.setLeft(newNode)
                } else {
                    insertNode(current.left()!!, newNode)
                }
            }
            newNode.key > current.key -> {
                if (current.right() == null) {
                    current.setRight(newNode)
                } else {
                    insertNode(current.right()!!, newNode)
                }
            }
            else -> current.value = newNode.value
        }
    }

    private fun fixAfterInsertion(node: RBNode<K, V>) {
        var current = node
        while (current != root && current.parent?.color == Color.RED) {
            val parent = current.parent!!
            val grandparent = parent.parent!!

            if (parent == grandparent.left()) {
                val uncle = grandparent.right()
                if (uncle?.color == Color.RED) {
                    parent.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    current = grandparent
                } else {
                    if (current == parent.right()) {
                        current = parent
                        rotateLeft(current)
                    }
                    parent.color = Color.BLACK
                    grandparent.color = Color.RED
                    rotateRight(grandparent)
                }
            } else {
                val uncle = grandparent.left()
                if (uncle?.color == Color.RED) {
                    parent.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    current = grandparent
                } else {
                    if (current == parent.left()) {
                        current = parent
                        rotateRight(current)
                    }
                    parent.color = Color.BLACK
                    grandparent.color = Color.RED
                    rotateLeft(grandparent)
                }
            }
        }
        root?.color = Color.BLACK
    }

    private fun rotateLeft(node: RBNode<K, V>) {
        val rightChild = node.right() ?: return
        node.setRight(rightChild.left())

        if (node.parent == null) {
            root = rightChild
        } else if (node == node.parent?.left()) {
            node.parent?.setLeft(rightChild)
        } else {
            node.parent?.setRight(rightChild)
        }

        rightChild.setLeft(node)
    }

    private fun rotateRight(node: RBNode<K, V>) {
        val leftChild = node.left() ?: return
        node.setLeft(leftChild.right())

        if (node.parent == null) {
            root = leftChild
        } else if (node == node.parent?.right()) {
            node.parent?.setRight(leftChild)
        } else {
            node.parent?.setLeft(leftChild)
        }

        leftChild.setRight(node)
    }

    override fun search(key: K): V? = searchNode(key)?.value

    private fun searchNode(key: K): RBNode<K, V>? {
        var current = root
        while (current != null) {
            current = when {
                key < current.key -> current.left()
                key > current.key -> current.right()
                else -> return current
            }
        }
        return null
    }

    override fun delete(key: K) {
        val node = searchNode(key) ?: return
        deleteNode(node)
    }

    private fun deleteNode(z: RBNode<K, V>) {
        var y = z
        var yOriginalColor = y.color
        val x: RBNode<K, V>?

        when {
            z.left() == null -> {
                x = z.right()
                transplant(z, z.right())
            }
            z.right() == null -> {
                x = z.left()
                transplant(z, z.left())
            }
            else -> {
                y = minimum(z.right()!!)
                yOriginalColor = y.color
                x = y.right()
                if (y.parent == z) {
                    x?.parent = y
                } else {
                    transplant(y, y.right())
                    y.setRight(z.right())
                    y.right()?.parent = y
                }
                transplant(z, y)
                y.setLeft(z.left())
                y.left()?.parent = y
                y.color = z.color
            }
        }

        if (yOriginalColor == Color.BLACK) {
            x?.let { fixAfterDeletion(it) }
        }
    }

    private fun fixAfterDeletion(x: RBNode<K, V>) {
        var current = x
        while (current != root && current.color == Color.BLACK) {
            when {
                current == current.parent?.left() -> {
                    var sibling = current.parent?.right()
                    if (sibling?.color == Color.RED) {
                        sibling.color = Color.BLACK
                        current.parent?.color = Color.RED
                        current.parent?.let { rotateLeft(it) }
                        sibling = current.parent?.right()
                    }
                    if (sibling?.left()?.color == Color.BLACK && sibling.right()?.color == Color.BLACK) {
                        sibling.color = Color.RED
                        current = current.parent!!
                    } else {
                        if (sibling?.right()?.color == Color.BLACK) {
                            sibling.left()?.color = Color.BLACK
                            sibling.color = Color.RED
                            sibling.right()?.let { rotateRight(it) }
                            sibling = current.parent?.right()
                        }
                        sibling?.color = current.parent?.color ?: Color.BLACK
                        current.parent?.color = Color.BLACK
                        sibling?.right()?.color = Color.BLACK
                        current.parent?.let { rotateLeft(it) }
                        current = root!!
                    }
                }
                else -> {
                    var sibling = current.parent?.left()
                    if (sibling?.color == Color.RED) {
                        sibling.color = Color.BLACK
                        current.parent?.color = Color.RED
                        current.parent?.let { rotateRight(it) }
                        sibling = current.parent?.left()
                    }
                    if (sibling?.right()?.color == Color.BLACK && sibling.left()?.color == Color.BLACK) {
                        sibling.color = Color.RED
                        current = current.parent!!
                    } else {
                        if (sibling?.left()?.color == Color.BLACK) {
                            sibling.right()?.color = Color.BLACK
                            sibling.color = Color.RED
                            sibling.left()?.let { rotateLeft(it) }
                            sibling = current.parent?.left()
                        }
                        sibling?.color = current.parent?.color ?: Color.BLACK
                        current.parent?.color = Color.BLACK
                        sibling?.left()?.color = Color.BLACK
                        current.parent?.let { rotateRight(it) }
                        current = root!!
                    }
                }
            }
        }
        current.color = Color.BLACK
    }

    private fun transplant(u: RBNode<K, V>, v: RBNode<K, V>?) {
        when {
            u.parent == null -> root = v
            u == u.parent?.left() -> u.parent?.setLeft(v)
            else -> u.parent?.setRight(v)
        }
        v?.parent = u.parent
    }

    private fun minimum(node: RBNode<K, V>): RBNode<K, V> {
        var current = node
        while (current.left() != null) {
            current = current.left()!!
        }
        return current
    }

    override fun iteration(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        inOrderTraversal(root, result)
        return result
    }

    private fun inOrderTraversal(node: RBNode<K, V>?, result: MutableList<Pair<K, V>>) {
        node?.let {
            inOrderTraversal(it.left(), result)
            result.add(it.key to it.value)
            inOrderTraversal(it.right(), result)
        }
    }
}