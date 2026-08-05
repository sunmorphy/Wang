package com.andikas.wang.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.ui.theme.errorContainerDark
import com.andikas.wang.ui.theme.errorContainerLight
import com.andikas.wang.ui.theme.errorDark
import com.andikas.wang.ui.theme.errorLight
import com.andikas.wang.ui.theme.infoContainerDark
import com.andikas.wang.ui.theme.infoContainerLight
import com.andikas.wang.ui.theme.infoDark
import com.andikas.wang.ui.theme.infoLight
import com.andikas.wang.ui.theme.successContainerDark
import com.andikas.wang.ui.theme.successContainerLight
import com.andikas.wang.ui.theme.successDark
import com.andikas.wang.ui.theme.successLight
import com.andikas.wang.ui.theme.warningContainerDark
import com.andikas.wang.ui.theme.warningContainerLight
import com.andikas.wang.ui.theme.warningDark
import com.andikas.wang.ui.theme.warningLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

enum class ToastType {
    SUCCESS,
    WARNING,
    ERROR,
    INFO
}

data class ToastData(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val title: String? = null,
    val type: ToastType = ToastType.INFO,
    val durationMillis: Long = 3500L,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

@Stable
class ToastHostState {
    var currentToastData by mutableStateOf<ToastData?>(null)
        private set

    private var dismissJob: Job? = null

    fun showToast(
        message: String,
        type: ToastType = ToastType.INFO,
        title: String? = null,
        durationMillis: Long = 3500L,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        scope: CoroutineScope
    ) {
        dismissJob?.cancel()
        val data = ToastData(
            message = message,
            title = title,
            type = type,
            durationMillis = durationMillis,
            actionLabel = actionLabel,
            onAction = onAction
        )
        currentToastData = data

        if (durationMillis > 0) {
            dismissJob = scope.launch {
                delay(durationMillis.milliseconds)
                if (currentToastData?.id == data.id) {
                    dismiss()
                }
            }
        }
    }

    fun showSuccess(
        message: String,
        title: String? = null,
        scope: CoroutineScope,
        durationMillis: Long = 3500L
    ) = showToast(
        message = message,
        type = ToastType.SUCCESS,
        title = title,
        durationMillis = durationMillis,
        scope = scope
    )

    fun showWarning(
        message: String,
        title: String? = null,
        scope: CoroutineScope,
        durationMillis: Long = 3500L
    ) = showToast(
        message = message,
        type = ToastType.WARNING,
        title = title,
        durationMillis = durationMillis,
        scope = scope
    )

    fun showError(
        message: String,
        title: String? = null,
        scope: CoroutineScope,
        durationMillis: Long = 3500L
    ) = showToast(
        message = message,
        type = ToastType.ERROR,
        title = title,
        durationMillis = durationMillis,
        scope = scope
    )

    fun showInfo(
        message: String,
        title: String? = null,
        scope: CoroutineScope,
        durationMillis: Long = 3500L
    ) = showToast(
        message = message,
        type = ToastType.INFO,
        title = title,
        durationMillis = durationMillis,
        scope = scope
    )

    fun dismiss() {
        dismissJob?.cancel()
        dismissJob = null
        currentToastData = null
    }
}

val LocalToastHostState = staticCompositionLocalOf { ToastHostState() }

@Composable
fun rememberToastHostState(): ToastHostState {
    return remember { ToastHostState() }
}

@Composable
fun ToastHost(
    hostState: ToastHostState,
    modifier: Modifier = Modifier
) {
    val currentData = hostState.currentToastData

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = currentData != null,
            enter = slideInVertically(
                initialOffsetY = { -it * 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 200)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it * 2 },
                animationSpec = tween(durationMillis = 250)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 200)
            ) + scaleOut(
                targetScale = 0.85f,
                animationSpec = tween(durationMillis = 200)
            )
        ) {
            currentData?.let { toastData ->
                CustomToastItem(
                    toastData = toastData,
                    onDismiss = { hostState.dismiss() }
                )
            }
        }
    }
}

@Composable
private fun CustomToastItem(
    toastData: ToastData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val (backgroundColor, contentColor, iconVector) = when (toastData.type) {
        ToastType.SUCCESS -> ToastStyle(
            backgroundColor = if (isDark) successContainerDark else successContainerLight,
            contentColor = if (isDark) successDark else successLight,
            iconVector = Icons.Rounded.CheckCircle
        )

        ToastType.WARNING -> ToastStyle(
            backgroundColor = if (isDark) warningContainerDark else warningContainerLight,
            contentColor = if (isDark) warningDark else warningLight,
            iconVector = Icons.Rounded.Warning
        )

        ToastType.ERROR -> ToastStyle(
            backgroundColor = if (isDark) errorContainerDark else errorContainerLight,
            contentColor = if (isDark) errorDark else errorLight,
            iconVector = Icons.Rounded.Error
        )

        ToastType.INFO -> ToastStyle(
            backgroundColor = if (isDark) infoContainerDark else infoContainerLight,
            contentColor = if (isDark) infoDark else infoLight,
            iconVector = Icons.Rounded.Info
        )
    }

    Card(
        modifier = modifier
            .widthIn(max = 480.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .clickable { onDismiss() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = toastData.type.name,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            val strings = LocalStrings.current
            val titleText = toastData.title ?: when (toastData.type) {
                ToastType.SUCCESS -> strings.success
                ToastType.WARNING -> strings.warning
                ToastType.ERROR -> strings.error
                ToastType.INFO -> strings.info
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (titleText.isNotEmpty()) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = contentColor
                    )
                }
                Text(
                    text = toastData.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    ),
                    color = contentColor
                )
            }

            if (!toastData.actionLabel.isNullOrEmpty() && toastData.onAction != null) {
                TextButton(
                    onClick = {
                        toastData.onAction.invoke()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = toastData.actionLabel,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = contentColor
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Dismiss",
                    tint = contentColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private data class ToastStyle(
    val backgroundColor: Color,
    val contentColor: Color,
    val iconVector: ImageVector
)
