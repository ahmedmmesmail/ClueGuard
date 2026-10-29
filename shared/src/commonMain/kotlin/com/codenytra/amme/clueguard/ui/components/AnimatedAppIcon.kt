package com.codenytra.amme.clueguard.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import clueguard.shared.generated.resources.Res
import clueguard.shared.generated.resources.app_icon_desc
import clueguard.shared.generated.resources.icon
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnimatedAppIcon(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val infinite = rememberInfiniteTransition(label = "icon")

    val primary = Color(0xFFFF2D42)
    val secondary = Color(0xFF98111E)

    val floatY by infinite.animateFloat(
        initialValue = 0f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "float",
    )
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse",
    )
    val glow by infinite.animateFloat(
        initialValue = 0.20f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow",
    )
    // حركة التدرج: 0 → 1 وترجع
    val gradientShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2500, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "gradientShift",
    )

    val entrance = remember { Animatable(0f) }
    val tapRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        entrance.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        )
    }

    Box(
        modifier = modifier
            .size(170.dp)
            .graphicsLayer {
                translationY = floatY.dp.toPx()
                scaleX = entrance.value
                scaleY = entrance.value
                alpha = entrance.value.coerceIn(0f, 1f)
            }
            .drawBehind {
                // توهّج ناعم ورا الأيقونة
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = glow), Color.Transparent),
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.minDimension / 2,
                    ),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(108.dp)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                    rotationZ = tapRotation.value
                }
                .clip(RoundedCornerShape(28.dp))
                .drawBehind {
                    val shift = gradientShift * size.width
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(primary, secondary),
                            start = Offset(shift, 0f),
                            end = Offset(shift + size.width, size.height),
                            tileMode = TileMode.Mirror,
                        )
                    )
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    scope.launch {
                        tapRotation.animateTo(
                            tapRotation.value + 360f,
                            tween(700, easing = FastOutSlowInEasing)
                        )
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.icon),
                contentDescription = stringResource(Res.string.app_icon_desc),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}