package lifegame

sealed class Cell {
    object alive: Cell()
    object dead: Cell()
}

fun Cell.description(): String =
    when(this) {
        is Cell.alive -> "🚀"
        is Cell.dead -> "💀"
    }

