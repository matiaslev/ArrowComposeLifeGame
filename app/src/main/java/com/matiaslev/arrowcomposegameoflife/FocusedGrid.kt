package com.matiaslev.arrowcomposegameoflife

import arrow.Kind
import arrow.core.Tuple2
import arrow.typeclasses.Comonad
import arrow.typeclasses.Functor

class ForFocusedGrid<F>: Functor<ForFocusedGrid<F>>, Comonad<ForFocusedGrid<F>> {
    override fun <A, B> Kind<ForFocusedGrid<F>, A>.map(f: (A) -> B): Kind<ForFocusedGrid<F>, B> {
         return FocusedGrid(
            focus = rara(this).focus,
            grid = rara(this).grid.map { it.map(f) }
        )
    }

    /**
    It has two parameters: an initial FocusedGrid<A> and a function (FocusedGrid<A>) -> B, and it needs to provide a FocusedGrid<B>.

    If we pay closer attention to the function f, we can see that it provides a single value that makes
    sense for corresponding with the focus of the grid. Therefore, if we know how to get one item of the resulting
    FocusedGrid for the focused position, it seems we need a way of having a grid where each position contains the current grid but
    focused on the position it occupies. That is, we duplicate the structure of the grid, where position (x, y) of the grid contains
    the original grid, but focused on (x, y), and from there, we can map the function f, to get each value in its right position.
    */
    override fun <A, B> Kind<ForFocusedGrid<F>, A>.coflatMap(f: (Kind<ForFocusedGrid<F>, A>) -> B): Kind<ForFocusedGrid<F>, B> {
        return FocusedGrid(
            focus = rara(this).focus,
            grid = rara(this).grid.mapIndexed { index1, list ->
                list.mapIndexed { index2, a ->
                    f(FocusedGrid(
                        focus = Tuple2(index1, index2),
                        grid = rara(this).grid
                    ))
                }
            }
        )
    }

    override fun <A> Kind<ForFocusedGrid<F>, A>.extract(): A {
        return rara(this).subscript(rara(this).focus)
    }
}

typealias FocusedGridOf<F, A> = Kind<ForFocusedGrid<F>, A>

// avoid fake FocusedGrid for call fix on FocusedGridOf
fun <F, A> rara(value: FocusedGridOf<F, A>): FocusedGrid<F, A> {
    return FocusedGrid<F, A>(Tuple2(1, 2), listOf(emptyList())).fix(value)
}

class FocusedGrid<F, A>(
    val focus: Tuple2<Int, Int>,
    val grid: List<List<A>>
) : FocusedGridOf<F, A> {

    fun fix(value: FocusedGridOf<F, A>) = value as FocusedGrid<F, A>

    fun subscript(at: Tuple2<Int, Int>): A = grid[at.a][at.b]

    fun subscript(x: Int, y: Int): A {
        // % x
        // % y
        return grid[x][y]
    }
}

// fun <F, A> FocusedGrid<F, A>.fix(value: FocusedGridOf<F, A>) = value as FocusedGrid<F, A>