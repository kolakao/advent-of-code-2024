package org.jf.aoc.d4

import org.jf.aoc.utils.readFile
import java.util.Collections

fun main() {
    val input = "d4.txt".readFile()
    oneStar(input)
    twoStar(input)

}


fun oneStar(input: String) {
    val inputMatrix = input.lines().map { l -> l.toCharArray().toList() }
    val rotation = inputMatrix
        .mapIndexed { i, l -> l.toList().also { ll -> Collections.rotate(ll, i) } }
        .toList()
        .transpose()

    val rotation2 = inputMatrix
        .mapIndexed { i, l -> l.toList().also { ll -> Collections.rotate(ll, -i) } }
        .toList()
        .transpose()
    val inputTransposed = inputMatrix.transpose()
    val diagonal = rotation.mapIndexed { i, l -> l.take(i + 1) } + (rotation
        .mapIndexed { i, l -> l.takeLast(l.size - (i + 1)).reversed() })
    val diagonal2 = (rotation2.mapIndexed { i, l -> l.take(rotation2.size - i) }.reversed() + (rotation2.reversed()
        .mapIndexed { i, l -> l.takeLast(l.size - (i + 1)) }))
    val result = inputMatrix.countXmas() + inputTransposed.countXmas() + diagonal.countXmas() + diagonal2.countXmas()
    println(result)

    println("INPUT: ${inputMatrix.countXmas()}")
    println("INPUT TRANSPOSED: ${inputTransposed.countXmas()}")
    println("ROTATION: ${diagonal.countXmas()}")
    println("ROTATION2: ${diagonal2.countXmas()}")

}

fun twoStar(input: String) {
    val inputMatrix = input.lines().map { l -> l.toCharArray().toList() }
    var counter = 0
    for ((index, chars) in inputMatrix.withIndex()) {
        for ((index1, c) in chars.withIndex()) {
            if (c == 'A' && isXmas(index, index1, inputMatrix)) {
                counter++
            }
        }
    }
    println("XMAS NUMBER: $counter")
}


fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (isEmpty() || this[0].isEmpty()) return this
    val width = this[0].size
    return (0 until width).map { i ->
        this.map { it[i] }
    }
}

fun List<List<Char>>.countXmas(): Int {
    return this.map { l ->
        val str = buildString { for (c in l) append(c) }
        (str.split("XMAS").size - 1) + (str.split("SAMX").size - 1)
    }
        .reduce(Integer::sum)
}

fun isXmas(i: Int, j: Int, matrix: List<List<Char>>): Boolean {
    if (i < 1 || i > matrix.size - 2 || j < 1 || j > matrix.size - 2) {
        return false
    } else {
        val leftTop = matrix[i - 1][j - 1]
        val rightTop = matrix[i - 1][j + 1]
        val leftBot = matrix[i + 1][j - 1]
        val rightBot = matrix[i + 1][j + 1]
        val neighbours = listOf(leftTop, rightTop, leftBot, rightBot)
        return neighbours.count { c -> c == 'M' } == 2 && neighbours.count { c -> c == 'S' } == 2 && leftTop != rightBot
    }
}