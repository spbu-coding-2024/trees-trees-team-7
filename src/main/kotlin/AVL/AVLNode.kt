package AVL

class AVLNode<K : Comparable<K>, V>(
    var key: K,
    var value: V,
    var left: AVLNode<K, V>? = null,
    var right: AVLNode<K, V>? = null,
    var height: Int = 1
)
