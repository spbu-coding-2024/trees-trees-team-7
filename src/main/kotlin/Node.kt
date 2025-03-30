class Node <K : Comparable<K>, V>(var key: K, var value: V) {
    var left: Node<K, V>? = null
    var right: Node<K, V>? = null
}
