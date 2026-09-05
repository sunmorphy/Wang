package com.andikas.wang.ui.onboarding

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.NotificationsActive
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.domain.model.vo.ThemeType
import com.andikas.wang.ui.components.WGridSelectionItem
import com.andikas.wang.ui.components.WPrimaryButton
import com.andikas.wang.ui.components.WangHeader
import com.andikas.wang.ui.components.dialogs.PermissionsDialog
import com.andikas.wang.ui.i18n.Strings
import com.andikas.wang.ui.theme.WangTheme
import com.andikas.wang.ui.utils.PermissionManager
import com.andikas.wang.ui.utils.XiaomiPermissionHelper
import com.andikas.wang.ui.utils.bottomBorder
import com.andikas.wang.domain.model.vo.AppFeature
import com.andikas.wang.domain.model.vo.Permission
import com.andikas.wang.domain.model.vo.PermissionRequirements
import org.koin.compose.koinInject

data class OnboardingPage(
    val page: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinInject()
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

    val strings = LocalStrings.current

    val pages = listOf(
        OnboardingPage(
            page = 1,
            title = strings.onboarding1Title,
            description = strings.onboarding1Desc,
            icon = Icons.Rounded.WifiOff
        ),
        OnboardingPage(
            page = 2,
            title = strings.onboarding2Title,
            description = strings.onboarding2Desc,
            icon = Icons.Rounded.Lock
        ),
        OnboardingPage(
            page = 3,
            title = strings.onboarding3Title,
            description = strings.onboarding3Desc,
            icon = Icons.Rounded.NotificationsActive
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            WangHeader()

            pages.forEach { page ->
                OnboardingItem(page = page)
            }

            OnboardingTheme(strings = strings, viewModel = viewModel)

            OnboardingPermission(strings = strings, onAllowClick = {
                updatePermissionStates()
                showPermissionsDialog = true
            })

            Spacer(modifier = Modifier.padding(bottom = 48.dp))
        }

        if (showPermissionsDialog) {
            PermissionsDialog(
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
                            @SuppressLint("BatteryLife")
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
                onDismiss = {
                    showPermissionsDialog = false
                    if (permissionsToCheck.filter { permission -> permission.isMandatory }
                            .all { hasPermission(it) }) {
                        onFinished()
                    }
                }
            )
        }
    }
}

@Composable
private fun OnboardingItem(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .bottomBorder(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (page.page % 2 != 0) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = page.title,
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(-15f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Column(
            modifier = Modifier.weight(3f),
            horizontalAlignment = if (page.page % 2 == 0) Alignment.Start else Alignment.End
        ) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = if (page.page % 2 == 0) TextAlign.Start else TextAlign.End
            )
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = if (page.page % 2 == 0) TextAlign.Start else TextAlign.End
            )
        }
        if (page.page % 2 == 0) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = page.title,
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(15f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun OnboardingTheme(
    strings: Strings,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier
) {
    val itemsChunked = ThemeType.entries.chunked(3)
    val selectedTheme by viewModel.theme.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .bottomBorder(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 24.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = strings.onboardingTheme,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))
        itemsChunked.forEach { items ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        WGridSelectionItem(
                            item = item,
                            isSelected = item.themeCode == selectedTheme,
                            onClick = { viewModel.saveTheme(item.themeCode) },
                            icon = { if (item.themeCode == selectedTheme) item.activeIcon else item.icon },
                            label = { it.getLocalizedLabel(strings) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPermission(
    strings: Strings,
    onAllowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .bottomBorder(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 24.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = strings.onboardingPermission,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            WPrimaryButton(
                text = strings.allow,
                onClick = onAllowClick
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    WangTheme {
        Surface {
            OnboardingScreen({ })
        }
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingScreenDarkPreview() {
    WangTheme {
        Surface {
            OnboardingScreen({ })
        }
    }
}
