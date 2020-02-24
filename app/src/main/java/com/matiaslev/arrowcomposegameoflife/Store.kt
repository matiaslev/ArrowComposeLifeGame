package com.matiaslev.arrowcomposegameoflife

import androidx.lifecycle.MutableLiveData
import lifegame.Cell
import lifegame.ConwayAction
import lifegame.Pattern
import lifegame.conwayReducer

class Store(var grid: List<List<Cell>> = Pattern.blinker.grid()) {

    val observable = MutableLiveData<Store>()
    var isRunning = false

    fun send(action: ConwayAction) {
        grid = conwayReducer(grid, action)
        observable.value = this
    }
}