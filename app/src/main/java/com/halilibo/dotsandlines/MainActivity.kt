package com.halilibo.dotsandlines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.halilibo.dotsandlines.ui.DotsAndLinesTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DotsAndLinesTheme {
                var dotsAndLinesConfig by remember { mutableStateOf(DotsAndLinesConfig()) }
                Scaffold(
                    drawerElevation = 0.dp,
                    drawerBackgroundColor = Color.Transparent,
                    drawerContent = {
                        DotsAndLinesSliderRow(
                            title = "Connectivity",
                            value = dotsAndLinesConfig.threshold,
                            valueRange = 0f..0.2f,
                            onValueChanged = {
                                dotsAndLinesConfig = dotsAndLinesConfig.copy(threshold = it)
                            }
                        )
                        DotsAndLinesSliderRow(
                            title = "Line Thickness",
                            value = dotsAndLinesConfig.maxThickness,
                            valueRange = 2f..20f,
                            onValueChanged = {
                                dotsAndLinesConfig = dotsAndLinesConfig.copy(maxThickness = it)
                            }
                        )
                        DotsAndLinesSliderRow(
                            title = "Dot Size",
                            value = dotsAndLinesConfig.dotRadius,
                            valueRange = 2f..20f,
                            onValueChanged = {
                                dotsAndLinesConfig = dotsAndLinesConfig.copy(dotRadius = it)
                            }
                        )
                        DotsAndLinesSliderRow(
                            title = "Speed",
                            value = dotsAndLinesConfig.speedCoefficient,
                            valueRange = 0.1f..20f,
                            onValueChanged = {
                                dotsAndLinesConfig = dotsAndLinesConfig.copy(speedCoefficient = it)
                            }
                        )
                        DotsAndLinesSliderRow(
                            title = "Density",
                            value = dotsAndLinesConfig.population,
                            valueRange = 0.2f..5f,
                            onValueChanged = {
                                dotsAndLinesConfig = dotsAndLinesConfig.copy(population = it)
                            }
                        )
                    }
                ) {
                    with(dotsAndLinesConfig) {
                        Box(modifier = Modifier.padding(it).fillMaxSize().background(Color.Black)) {
                            Box(
                                modifier = Modifier.fillMaxSize(0.5f).align(Alignment.TopStart)
                                    .padding(8.dp).background(Color.Black)
                                    .border(2.dp, Color.White)
                                    .dotsAndLines(
                                        threshold = threshold,
                                        maxThickness = maxThickness,
                                        dotRadius = dotRadius,
                                        speed = speedCoefficient,
                                        populationFactor = population
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(0.5f).align(Alignment.BottomStart)
                                    .padding(8.dp).background(Color.Black)
                                    .border(2.dp, Color.White)
                                    .dotsAndLines(
                                        threshold = threshold,
                                        maxThickness = maxThickness,
                                        dotRadius = dotRadius,
                                        speed = speedCoefficient,
                                        populationFactor = population
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(0.5f).align(Alignment.TopEnd)
                                    .padding(8.dp).background(Color.Black)
                                    .border(2.dp, Color.White)
                                    .dotsAndLines(
                                        threshold = threshold,
                                        maxThickness = maxThickness,
                                        dotRadius = dotRadius,
                                        speed = speedCoefficient,
                                        populationFactor = population
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(0.5f).align(Alignment.BottomEnd)
                                    .padding(8.dp).background(Color.Black)
                                    .border(2.dp, Color.White)
                                    .dotsAndLines(
                                        threshold = threshold,
                                        maxThickness = maxThickness,
                                        dotRadius = dotRadius,
                                        speed = speedCoefficient,
                                        populationFactor = population
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DotsAndLinesSliderRow(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp)
    ) {
        Column {
            Text("$title: $value")
            Slider(
                value = value,
                valueRange = valueRange,
                onValueChange = onValueChanged
            )
        }
    }
}

data class DotsAndLinesConfig(
    val threshold: Float = 0.1f,
    val maxThickness: Float = 6f,
    val dotRadius: Float = 4f,
    val speedCoefficient: Float = 1f,
    val population: Float = 1f // per 100^2 pixels
)