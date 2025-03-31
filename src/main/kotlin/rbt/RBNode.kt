package rbt

import Node

class RBNode<K : Comparable<K>, V>(
    key: K,
    value: V
) : Node<K, V>(key, value) {
    var color: Color = Color.RED
    var parent: RBNode<K, V>? = null

    fun left(): RBNode<K, V>? = super.left as? RBNode<K, V>
    fun right(): RBNode<K, V>? = super.right as? RBNode<K, V>

    fun setLeft(node: RBNode<K, V>?) {
        super.left = node
        if (node != null) {
            node.parent = this
        }
    }

    fun setRight(node: RBNode<K, V>?) {
        super.right = node
        if (node != null) {
            node.parent = this
        }
    }
}