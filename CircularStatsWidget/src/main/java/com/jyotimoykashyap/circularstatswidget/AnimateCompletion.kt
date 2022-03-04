package com.jyotimoykashyap.circularstatswidget

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * enum classes for different type of completion animation.
 */
enum class AnimateCompletion {
    BOUNCE, CHECKMARK
}

@Composable
fun scaleAnimate(
    circularStatState: StatsState,
    canvasSize: Dp,
) : Dp {
    val scaleAnimation by animateDpAsState(
        if (circularStatState == StatsState.SCALED_UP)
            canvasSize + 25.dp
        else canvasSize,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    return scaleAnimation
}