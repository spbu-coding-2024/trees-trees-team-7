interface Tree<K : Comparable<K>, V> {
    fun insert(key: K, value: V)
    fun search(key: K): V?
    fun delete(key: K)
    fun iteration(): List<Pair<K, V>>
}

