package org.jf.aoc.d12

import org.jf.aoc.utils.readFile

private val ZERO_PAIR = 0 to 0

fun main() {
    val input = "d12.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val garden = input.lines().map { l -> l.toList() }
    val visited = garden.map { List(garden[0].size) { _ -> false }.toMutableList() }
    val areaPerimeters = arrayListOf<Pair<Int, Int>>()

    for (i in garden.indices) {
        for (j in garden[0].indices) {
            if (!visited[i][j]) {
                areaPerimeters.add(calculateParams(i, j, garden, visited))
            }
        }
    }

    val result = areaPerimeters.map { it.first * it.second }.reduce(Integer::sum)

    println(areaPerimeters)
    println(result)
}

fun calculateParams(i: Int, j: Int, garden: List<List<Char>>, visited: List<MutableList<Boolean>>): Pair<Int, Int> {
    if (visited[i][j]) {
        return ZERO_PAIR
    }
    visited[i][j] = true
    val char = garden[i][j]

    var perimeter = 4
    val down = if (i + 1 < garden.size && garden[i + 1][j] == char) {
        perimeter--
        calculateParams(i + 1, j, garden, visited)
    } else ZERO_PAIR
    val up = if (i - 1 >= 0 && garden[i - 1][j] == char) {
        perimeter--
        calculateParams(i - 1, j, garden, visited)
    } else ZERO_PAIR
    val right = if (j + 1 < garden[0].size && garden[i][j + 1] == char) {
        perimeter--
        calculateParams(i, j + 1, garden, visited)
    } else ZERO_PAIR
    val left = if (j - 1 >= 0 && garden[i][j - 1] == char) {
        perimeter--
        calculateParams(i, j - 1, garden, visited)
    } else ZERO_PAIR

    return listOf(
        (1 to perimeter),
        up,
        down,
        left,
        right
    ).reduce { p1, p2 -> p1.first + p2.first to p1.second + p2.second }
}


fun secondStar(input: String) {
    val garden = input.lines().map { l -> l.toList() }
    val visited = garden.map { List(garden[0].size) { _ -> false }.toMutableList() }
    val areaWalls = arrayListOf<Pair<Int, Int>>()

    for (i in garden.indices) {
        for (j in garden[0].indices) {
            if (!visited[i][j]) {
                areaWalls.add(calculateParams2(i, j, garden, visited))
            }
        }
    }

    val result = areaWalls.map { it.first * it.second }.reduce(Integer::sum)
    println(result)
}


fun calculateParams2(
    i: Int,
    j: Int,
    garden: List<List<Char>>,
    visited: List<MutableList<Boolean>>,
): Pair<Int, Int> {
    if (visited[i][j]) {
        return ZERO_PAIR
    }
    visited[i][j] = true
    val char = garden[i][j]
    var corners = 0
    val isDown = i + 1 < garden.size && garden[i + 1][j] == char
    val isUp = i - 1 >= 0 && garden[i - 1][j] == char
    val isLeft = j - 1 >= 0 && garden[i][j - 1] == char
    val isRight = j + 1 < garden[0].size && garden[i][j + 1] == char
    val down = if (isDown) {
        calculateParams2(i + 1, j, garden, visited)
    } else {
        ZERO_PAIR
    }
    val up = if (isUp) {
        calculateParams2(i - 1, j, garden, visited)
    } else {
        ZERO_PAIR
    }
    val right = if (isRight) {
        calculateParams2(i, j + 1, garden, visited)
    } else {
        ZERO_PAIR
    }
    val left = if (isLeft) {
        calculateParams2(i, j - 1, garden, visited)
    } else {
        ZERO_PAIR
    }

    if (!isUp && !isRight) {
        corners++
    }
    if (!isUp && !isLeft) {
        corners++
    }
    if (!isDown && !isRight) {
        corners++
    }
    if (!isDown && !isLeft) {
        corners++
    }
    if (isUp && isRight && garden[i - 1][j + 1] != char) {
        corners++
    }
    if (isUp && isLeft && garden[i - 1][j - 1] != char) {
        corners++
    }
    if (isDown && isRight && garden[i + 1][j + 1] != char) {
        corners++
    }
    if (isDown && isLeft && garden[i + 1][j - 1] != char) {
        corners++
    }

    return listOf(
        (1 to corners),
        up,
        down,
        left,
        right
    ).reduce { p1, p2 -> p1.first + p2.first to p1.second + p2.second }

}