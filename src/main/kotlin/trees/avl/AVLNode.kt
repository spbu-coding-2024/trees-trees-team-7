package trees.avl

import Node

class AVLNode<K : Comparable<K>, V>(
    key: K, 
    value: V
) : Node<K, V, AVLNode<K, V>>(key, value) {
    var height: Int = 1 
    val balanceFactor: Int
        get() = (left?.height ?: 0) - (right?.height ?: 0)
}