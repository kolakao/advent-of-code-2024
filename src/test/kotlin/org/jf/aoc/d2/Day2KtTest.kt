package org.jf.aoc.d2

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class Day2KtTest {

    @Test
    fun isSafeTest() {
        assert(listOf(1, 2, 3, 4, 5, 6, 7).isSafe())
        assert(listOf(1, 4, 7, 10, 13, 16, 19).isSafe())
        assert(listOf(1, 4, 7, 10, 10, 13, 16, 19).isSafe())
        assert(listOf(1, 1, 4, 7, 10, 13, 16, 19).isSafe())
        assert(listOf(1, 4, 7, 10, 13, 16, 19, 19).isSafe())
        assert(listOf(1, 2, 3, 4, 5, 6, 7, 12).isSafe())
        assert(listOf(1, 2, 3, 4, 5, 6, 7, 1).isSafe())
        assert(listOf(1, 2, 3, 4, 15, 6, 7, 8).isSafe())
        assert(listOf(5, 2, 3, 4, 5, 6, 7).isSafe())
        assertFalse(listOf(1, 3, 2, 3, 4, 5, 6, 7, 12).isSafe())
        assertFalse(listOf(1, 2, 3, 4, 5, 6, 7, 15, 16).isSafe())
        assertFalse(listOf(1, 2, 3, 4, 5, 10, 11, 12).isSafe())
    }
}
