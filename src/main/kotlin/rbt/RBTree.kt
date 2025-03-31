package rbt

import Tree

class RBTree<K : Comparable<K>, V> : Tree<K, V> {
    private var root: RBNode<K, V>? = null

    override fun insert(key: K, value: V) {
        val newNode = RBNode(key, value)
        var parent: RBNode<K, V>? = null
        var current = root

        while (current != null) {
            parent = current
            current = when {
                key < current.key -> current.left()
                key > current.key -> current.right()
                else -> {
                    current.value = value
                    return
                }
            }
        }

        newNode.parent = parent

        when {
            parent == null -> root = newNode
            key < parent.key -> parent.setLeft(newNode)
            else -> parent.setRight(newNode)
        }

        fixAfterInsertion(newNode)
        root?.color = Color.BLACK
    }

    private fun fixAfterInsertion(node: RBNode<K, V>) {
        var current = node
        while (current != root && current.parent?.color == Color.RED) {
            val parent = current.parent!!
            val grandparent = parent.parent ?: break

            if (parent == grandparent.left()) {
                val uncle = grandparent.right()
                if (uncle?.color == Color.RED) {
                    parent.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    current = grandparent
                    continue
                }
                if (current == parent.right()) {
                    current = parent
                    rotateLeft(current)
                }
                parent.color = Color.BLACK
                grandparent.color = Color.RED
                rotateRight(grandparent)
            } else {
                val uncle = grandparent.left()
                if (uncle?.color == Color.RED) {
                    parent.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandparent.color = Color.RED
                    current = grandparent
                    continue
                }
                if (current == parent.left()) {
                    current = parent
                    rotateRight(current)
                }
                parent.color = Color.BLACK
                grandparent.color = Color.RED
                rotateLeft(grandparent)
            }
        }
        root?.color = Color.BLACK
    }

    private fun rotateLeft(node: RBNode<K, V>) {
        val rightChild = node.right() ?: return
        node.setRight(rightChild.left())

        rightChild.parent = node.parent
        if (node.parent == null) {
            root = rightChild
        } else if (node == node.parent?.left()) {
            node.parent?.setLeft(rightChild)
        } else {
            node.parent?.setRight(rightChild)
        }

        rightChild.setLeft(node)
        node.parent = rightChild
    }

    private fun rotateRight(node: RBNode<K, V>) {
        val leftChild = node.left() ?: return
        node.setLeft(leftChild.right())

        leftChild.parent = node.parent
        if (node.parent == null) {
            root = leftChild
        } else if (node == node.parent?.right()) {
            node.parent?.setRight(leftChild)
        } else {
            node.parent?.setLeft(leftChild)
        }

        leftChild.setRight(node)
        node.parent = leftChild
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
        root?.color = Color.BLACK
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
                if (y.parent != z) {
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

    private fun transplant(u: RBNode<K, V>, v: RBNode<K, V>?) {
        require(u !== v) { "Cannot transplant node onto itself" }
        if (v != null) require(v.parent == null || v.parent == u) { "Node already has parent" }

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

    private fun fixAfterDeletion(x: RBNode<K, V>?) {
        var current = x ?: return
        while (current != root && current.color == Color.BLACK) {
            if (current == current.parent?.left()) {
                var sibling = current.parent?.right() ?: break

                if (sibling.color == Color.RED) {
                    sibling.color = Color.BLACK
                    current.parent?.color = Color.RED
                    rotateLeft(current.parent!!)
                    sibling = current.parent?.right() ?: break
                }

                if (sibling.left()?.color != Color.RED &&
                    sibling.right()?.color != Color.RED) {
                    sibling.color = Color.RED
                    current = current.parent!!
                } else {
                    if (sibling.right()?.color != Color.RED) {
                        sibling.left()?.color = Color.BLACK
                        sibling.color = Color.RED
                        rotateRight(sibling)
                        sibling = current.parent?.right() ?: break
                    }
                    sibling.color = current.parent?.color ?: Color.BLACK
                    current.parent?.color = Color.BLACK
                    sibling.right()?.color = Color.BLACK
                    rotateLeft(current.parent!!)
                    current = root!!
                }
            } else {
                var sibling = current.parent?.left() ?: break

                if (sibling.color == Color.RED) {
                    sibling.color = Color.BLACK
                    current.parent?.color = Color.RED
                    rotateRight(current.parent!!)
                    sibling = current.parent?.left() ?: break
                }

                if (sibling.left()?.color != Color.RED &&
                    sibling.right()?.color != Color.RED) {
                    sibling.color = Color.RED
                    current = current.parent!!
                } else {
                    if (sibling.left()?.color != Color.RED) {
                        sibling.right()?.color = Color.BLACK
                        sibling.color = Color.RED
                        rotateLeft(sibling)
                        sibling = current.parent?.left() ?: break
                    }

                    sibling.color = current.parent?.color ?: Color.BLACK
                    current.parent?.color = Color.BLACK
                    sibling.left()?.color = Color.BLACK
                    rotateRight(current.parent!!)
                    current = root!!
                }
            }
        }
        current.color = Color.BLACK
    }

    override fun iteration(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        inOrderTraversal(root, result)
        return result
    }

    private fun inOrderTraversal(node: RBNode<K, V>?, result: MutableList<Pair<K, V>>) {
        val visited = mutableSetOf<RBNode<K, V>>()
        val stack = ArrayDeque<RBNode<K, V>>()
        var current = node

        while (current != null || stack.isNotEmpty()) {
            while (current != null) {
                if (!visited.add(current)) {
                    throw IllegalStateException("Cycle detected in tree traversal!")
                }
                stack.addLast(current)
                current = current.left()
            }
            current = stack.removeLast()
            result.add(current.key to current.value)
            current = current.right()
        }
    }
}
