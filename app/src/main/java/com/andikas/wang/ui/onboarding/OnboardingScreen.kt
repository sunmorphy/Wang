package com.andikas.wang.ui.onboarding

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.BatteryAlert
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FlipToFront
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.andikas.wang.ui.components.WangHeader
import com.andikas.wang.ui.components.WangPrimaryButton
import com.andikas.wang.ui.components.WangSecondaryButton
import com.andikas.wang.ui.components.WangSurfaceButton
import com.andikas.wang.ui.components.WangTonalButton
import com.andikas.wang.ui.theme.WangTheme
import com.andikas.wang.ui.utils.PermissionManager
import com.andikas.wang.ui.utils.XiaomiPermissionHelper
import com.andikas.wang.vo.AppFeature
import com.andikas.wang.vo.Permission
import com.andikas.wang.vo.PermissionRequirements
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val circleCenter: (width: Float, height: Float, density: Float) -> Offset
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val permissionManager = remember { PermissionManager(context) }
    val xiaomiHelper = remember { XiaomiPermissionHelper(context) }

    var showPermissionsDialog by remember { mutableStateOf(false) }

    fun hasPermission(permission: Permission): Boolean {
        return when (permission) {
            Permission.POST_NOTIFICATIONS -> permissionManager.hasPostNotificationsPermission()
            Permission.NOTIFICATION_LISTENER -> permissionManager.hasNotificationListenerPermission()
            Permission.BATTERY_OPTIMIZATION -> permissionManager.isIgnoringBatteryOptimizations()
            Permission.SYSTEM_ALERT_WINDOW -> permissionManager.hasOverlayPermission()
            Permission.MIUI_AUTOSTART -> permissionManager.checkAutostartIndirectly() == PermissionManager.AutostartStatus.LIKELY_ENABLED
            Permission.MIUI_BATTERY_NO_RESTRICT -> permissionManager.isIgnoringBatteryOptimizations()
            Permission.MIUI_BACKGROUND_POPUP -> permissionManager.hasOverlayPermission()
            else -> true
        }
    }

    val permissionsToCheck = remember {
        (
                PermissionRequirements.getRequirementsFor(AppFeature.LOCAL_ALERTS) +
                        PermissionRequirements.getRequirementsFor(AppFeature.AUTO_TRANSACTION_CAPTURE) +
                        PermissionRequirements.getRequirementsFor(AppFeature.FLOATING_BUBBLE)
                ).distinct().filter {
                if (it.name.startsWith("MIUI_")) permissionManager.isXiaomiDevice() else true
            }
    }

    var grantedPermissions by remember {
        mutableStateOf(permissionsToCheck.associateWith { hasPermission(it) })
    }

    fun updatePermissionStates() {
        grantedPermissions = permissionsToCheck.associateWith { hasPermission(it) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        updatePermissionStates()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                updatePermissionStates()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val handleDone = {
        val allGranted = permissionsToCheck.all { hasPermission(it) }
        if (allGranted) {
            onFinished()
        } else {
            updatePermissionStates()
            showPermissionsDialog = true
        }
    }

    val pages = listOf(
        OnboardingPage(
            title = "Fully Offline",
            description = "Don't have any internet? Not a big deal! The app works even without internet.",
            icon = Icons.Rounded.WifiOff,
            circleCenter = { w, h, d -> Offset(0f, h * 0.2f) } // Top Left
        ),
        OnboardingPage(
            title = "Encrypted Data",
            description = "Doubt about your data? We collect nothing! The app works using local database with encryption.",
            icon = Icons.Rounded.Lock,
            circleCenter = { w, h, d -> Offset(w, h * 0.5f) } // Middle Right
        ),
        OnboardingPage(
            title = "Notification Listener",
            description = "Tired of doing it manually? Just press the bubble and the app will listen to your transactions.",
            icon = Icons.Rounded.Notifications,
            circleCenter = { w, h, d -> Offset(w * 0.2f, h * 0.9f) } // Bottom Leftish
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    val ringColor = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Calculate current position based on pager state (smooth transition)
            val page = pagerState.currentPage
            val offset = pagerState.currentPageOffsetFraction

            val startCenter = pages[page].circleCenter(width, height, density)
            val endCenter = if (page + 1 < pages.size) {
                pages[page + 1].circleCenter(width, height, density)
            } else {
                startCenter
            }

            // Interpolate position between pages
            val animatedCenter = Offset(
                x = startCenter.x + (endCenter.x - startCenter.x) * offset,
                y = startCenter.y + (endCenter.y - startCenter.y) * offset
            )

            // Dynamic radius (optional: pulses slightly when moving)
            val baseRadius = 120.dp.toPx()
            val animatedRadius = baseRadius + (kotlin.math.abs(offset) * 20.dp.toPx())

            drawCircle(
                color = ringColor.copy(alpha = 0.4f),
                radius = animatedRadius,
                center = animatedCenter,
                style = Stroke(width = 45.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            WangHeader(
                endContent = if (pagerState.currentPage != pages.size - 1) {
                    {
                        WangSurfaceButton(
                            text = "Skip",
                            onClick = handleDone
                        )
                    }
                } else null
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) { position ->
                val page = pages[position]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = page.title,
                            modifier = Modifier.size(128.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(pages.size) { iteration ->
                        val isSelected = pagerState.currentPage == iteration
                        val color =
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        val width = if (isSelected) 24.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                WangPrimaryButton(
                    text = if (pagerState.currentPage == pages.size - 1) "Done" else "Next",
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            handleDone()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(bottom = 48.dp))
        }

        if (showPermissionsDialog) {
            WangPermissionsDialog(
                permissions = permissionsToCheck,
                grantedStates = grantedPermissions,
                onGrantClick = { permission ->
                    when (permission) {
                        Permission.POST_NOTIFICATIONS -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }

                        Permission.NOTIFICATION_LISTENER -> {
                            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                        }

                        Permission.BATTERY_OPTIMIZATION -> {
                            context.startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = "package:${context.packageName}".toUri()
                            })
                        }

                        Permission.SYSTEM_ALERT_WINDOW -> {
                            context.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                                data = "package:${context.packageName}".toUri()
                            })
                        }

                        Permission.MIUI_AUTOSTART -> xiaomiHelper.openAutostartSettings()
                        Permission.MIUI_BATTERY_NO_RESTRICT -> xiaomiHelper.openMiuiBatterySettings()
                        Permission.MIUI_BACKGROUND_POPUP -> xiaomiHelper.openMiuiBackgroundPopupSettings()
                        else -> {}
                    }
                },
                onContinueClick = {
                    showPermissionsDialog = false
                    onFinished()
                },
                onDismissRequest = {
                    showPermissionsDialog = false
                }
            )
        }
    }
}

@Composable
private fun WangPermissionsDialog(
    permissions: List<Permission>,
    grantedStates: Map<Permission, Boolean>,
    onGrantClick: (Permission) -> Unit,
    onContinueClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Security,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "App Permissions",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "To enable automatic transaction capture and alerts, please grant the following permissions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    permissions.forEach { permission ->
                        val isGranted = grantedStates[permission] ?: false
                        PermissionListItem(
                            permission = permission,
                            isGranted = isGranted,
                            onGrantClick = { onGrantClick(permission) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val missingMandatory =
                    permissions.any { it.isMandatory && !(grantedStates[it] ?: false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WangSecondaryButton(
                        text = "Cancel",
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f)
                    )
                    WangPrimaryButton(
                        text = "Continue",
                        onClick = onContinueClick,
                        enabled = !missingMandatory,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionListItem(
    permission: Permission,
    isGranted: Boolean,
    onGrantClick: () -> Unit
) {
    val icon = when (permission) {
        Permission.POST_NOTIFICATIONS -> Icons.Rounded.Notifications
        Permission.BATTERY_OPTIMIZATION -> Icons.Rounded.BatteryChargingFull
        Permission.NOTIFICATION_LISTENER -> Icons.Rounded.Hearing
        Permission.SYSTEM_ALERT_WINDOW -> Icons.Rounded.FlipToFront
        Permission.MIUI_AUTOSTART -> Icons.Rounded.Autorenew
        Permission.MIUI_BATTERY_NO_RESTRICT -> Icons.Rounded.BatteryAlert
        Permission.MIUI_BACKGROUND_POPUP -> Icons.AutoMirrored.Rounded.OpenInNew
        else -> Icons.Rounded.Security
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            val badgeText = if (permission.isMandatory) "Required" else "Optional"
            val badgeBg = if (permission.isMandatory)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
            val badgeFg = if (permission.isMandatory)
                MaterialTheme.colorScheme.onErrorContainer
            else
                MaterialTheme.colorScheme.onSecondaryContainer

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeFg
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = permission.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = permission.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isGranted) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Granted",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            WangTonalButton(
                text = "Grant",
                onClick = onGrantClick,
                modifier = Modifier.height(36.dp)
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    WangTheme {
        Surface {
            OnboardingScreen { }
        }
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingScreenDarkPreview() {
    WangTheme {
        Surface {
            OnboardingScreen { }
        }
    }
}
