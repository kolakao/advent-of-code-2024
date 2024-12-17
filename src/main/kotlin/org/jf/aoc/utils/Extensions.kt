package org.jf.aoc.utils


fun String.readFile(): String = {}::class.java.classLoader.getResource(this)?.readText().orEmpty()
    .ifEmpty { throw Exception("File '${this}' not found") }

fun <T, U> List<T>.cartesianProduct(c2: Collection<U>): List<Pair<T, U>> {
    return this.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}



fun <T> List<T>.elementPairs(): Sequence<Pair<T, T>> = sequence {
    val arr = this@elementPairs
    for (i in 0 until arr.size - 1)
        for (j in i + 1 until arr.size)
            yield(arr[i] to arr[j])
}

fun Pair<Int, Int>.isInBoundaries(boundaries: Pair<Int, Int>): Boolean {
    return (this.first >= 0 && this.first < boundaries.first) && (this.second >= 0 && this.first < boundaries.second)
}