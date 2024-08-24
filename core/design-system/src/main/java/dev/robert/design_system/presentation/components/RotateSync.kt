package dev.robert.design_system.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.robert.design_system.R

@Composable
fun RotatingSyncIcon(sync: Boolean, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "Sync rotation")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val animatedSync by animateFloatAsState(
        targetValue = if (sync) 1f else 0f,
        label = "Sync state"
    )

    Image(
        painter = painterResource(id = R.drawable.baseline_sync_24),
        contentDescription = "Sync icon",
        modifier = modifier.graphicsLayer(rotationZ = rotation * animatedSync)
            .size(15.dp)
    )
}
