package lifegame

import arrow.core.Tuple2

sealed class Pattern(val positions: List<Tuple2<Int, Int>>) {
    object blinker: Pattern(listOf(Tuple2(5, 4), Tuple2(5, 5), Tuple2(5, 6)))
    object beacon: Pattern(listOf(Tuple2(4, 4), Tuple2(4, 5), Tuple2(5, 4), Tuple2(6, 7), Tuple2(7, 6), Tuple2(7, 7)))
    object glider: Pattern(listOf(Tuple2(4, 4), Tuple2(4, 5), Tuple2(4, 6), Tuple2(5, 4), Tuple2(6, 5)))
    object toad: Pattern(listOf(Tuple2(4,5), Tuple2(4,6), Tuple2(4, 7), Tuple2(5,4), Tuple2(5,5), Tuple2(5, 6)))
    object diehard: Pattern(listOf(Tuple2(4, 7), Tuple2(5, 1), Tuple2(5, 2), Tuple2(6, 2), Tuple2(6 ,6), Tuple2(6,7), Tuple2(6, 8)))
    object acorn: Pattern(listOf(Tuple2(4,2), Tuple2(5,4), Tuple2(6,1), Tuple2(6,2), Tuple2(6, 5), Tuple2(6,6), Tuple2(6, 7)))
    object pentomino: Pattern(listOf(Tuple2(4,5), Tuple2(4,6), Tuple2(5,4), Tuple2(5, 5), Tuple2(6,5)))

    fun grid(): List<List<Cell>> {
        var initials: MutableList<MutableList<Cell>> = deadCells()
        val positions = this.positions
        positions.forEach { initials[it.a][it.b] = Cell.alive }
        return initials
    }
    private fun Pattern.deadCells(): MutableList<MutableList<Cell>> = MutableList(10) { MutableList(10) { Cell.dead as Cell } }

    companion object {
        fun all() = listOf(blinker, beacon, glider, toad, diehard, acorn, pentomino)
    }
}
