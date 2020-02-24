package com.matiaslev.arrowcomposegameoflife

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.lifecycle.Observer
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.AdapterList
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Center
import androidx.ui.layout.Column
import androidx.ui.layout.EdgeInsets
import androidx.ui.layout.LayoutWidth
import androidx.ui.layout.Row
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TextButton
import androidx.ui.unit.dp
import lifegame.ConwayAction
import lifegame.Pattern
import lifegame.description

sealed class Screen {
    object Game : Screen()
    object PickPattern : Screen()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = Store()
        Content(Screen.Game, store)
        store.observable.observe(this, Observer { Content(Screen.Game, it) })
    }

    private fun Content(screen: Screen, store: Store) {
        setContent {
            MaterialTheme {
                when (screen) {
                    is Screen.Game -> {
                        drawGame(store)
                    }
                    is Screen.PickPattern -> {
                        listPatterns(store)
                    }
                }
            }
        }
    }

    @Composable
    private fun drawGame(store: Store) {
        Column(
            arrangement = Arrangement.Center
        ) {
            Grid(store)
            Buttons(store)
        }
    }

    @Composable
    fun listPatterns(store: Store) {
        AdapterList(
            data = Pattern.all()
        ) {
            TextButton(onClick = { store.send(ConwayAction.select(it)) }) {
                Text(text = it::class.java.simpleName)
            }
        }
    }

    @Composable
    fun Grid(store: Store) {
            store.grid.forEachIndexed { index1, list ->
                Row(
                    modifier = LayoutWidth.Fill,
                    arrangement = Arrangement.Center
                ) {
                    list.forEachIndexed { index2, cell ->
                        Text(store.grid[index1][index2].description())
                    }
                }
            }
    }

    @Composable
    fun Buttons(store: Store) {
        Row(
            modifier = LayoutWidth.Fill,
            arrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { Content(Screen.PickPattern, store) }
            ) {
                Text(text = "Pattern")
            }

            if (store.isRunning.not()) {
                TextButton(
                    onClick =
                    {
                        store.isRunning = true
                        nextStep(store)
                    }
                ) {
                    Text(text = "Start")
                }
            } else {
                TextButton(
                    onClick = { store.isRunning = false }
                ) {
                    Text(text = "Stop")
                }
            }
        }
    }

    private fun nextStep(store: Store) {
        if (store.isRunning) {
            store.send(ConwayAction.stepSimulation)
            Handler().postDelayed({ nextStep(store) }, 300)
        }
    }
}


