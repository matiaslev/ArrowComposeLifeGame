package com.matiaslev.arrowcomposegameoflife

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.lifecycle.Observer
import androidx.ui.core.Alignment
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.ExpandedWidth
import androidx.ui.layout.Row
import androidx.ui.layout.Spacing
import androidx.ui.layout.Stack
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TextButtonStyle
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
        Stack(modifier = ExpandedWidth) {
            aligned(Alignment.Center) {
                Grid(store)
            }
            aligned(Alignment.BottomCenter) {
                Buttons(store)
            }
        }
    }

    @Composable
    fun listPatterns(store: Store) {
        Column {
            Pattern.all().forEach {
                Button(text = "${it::class.java.canonicalName}",
                    style = TextButtonStyle(),
                    onClick = { store.send(ConwayAction.select(it)) }
                )
            }
        }
    }

    @Composable
    fun Grid(store: Store) {
        Column {
            store.grid.forEachIndexed { index1, list ->
                Row {
                    list.forEachIndexed { index2, cell ->
                        Text(store.grid[index2][index1].description())
                    }
                }
            }
        }
    }

    @Composable
    fun Buttons(store: Store) {
        Row(arrangement = Arrangement.SpaceEvenly) {
            Button(
                text = "Pattern",
                modifier = Spacing(10.dp),
                style = TextButtonStyle(),
                onClick = { Content(Screen.PickPattern, store) }
            )

            if (store.isRunning.not()) {
                Button(text = "Start",
                    modifier = Spacing(10.dp),
                    style = TextButtonStyle(),
                    onClick =
                    {
                        store.isRunning = true
                        nextStep(store)
                    }
                )
            } else {
                Button(text = "Stop",
                    modifier = Spacing(10.dp),
                    style = TextButtonStyle(),
                    onClick = { store.isRunning = false }
                )
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


