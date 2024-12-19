package org.jf.aoc.d9

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day9KtTest {

    @Test
    fun dd() {
        val expected = listOf(1, 1, 1, 2, 3, 4, 5, 2, 2, 2, 7, 8, 8, 9, 9, 9, 9, 9)
        val result = ArrayList(listOf(1, 1, 1, 2, 3, 4, 5, 2, 2, 2, 7, 8, 8, 9, 9, 9, 9, 9))
        result.replaceFirstBlock(
            3,
            2,
            6,
            Integer.MAX_VALUE
        )
        Assertions.assertEquals(expected, result)
    }
}
