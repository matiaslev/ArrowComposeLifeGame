package com.matiaslev.arrowcomposegameoflife

import arrow.core.Tuple2
import lifegame.FocusedGrid
import org.junit.Test

class FocusedGridTest {

    val SUT = FocusedGrid<Int>(
        focus = Tuple2(0, 0),
        grid = listOf(listOf(0, 0))
    )

    @Test
    fun `subscript`() {
        SUT.subscript(-1, -1)
    }
}