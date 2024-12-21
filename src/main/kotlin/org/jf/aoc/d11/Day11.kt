package org.jf.aoc.d11

import org.jf.aoc.utils.readFile
import java.math.BigDecimal

val sMap = hashMapOf<BigDecimal, HashMap<Int, BigDecimal>>()

fun main() {
    val input = "d11.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val stones = input.split("\\s+".toRegex()).map { BigDecimal(it) }
    val result = stones.map { blink(it, 0, 25) }.reduce { bd1, bd2 -> bd1.plus(bd2) }
    println(result)
}

fun secondStar(input: String) {
    val stones = input.split("\\s+".toRegex()).map { BigDecimal(it) }
    val result = stones.map { blink(it, 0, 75) }.reduce { bd1, bd2 -> bd1.plus(bd2) }
    println(result)
}

fun blink(stone: BigDecimal, i: Int, max: Int): BigDecimal {
    if (i == max) {
        return BigDecimal.ONE
    }
    if (sMap.getOrElse(stone) { hashMapOf() }.containsKey(i)) {
        return sMap[stone]!![i]!!
    } else {
        val result = transform(stone).map { blink(it, i + 1, max) }.reduce { bd1, bd2 -> bd1.plus(bd2) }
        return sMap.computeIfAbsent(stone) { hashMapOf() }
            .computeIfAbsent(i) { result }
    }
}

private fun transform(stone: BigDecimal): List<BigDecimal> {
    return if (stone.toString().length % 2 == 0) {
        val sStone = stone.toString()
        listOf(
            BigDecimal(sStone.substring(0, sStone.length / 2)),
            BigDecimal(sStone.substring(sStone.length / 2))
        )
    } else if (stone == BigDecimal.ZERO) {
        listOf(BigDecimal.ONE)
    } else listOf(stone.times(BigDecimal(2024)))
}

