package com.jyotimoykashyap.circularstatswidget

import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

/**
 * Dev Branch - Free to experiment
 */

@Composable
fun CircularStats(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colors
        .onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 50f,
    foregroundIndicatorColor: Color = MaterialTheme.colors.primary,
    foregroundIndicatorStrokeWidth: Float = 50f,
    labelText: String = "Progress",
    labelTextFontSize: TextUnit = 12.sp,
    labelTextColor: Color = MaterialTheme.colors.onSurface.copy(0.3f),
    progressTextFontSize: TextUnit = MaterialTheme.typography.h3.fontSize,
    progressTextColor: Color = MaterialTheme.colors.onSurface
){


    // this is the allowed indicator value
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }


    // if indicator value is more than put max
    allowedIndicatorValue = if(indicatorValue <= maxIndicatorValue){
        indicatorValue
    }else maxIndicatorValue

    var animateIndicatorValue by remember {
        mutableStateOf(0f)
    }
    
    LaunchedEffect(key1 = allowedIndicatorValue){
        animateIndicatorValue = allowedIndicatorValue.toFloat()
    }

    // calculate the percentage
    val percentage =
        (animateIndicatorValue / maxIndicatorValue) * 100


    /**
     * Assigning a state the size of the circular stat
     * so that I can animate it from one state to another
     * when I receive a maximum value of the indicator
     * for that I need to store the state in the circular state
     * variable
     */

    var circularStatState by remember {
        mutableStateOf(StatsState.NORMAL)
    }

    val scaleAnimation by animateDpAsState(
        if(circularStatState == StatsState.SCALED_UP)
             canvasSize + 25.dp
        else canvasSize,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    // calculate sweep angle
    val sweepAngle by animateFloatAsState(
        targetValue = (3.6*percentage).toFloat(),
        animationSpec = tween(1000),
        finishedListener = {
            // start another animation here
            if(indicatorValue == maxIndicatorValue){

                /**
                 * When indicator value is equal to max value
                 * then we need to make it scaled up
                 */
                circularStatState = StatsState.SCALED_UP

                /**
                 * And then after 300ms we need make it of normal size
                 * again
                 */
                Handler(Looper.getMainLooper()).postDelayed({
                    circularStatState = StatsState.NORMAL
                }, 300)
            }
        }
    )

    // calculate the progress
    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    // animate progress text color
    val animateProgressTextColor by animateColorAsState(
        targetValue =
        if(allowedIndicatorValue == 0)
            MaterialTheme.colors.onSecondary.copy(0.3f)
        else
            progressTextColor,
        animationSpec = tween(1000)
    )




    Column(
        modifier = Modifier
            .size(scaleAnimation)
            .drawBehind {
                val componentSize = size / 1.25f

                // draw the background
                backgroundArc(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth
                )

                // draw the foreground
                foregroundProgress(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth
                )

            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        EmbeddedElements(
            labelTextFontSize = labelTextFontSize,
            labelTextColor = labelTextColor,
            progressText = receivedValue,
            progressTextFontSize = progressTextFontSize,
            progressTextColor = animateProgressTextColor
        )

    }


}

fun DrawScope.foregroundProgress(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.backgroundArc(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    labelText: String = "Progress",
    labelTextFontSize: TextUnit,
    labelTextColor: Color,
    progressText: Int,
    progressTextFontSize: TextUnit,
    progressTextColor: Color
){


    Text(
        text = progressText.toString(),
        fontSize = progressTextFontSize,
        color = progressTextColor,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = labelText,
        fontSize = labelTextFontSize,
        color = labelTextColor
    )
}

/**
 * An enum class for the state of the widget
 *
 * SCALED_UP -> This state represent the scaled up version of it
 * NORMAL -> Normal state is the state for the widget to be normal size
 *
 * This states are necessary for animating the component
 * from one state to another when we receive the desired condition
 * to be true
 */
enum class StatsState{
    SCALED_UP, NORMAL
}

@Composable
@Preview(showBackground = true)
fun CircularStatsPreview(){
    CircularStats()
}
