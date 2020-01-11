package lifegame

import arrow.Kind
import arrow.core.Tuple2
import arrow.extension
import arrow.typeclasses.Comonad
import arrow.typeclasses.Functor

@extension
interface FocusedGridFunctor: Functor<ForFocusedGrid> {
    override fun <A, B> Kind<ForFocusedGrid, A>.map(f: (A) -> B): Kind<ForFocusedGrid, B> {
        return FocusedGrid(
            focus = fix().focus,
            grid = fix().grid.map { it.map(f) }
        )
    }
}

@extension
interface FocusedGridComonad: Comonad<ForFocusedGrid> {
    override fun <A, B> Kind<ForFocusedGrid, A>.coflatMap(f: (Kind<ForFocusedGrid, A>) -> B): Kind<ForFocusedGrid, B> {
        return FocusedGrid(
            focus = fix().focus,
            grid = fix().grid.mapIndexed { index1, list ->
                list.mapIndexed { index2, a ->
                    f(FocusedGrid(
                        focus = Tuple2(index1, index2),
                        grid = fix().grid
                    ))
                }
            }
        )
    }

    override fun <A> Kind<ForFocusedGrid, A>.extract(): A {
        return fix().subscript(fix().focus)
    }

    override fun <A, B> Kind<ForFocusedGrid, A>.map(f: (A) -> B): Kind<ForFocusedGrid, B> {
        TODO() // Not Needed, use the implementation inside Functor
    }
}

// TypeClass definition

class ForFocusedGrid private constructor()  {
    // Container - F
}

data class FocusedGrid<A>(
    val focus: Tuple2<Int, Int>,
    val grid: List<List<A>>
) : Kind<ForFocusedGrid, A> {
    companion object {}

    fun subscript(at: Tuple2<Int, Int>): A = grid[at.a][at.b]

    fun subscript(x: Int, y: Int): A {
        val x = (x + grid.count()) % grid.count()
        val y = (y + grid[x].count()) % grid[x].count()
        return grid[x][y]
    }
}

fun <A> Kind<ForFocusedGrid, A>.fix() = this as FocusedGrid<A>