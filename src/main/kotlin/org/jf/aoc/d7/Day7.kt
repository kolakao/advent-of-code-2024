package org.jf.aoc.d7

import org.jf.aoc.utils.readFile

fun main() {
    val input = "d7.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val result = input.lines().map { l ->
        val (sEqResult, sOperands) = l.split(":")
        val eqResult = sEqResult.toLong()
        val operands = sOperands.trim().split("\\s+".toRegex()).map { it.toLong() }
        val isTrue = isEquationTrue(
            eqResult,
            operands,
            setOfPossibleOperatorsBinary(operands)
        )
        when (isTrue) {
            true -> eqResult
            false -> 0
        }
    }
        .reduce(Long::plus)
    println(result)

}

fun setOfPossibleOperatorsBinary(operands: List<Long>): List<List<(Long, Long) -> Long>> {
    val numOfOperators = operands.size - 1
    return genBasePerm(base = 2, numOfOperators, "").map { it.toList() }.map {
        it.map {
            when (it) {
                '0' -> { a: Long, b: Long -> a + b }
                '1' -> { a: Long, b: Long -> a * b }
                else -> throw Exception("REEEE")
            }
        }
    }
}

fun secondStar(input: String) {
    val result = input.lines().map { l ->
        val (sEqResult, sOperands) = l.split(":")
        val eqResult = sEqResult.toLong()
        val operands = sOperands.trim().split("\\s+".toRegex()).map { it.toLong() }
        val isTrue = isEquationTrue(
            eqResult,
            operands,
            setOfPossibleOperatorsTernary(operands)
        )
        when (isTrue) {
            true -> eqResult
            false -> 0
        }
    }
        .reduce(Long::plus)
    println(result)
}

fun setOfPossibleOperatorsTernary(operands: List<Long>): List<List<(Long, Long) -> Long>> {
    val numOfOperators = operands.size - 1
    return genBasePerm(base = 3, numOfOperators, "").map { it.toList() }.map {
        it.map {
            when (it) {
                '0' -> { a: Long, b: Long -> a + b }
                '1' -> { a: Long, b: Long -> a * b }
                '2' -> { a: Long, b: Long -> (a.toString() + b.toString()).toLong() }
                else -> throw Exception("REEEE")
            }
        }
    }
}

fun genBasePerm(base: Int, length: Int, s: String): List<String> {
    return if (s.length == length) {
        listOf(s)
    } else {
        (0 until base).toList().map { genBasePerm(base, length, s + it.toString()) }.reduce { l1, l2 -> l1 + l2 }
    }
}

fun isEquationTrue(result: Long, operands: List<Long>, operatorsSet: List<List<(Long, Long) -> Long>>): Boolean {
    return operatorsSet.any { operators ->
        var acc = operands[0]
        for ((index, function) in operators.withIndex()) {
            acc = function(acc, operands[index + 1])
        }
        acc == result
    }
}