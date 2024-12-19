package org.jf.aoc.d9

import org.jf.aoc.utils.readFile
import java.math.BigDecimal
import java.util.ArrayList

const val SPACE = -1

fun main() {
    val input = "d9.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val decoded = decode(input)

    val toDefragment = ArrayList(decoded)
    val filesIterator = decoded.reversed().toList().filter { d -> d >= 0 }.iterator()
    var i = toDefragment.size - 1
    while (!toDefragment.isDefragmented()) {
        toDefragment.replaceFirst({ d -> d == SPACE }, filesIterator.next())
        while (toDefragment[i] < 0) {
            i--
        }
        toDefragment[i] = SPACE
        i--
    }

    val result = checksum(toDefragment)
    println(result)
}

fun secondStar(input: String) {
    val decoded = decode(input)

    val toDefragment = ArrayList(decoded)
    val filesIterator = decoded.reversed().toList().filter { d -> d >= 0 }.iterator()
    var maxIndex: Int
    while (filesIterator.hasNext()) {
        val replacement = filesIterator.next()
        val firstIndexOfReplacement = toDefragment.indexOfFirst { d -> d == replacement }
        maxIndex = firstIndexOfReplacement
        val blockSize = toDefragment.count { d -> d == replacement }
        if (toDefragment.replaceFirstBlock(
                toDefragment.count { d -> d == replacement },
                replacement,
                SPACE,
                maxIndex
            )
        ) {
            for (i in firstIndexOfReplacement..<firstIndexOfReplacement + blockSize) {
                toDefragment[i] = SPACE
            }
        }
        repeat(blockSize - 1) {
            filesIterator.next()
        }
    }

    val result = checksum(toDefragment)
    println(result)
}

private fun checksum(toDefragment: ArrayList<Int>): BigDecimal? = toDefragment
    .mapIndexed { index, c ->
        if (c > 0) {
            BigDecimal(index) * BigDecimal(c.toString())
        } else {
            BigDecimal.ZERO
        }
    }
    .reduce { i1, i2 -> i1 + i2 }

private fun decode(input: String): ArrayList<Int> {
    val decoded = ArrayList<Int>()
    var id = 0
    for ((index, c) in input.toList().withIndex()) {
        val num = Integer.parseInt(c.toString())
        if (index % 2 == 0) {
            for (i in 0 until num) {
                decoded.add(id)
            }
            id++
        } else {
            for (i in 0 until num) {
                decoded.add(SPACE)
            }
        }
    }
    return decoded
}

fun List<Int>.isDefragmented(): Boolean = this.indexOfLast { d -> d >= 0 } < this.indexOfFirst { d -> d == SPACE }

fun <T> ArrayList<T>.replaceFirst(predicate: (T) -> Boolean, replacement: T): ArrayList<T> {
    for ((index, i) in this.withIndex()) {
        if (predicate(i)) {
            this[index] = replacement
            break
        }
    }
    return this
}

fun <T> ArrayList<T>.replaceFirstBlock(count: Int, replacement: T, replaceFor: T, maxIndex: Int): Boolean {
    val startIndex = this.windowed(count).indexOfFirst { l ->
        l.all { t -> t == replaceFor }
    }
    if (startIndex == SPACE || startIndex >= maxIndex) {
        return false
    }
    for (i in startIndex..<startIndex + count) {
        this[i] = replacement
    }
    return true
}