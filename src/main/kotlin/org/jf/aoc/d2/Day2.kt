package org.jf.aoc.d2

import org.jf.aoc.utils.readFile

fun main() {
    val input = "d2.txt".readFile()
    oneStar(input)
    twoStar(input)
}

fun twoStar(input: String) {
    val result = input.lines()
        .map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }
        .count { it.isSafe() }
    println(result)
}

fun oneStar(input: String) {
    val result = input.lines()
        .map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }
        .count { it.hasSmallEnoughDifferences() || it.reversed().hasSmallEnoughDifferences() }

    println(result)
}

fun List<Int>.hasSmallEnoughDifferences(): Boolean {
    return this.asSequence().zipWithNext { a, b -> (b - a) in 1..3 }.all { it }
}

fun List<Int>.almostHasSmallEnoughDifferences(): Boolean {
    for ((index, i) in this.withIndex()) {
        if (index + 1 < this.size) {
            val diff = this[index + 1] - i
            if (diff <= 0 || diff > 3) {
                return this.filterIndexed { ind, _ -> ind != index + 1 }.hasSmallEnoughDifferences() ||
                        this.filterIndexed { ind, _ -> ind != index }.hasSmallEnoughDifferences()
            }
        }
    }
    return true
}

fun List<Int>.isSafe(): Boolean {
    return this.almostHasSmallEnoughDifferences() || this.reversed().almostHasSmallEnoughDifferences()
}
