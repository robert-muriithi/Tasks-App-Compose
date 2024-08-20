package dev.robert.tasks.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressbar(
    size: Dp = 110.dp,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.surface,
    shadowColor: Color = Color.White.copy(alpha = 0.6f),
    indicatorThickness: Dp = 10.dp,
    dataUsage: Float = 80f,
    animationDuration: Int = 1000,
) {
    var dataUsageRemember by remember {
        mutableFloatStateOf(-1f)
    }

    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(
            durationMillis = animationDuration
        ), label = ""
    )

    LaunchedEffect(Unit) {
        dataUsageRemember = dataUsage
    }

    val circleColor = MaterialTheme.colorScheme.tertiaryContainer
    Box(
        modifier = Modifier
            .size(size)
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(size)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(shadowColor, circleColor),
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2),
                    radius = this.size.height / 2
                ),
                radius = this.size.height / 2,
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            drawCircle(
                color = circleColor,
                radius = (size / 2 - indicatorThickness).toPx(),
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            val sweepAngle = (dataUsageAnimate.value) * 360 / 100
            drawArc(
                color = foregroundIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                size = Size(
                    width = (size - indicatorThickness).toPx(),
                    height = (size - indicatorThickness).toPx()
                ),
                topLeft = Offset(
                    x = (indicatorThickness / 2).toPx(),
                    y = (indicatorThickness / 2).toPx()
                )
            )
        }

        DisplayText1(
            animateNumber = dataUsageAnimate,
            dataTextStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.surface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DisplayText1(
    animateNumber: State<Float>,
    dataTextStyle: TextStyle,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = (animateNumber.value).toInt().toString() + "%",
            style = dataTextStyle,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun CircularProgressbarPreview() {
    CircularProgressbar(
        foregroundIndicatorColor = MaterialTheme.colorScheme.primary,
        shadowColor = Color.LightGray,
        indicatorThickness = 8.dp,
    )
}
