package com.halilibo.dotsandlines

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import com.halilibo.dotsandlines.Dot.Companion.distanceTo
import com.halilibo.dotsandlines.DotsAndLinesState.Companion.create
import com.halilibo.dotsandlines.DotsAndLinesState.Companion.next
import com.halilibo.dotsandlines.DotsAndLinesState.Companion.populationControl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalCoroutinesApi::class)
fun Modifier.dotsAndLines(
    contentColor: Color = Color.White,
    threshold: Float,
    maxThickness: Float,
    dotRadius: Float,
    speed: Float,
    populationFactor: Float
) = composed {
    val coroutineScope = rememberCoroutineScope()
    val dotsAndLinesStateHolder = remember {
        DotsAndLinesStateHolder(coroutineScope)
    }

    val (dotsAndLinesStateFlow, setDotsAndLinesState) = dotsAndLinesStateHolder

    val dotsAndLinesState by dotsAndLinesStateFlow.collectAsState()

    onCommit(speed) {
        setDotsAndLinesState { it.copy(speed = speed) }
    }

    onCommit(dotRadius) {
        setDotsAndLinesState { it.copy(dotRadius = dotRadius) }
    }

    onCommit(populationFactor) {
        setDotsAndLinesState { it.populationControl(populationFactor) }
    }

    onSizeChanged { size ->
        dotsAndLinesStateHolder.init(
            create(
                size = size,
                populationFactor = populationFactor,
                dotRadius = dotRadius,
                speed = speed
            )
        )
    }
    .drawBehind {
        @Suppress("NAME_SHADOWING")
        val dotsAndLinesState = dotsAndLinesState ?: return@drawBehind

        dotsAndLinesState.dots.forEach {
            drawCircle(contentColor, radius = dotRadius, center = it.position)
        }

        val realThreshold = threshold * sqrt(size.width.pow(2) + size.height.pow(2))

        dotsAndLinesState.dots.nestedForEach { first, second ->
            val distance = first distanceTo second

            if (distance <= realThreshold) {
                drawLine(
                    contentColor,
                    first.position,
                    second.position,
                    0.5f + (realThreshold - distance) * maxThickness / realThreshold
                )
            }
        }
    }
}

class DotsAndLinesStateHolder(
    private val coroutineScope: CoroutineScope
) {

    private val mutationFlow = MutableSharedFlow<(DotsAndLinesState) -> DotsAndLinesState>(
        replay = 10,
        extraBufferCapacity = 100
    )

    private val mutableStateFlow = MutableStateFlow<DotsAndLinesState?>(null)
    private val state: StateFlow<DotsAndLinesState?> = mutableStateFlow

    fun init(initialState: DotsAndLinesState) {
        if(mutableStateFlow.value != null) return

        mutableStateFlow.value = initialState

        coroutineScope.launch {
            mutationFlow.collect { mutate ->
                mutableStateFlow.value = mutableStateFlow.value?.let(mutate)
            }
        }

        coroutineScope.launch {
            while (isActive) {
                val period = 1000L / 60
                delay(period)

                mutationFlow.emit { it.next(period) }
            }
        }
    }

    operator fun component1(): StateFlow<DotsAndLinesState?> {
        return state
    }

    operator fun component2(): (((DotsAndLinesState) -> DotsAndLinesState)) -> Unit {
        return { block ->
            coroutineScope.launch {
                mutationFlow.emit(block)
            }
        }
    }

}