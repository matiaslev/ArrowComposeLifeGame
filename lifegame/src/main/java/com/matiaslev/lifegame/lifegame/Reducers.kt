package lifegame

sealed class ConwayAction {
    object stepSimulation: ConwayAction()
    class select(val pattern: Pattern): ConwayAction()
}

fun conwayReducer(state: List<List<Cell>>, action: ConwayAction): List<List<Cell>> = when(action) {
    is ConwayAction.stepSimulation -> gameOfLife(state)
    is ConwayAction.select -> action.pattern.grid()
}