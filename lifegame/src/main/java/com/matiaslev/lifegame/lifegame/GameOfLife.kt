package lifegame

import arrow.core.Tuple2
import arrow.core.extensions.list.apply.product
import arrow.core.extensions.list.foldable.combineAll
import arrow.core.extensions.monoid
import arrow.core.k
import lifegame.focusedgrid.comonad.coflatMap
import lifegame.focusedgrid.comonad.extract
import lifegame.focusedgrid.functor.map

fun FocusedGrid<Int>.localSum(): Int {
    val coefficients = listOf(-1, 0, 1).k()
    return coefficients.product(coefficients)
        .filter { it.a != 0 || it.b != 0 }
        .map { this.subscript(this.focus.a + it.a, this.focus.b + it.b) }
        .combineAll(Int.monoid())
}

fun gameOfLife(state: List<List<Cell>>): List<List<Cell>> {
    return FocusedGrid(focus = Tuple2(0,0), grid = state)
        .map { cellToInt(it) }
        .coflatMap { conwayStep(it.fix()) }
        .map { intToCell(it) }
        .grid
}

fun conwayStep(grid: FocusedGrid<Int>): Int {
    val liveNeighbors = grid.localSum()
    val cell = grid.extract()

    return if (cell == 1) {
        if (liveNeighbors in 2..3) 1 else 0
    } else {
        if (liveNeighbors == 3) 1 else 0
    }
}

private fun cellToInt(cell: Cell): Int = when(cell) {
    is Cell.dead -> 0
    is Cell.alive -> 1
}

private fun intToCell(int: Int): Cell = if(int == 0) Cell.dead else Cell.alive