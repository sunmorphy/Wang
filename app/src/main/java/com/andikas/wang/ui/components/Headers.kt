package com.andikas.wang.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andikas.wang.ui.theme.WangTheme

@Composable
fun WangHeader(
    modifier: Modifier = Modifier,
    startContent: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null
) {
    val startWeight by animateFloatAsState(
        targetValue = if (startContent != null) 1f else 0.001f,
        label = "startWeight"
    )
    val endWeight by animateFloatAsState(
        targetValue = if (endContent != null) 1f else 0.001f,
        label = "endWeight"
    )

    val logoBias by animateFloatAsState(
        targetValue = when {
            startContent == null && endContent == null -> 0f
            startContent != null && endContent == null -> 1f
            startContent == null && endContent != null -> -1f
            else -> 0f
        },
        label = "logoBias"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(64.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(startWeight),
            contentAlignment = Alignment.CenterStart
        ) {
            if (startContent != null) {
                startContent()
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = BiasAlignment(logoBias, 0f)
        ) {
            WangLogo(modifier = Modifier.height(24.dp))
        }
        Box(
            modifier = Modifier.weight(endWeight),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (endContent != null) {
                endContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WangHeaderInteractivePreview() {
    var showStart by remember { mutableStateOf(true) }
    var showEnd by remember { mutableStateOf(true) }

    WangTheme {
        Surface {
            Column {
                WangHeader(
                    startContent = if (showStart) {
                        {
                            WIconButton(
                                imageVector = Icons.Default.Menu,
                                onClick = { showStart = false },
                                useBackground = false
                            )
                        }
                    } else null,
                    endContent = if (showEnd) {
                        {
                            WIconButton(
                                imageVector = Icons.Default.Notifications,
                                onClick = { showEnd = false },
                                useBackground = false
                            )
                        }
                    } else null
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WSecondaryButton(
                        text = "Toggle Start",
                        onClick = { showStart = !showStart }
                    )
                    WSecondaryButton(
                        text = "Toggle End",
                        onClick = { showEnd = !showEnd }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WangHeaderPreview() {
    WangTheme {
        Surface {
            WangHeaderPreviewContent()
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WangHeaderDarkPreview() {
    WangTheme {
        Surface {
            WangHeaderPreviewContent()
        }
    }
}

@Composable
private fun WangHeaderPreviewContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        WangHeader()

        WangHeader(
            startContent = {
                WIconButton(
                    imageVector = Icons.Default.Menu,
                    onClick = {},
                    useBackground = true
                )
            }
        )

        WangHeader(
            endContent = {
                WIconButton(
                    imageVector = Icons.Default.Notifications,
                    onClick = {},
                    useBackground = false
                )
            }
        )

        WangHeader(
            startContent = {
                WIconButton(
                    imageVector = Icons.Default.Menu,
                    onClick = {},
                    useBackground = false
                )
            },
            endContent = {
                WIconButton(
                    imageVector = Icons.Default.Notifications,
                    onClick = {},
                    useBackground = false
                )
            }
        )
    }
}
