package com.jyotimoykashyap.circularstatswidget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

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
    progressTextColor: Color = MaterialTheme.colors.onSecondary
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

    // calculate sweep angle
    val sweepAngle by animateFloatAsState(
        targetValue = (3.6*percentage).toFloat(),
        animationSpec = tween(1000)
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
            .size(canvasSize)
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

@Composable
@Preview(showBackground = true)
fun CircularStatsPreview(){
    CircularStats()
}
