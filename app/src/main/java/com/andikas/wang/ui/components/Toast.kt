package com.andikas.wang.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andikas.wang.ui.theme.WangTheme
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
    val durationMillis: Long = 3000L,
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
        durationMillis: Long = 3000L,
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
        durationMillis: Long = 3000L
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
        durationMillis: Long = 3000L
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
        durationMillis: Long = 3000L
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
        durationMillis: Long = 3000L
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
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(animationSpec = tween(durationMillis = 180)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 180)
            ) + fadeOut(animationSpec = tween(durationMillis = 180))
        ) {
            currentData?.let { toastData ->
                SimpleToastItem(
                    toastData = toastData,
                    onDismiss = { hostState.dismiss() }
                )
            }
        }
    }
}

@Composable
private fun SimpleToastItem(
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

    Surface(
        modifier = modifier
            .widthIn(max = 420.dp)
            .clickable(onClick = onDismiss),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = toastData.type.name,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            if (!toastData.title.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = toastData.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                        color = contentColor
                    )
                    Text(
                        text = toastData.message,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                            lineHeight = 17.sp
                        ),
                        color = contentColor.copy(alpha = 0.9f)
                    )
                }
            } else {
                Text(
                    text = toastData.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = contentColor,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            if (!toastData.actionLabel.isNullOrEmpty() && toastData.onAction != null) {
                TextButton(
                    onClick = {
                        toastData.onAction.invoke()
                        onDismiss()
                    },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = toastData.actionLabel,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = contentColor
                    )
                }
            }
        }
    }
}

private data class ToastStyle(
    val backgroundColor: Color,
    val contentColor: Color,
    val iconVector: ImageVector
)

@Preview
@Composable
private fun SimpleToastItemPreview() {
    WangTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SimpleToastItem(
                toastData = ToastData(
                    message = "Transaction saved successfully!",
                    type = ToastType.SUCCESS
                ),
                onDismiss = {}
            )

            SimpleToastItem(
                toastData = ToastData(
                    title = "Budget Warning",
                    message = "You have reached 85% of your food budget.",
                    type = ToastType.WARNING
                ),
                onDismiss = {}
            )

            SimpleToastItem(
                toastData = ToastData(
                    message = "Failed to connect to database",
                    type = ToastType.ERROR
                ),
                onDismiss = {}
            )
        }
    }
}
