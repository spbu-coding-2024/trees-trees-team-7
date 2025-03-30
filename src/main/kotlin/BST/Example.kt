package BST

fun main() {
    val tree = BinarySearchTree<Int, String>()

    tree.insert(4, "D")
    tree.insert(2, "B")
    tree.insert(6, "F")
    tree.insert(1, "A")
    tree.insert(3, "C")
    tree.insert(5, "E")
    tree.insert(7, "G")
    println(tree.iteration()) // Выведет [(4, D), (2, B), (6, F), (1, A), (3, C), (5, E), (7, G)]

    tree.delete(1)
    println(tree.iteration()) // Выведет [(4, D), (2, B), (6, F), (3, C), (5, E), (7, G)]

    tree.insert(1, "Hmm")
    println(tree.iteration()) // Выведет [(4, D), (2, B), (6, F), (1, Hmm), (3, C), (5, E), (7, G)]


}
