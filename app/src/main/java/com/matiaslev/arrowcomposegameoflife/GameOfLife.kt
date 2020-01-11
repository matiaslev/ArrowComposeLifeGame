package com.matiaslev.arrowcomposegameoflife

import arrow.core.Tuple2
import arrow.core.Tuple3
import arrow.typeclasses.Monoid

// monoid

//fun <F, A: Monoid<A>> FocusedGrid<F, A>.localSum(): A {
//    val coefficients = Tuple2(arrayOf(-1, 0, 1), arrayOf(-1, 0, 1))
//    return coefficients.zip {
//
//    }
//}

// game of life

fun conwayStep(grid: FocusedGridOf<Int>): Int {
    val liveNeighbors = // grid^.localSum()
    val cell = // grid.extract()

    if cell == 1 {
        return (liveNeighbors >= 2 && liveNeighbors <= 3) ? 1 : 0
    } else {
        return (liveNeighbors == 3) ? 1 : 0
    }
}

//conwayStep

// cellToInt

// intToCell