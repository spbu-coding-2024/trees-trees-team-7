open class Node<K : Comparable<K>, V, N : Node<K, V, N>>(
    var key: K,
    var value: V
) {
    var left: N? = null
    var right: N? = null
}