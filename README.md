## 📦 3 in 1 Library: BST, AVL, RBT.
The library was written by first-year students of software engineering at St. Petersburg State University: Nikitin Egor, Ivanov Daniil, Khmelev Vladimir.

## Abilities 
Our library provides an opportunity to use three structures, such as:
+ ***Binary Search Tree***
+ ***Red Black Tree***
+ ***AVL Tree***

## Local start
1. Clone the repository 
2. Go to the project directory
3.Build the project with Gradle:
```./gradlew build```
4. Running tests
```./gradlew test```

## Examples
```
import trees.avl.AVLTree
fun main() {
val avlTree = AVLTree<Int, String>()
avlTree.insert(1, "one")
avlTree.insert(2, "two")

println("${avlTree.search(2)}") // Prints "two"
}
```

```
// Dictionary
val dictionary = RBTree<String, String>().apply {
    insert("algorithm", "Набор инструкций")
    insert("binary", "Двоичный")
    insert("tree", "Дерево")
}

// Auto-sorting
dictionary.iteration().forEach { (term, definition) ->
    println("$term: $definition")
}
```

## Test coverage

- basic operations

- edge cases

- tree properties

- Stress test

## Based on 
+ JUnit5
+ Gradle 8.10
+ Kotlin 1.9.24

## Contact us
+ Egor Nikitin - t.me/insearchofparadise
+ Daniil Ivanov - t.me/desiredeternityyy
+ Vladimir Khmelev - t.me/khmelevvova

## DOCS

The [documentation](https://github.com/spbu-coding-2024/trees-trees-team-7/blob/main/docs.md) is available at the root of the repository

## LICENSE 📜

This project is licensed under the MIT license. For more information, see the LICENSE file.


