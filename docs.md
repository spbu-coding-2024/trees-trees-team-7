## Documentation

Each of the three types of trees can include a key value **pair** or accept nothing (i.e., be an empty tree).

## List of methods 

**Binary Search Tree**


`delete(key: K)` - deletes an item by key number.

`insert(key: K, value: V)` - allows you to insert an object by taking the key + value.

`iteration(): List<Pair<K, V>>` - allows you to integrate the structure (the method depends on the tree).

`contains(key: K): Boolean` - checks whether an object is contained in the tree by key (only for BST).

`search(key: K): V?` - performs a recursive tree search.

**AVL Tree**

1. Public methods (Tree interface):
`insert(key: K, value: V)` - Inserts a key-value pair into the tree. Automatically rebalances the tree after insertion.

`search(key: K): V?` - Returns a value by key. If the key is not found, returns null.

`delete(key: K)` - Deletes the node with the specified key. Throws NoSuchElementException if the key does not exist.

`iteration(): List<Pair<K, V>>` - Returns a list of key-value pairs in in-order traversal.


2. Internal methods:
`private fun insert(node: AVLNode<K, V>?, key: K, value: V): AVLNode<K, V>?` - Recursively inserts a key into the subtree rooted at node. Returns the new root of the subtree after rebalancing.

`private fun searchNode(key: K): AVLNode<K, V>?` - Searches for a node by key. Uses an iterative approach to traverse the tree.

`fun getRoot(): AVLNode<K, V>?` - Returns the root node of the tree (for testing/debugging).


3. Deleting and balancing:
`private fun delete(node: AVLNode<K, V>?, key: K): AVLNode<K, V>?` - Recursively deletes a key from a subtree. Returns the new root of the subtree after balancing.

`private fun balance(node: AVLNode<K, V>): AVLNode<K, V>` - Checks the balance factor of the node and calls the appropriate balancing methods:
balanceRightHeavy if the balance factor is 2, balanceLeftHeavy if the balance factor is -2.


4. Rotations:
`private fun rotateRight(y: AVLNode<K, V>): AVLNode<K, V>` - Performs a right rotation around node y.


5. Helper methods:
`private fun updateHeight(node: AVLNode<K, V>)` - Updates the height of a node based on the heights of its children

`private fun height(node: AVLNode<K, V>?): Int` - Returns the height of a node. If the node is null, returns 0.

`private fun getBalanceFactor(node: AVLNode<K, V>): Int` - Calculates the balance factor: left.height - right.height.

`private fun findMin(node: AVLNode<K, V>?): AVLNode<K, V>` - Finds the node with the minimum key in a subtree. Used when deleting a node with two children.


6. Tree traversals:
`fun dfsInOrder(): List<Pair<K, V>>` - Returns nodes in order left→root→right.

`fun dfsPreOrder(): List<Pair<K, V>>` - Returns nodes in order root → left → right.

`fun dfsPostOrder(): List<Pair<K, V>>` - Returns nodes in order left → right → root.


**Red Black Tree**

1. Core Public Methods:
`insert(key: K, value: V)`- Inserts a key-value pair into the tree

`search(key: K): V?` - Searches for a value by key

`delete(key: K)` - Removes the node with specified key

`iteration(): List<Pair<K, V>>` - Returns all key-value pairs in ascending order (in-order traversal)

2. Internal Balancing Operations
`fixAfterInsertion(node: RBNode<K, V>)` - Restores Red-Black properties after insertion

`fixAfterDeletion(node: RBNode<K, V>?)` - Rebalances tree after node removal

3. Rotation Helpers

`rotateLeft(node: RBNode<K, V>)` - Performs left rotation around specified node

`rotateRight(node: RBNode<K, V>)` - Performs right rotation around specified node

`searchNode(key: K): RBNode<K, V>?` - Internal method for node-level search. Returns RBNode object instead of just value

`transplant(u: RBNode<K, V>, v: RBNode<K, V>?)` - Replaces subtree at node u with subtree at node v

`minimum(node: RBNode<K, V>): RBNode<K, V>` - Finds minimum node in a subtree

`inOrderTraversal(node: RBNode<K, V>?, result: MutableList<Pair<K, V>>)` - Iterative in-order traversal implementation

`deleteNode(z: RBNode<K, V>)` - Internal node removal logic
