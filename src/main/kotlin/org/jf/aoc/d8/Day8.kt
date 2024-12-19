package org.jf.aoc.d8

import org.jf.aoc.utils.elementPairs
import org.jf.aoc.utils.isInBoundaries
import org.jf.aoc.utils.readFile
import java.util.ArrayList
import kotlin.collections.HashMap

fun main() {
    val input = "d8.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    println(numberOfAntinodes(input))

}

fun secondStar(input: String) {
    println(numberOfAntinodes(input, unlimitedAntinodes = true))
}

fun numberOfAntinodes(input: String, unlimitedAntinodes: Boolean = false): Int {
    val nodesMatrix = input
        .lines()
        .mapIndexed { i, l ->
            l.toList()
                .mapIndexed { j, c -> Node(i, j, c) }
        }
    val antennas = antennas(nodesMatrix)
    val antinodes =
        antinodes(
            antennas,
            nodesMatrix.size to nodesMatrix[0].size,
            unlimitedAntinodes = unlimitedAntinodes
        ).map { it.second }
            .filter { (it.i < nodesMatrix.size && it.i >= 0) && (it.j < nodesMatrix[0].size && it.j >= 0) }
            .distinct()
    return antinodes.count()
}

fun antennas(nodes: List<List<Node>>): List<Pair<Node, Node>> {
    val map = HashMap<Char, ArrayList<Node>>()
    nodes.flatten().forEach {
        if (it.symbol.isLetterOrDigit()) {
            map.computeIfAbsent(it.symbol) { _ ->
                val list = ArrayList<Node>()
                list.add(it)
                list
            }.add(it)
        }
    }
    return map.values.map { it.elementPairs().toList() }.flatten()
}

fun antinodes(
    antennas: List<Pair<Node, Node>>,
    boundaries: Pair<Int, Int>,
    unlimitedAntinodes: Boolean = false
): List<Pair<Pair<Node, Node>, Node>> {
    val antinodes = ArrayList<Pair<Pair<Node, Node>, Node>>()
    antennas.forEach {
        if (it.first != it.second) {
            val (i, j) = it.let { Pair(it.first.i - it.second.i, it.first.j - it.second.j) }
            if (unlimitedAntinodes) {
                var (offsetI, offsetJ) = 0 to 0
                while (((it.first.i + (offsetI * i))
                            to
                            (it.first.j + (offsetJ * j)))
                        .isInBoundaries(boundaries)
                ) {
                    antinodes.add(
                        (it.first to it.second) to Node(
                            it.first.i + (i * offsetI),
                            it.first.j + (j * offsetJ),
                            '#'
                        )
                    )
                    offsetI++
                    offsetJ++
                }
                offsetI = 0
                offsetJ = 0
                while (((it.second.i - (offsetI * i))
                            to
                            (it.second.j - (offsetJ * j))).isInBoundaries(boundaries)
                ) {
                    antinodes.add(
                        (it.first to it.second) to Node(
                            it.second.i - (i * offsetI),
                            it.second.j - (j * offsetJ),
                            '#'
                        )
                    )
                    offsetI++
                    offsetJ++
                }

            } else {
                antinodes.add((it.first to it.second) to Node(it.first.i + i, it.first.j + j, '#'))
                antinodes.add((it.first to it.second) to Node(it.second.i - i, it.second.j - j, '#'))
            }
        }
    }
    return antinodes
}

data class Node(val i: Int, val j: Int, val symbol: Char)

