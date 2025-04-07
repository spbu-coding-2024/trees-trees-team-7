package trees.bst

import Node

class BSTNode<K : Comparable<K>, V>(
	key: K,
	value: V
) : Node<K, V, BSTNode<K, V>>(key, value)
