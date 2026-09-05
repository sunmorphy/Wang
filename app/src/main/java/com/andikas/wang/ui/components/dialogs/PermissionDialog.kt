package com.andikas.wang.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.andikas.wang.ui.components.WTonalButton
import com.andikas.wang.domain.model.vo.Permission

@Composable
fun PermissionsDialog(
    permissions: List<Permission>,
    grantedStates: Map<Permission, Boolean>,
    onGrantClick: (Permission) -> Unit,
    onDismiss: () -> Unit
) {
    val strings = LocalStrings.current

    WMainDialog(
        onDismiss = onDismiss,
        icon = Icons.Rounded.Security,
        title = strings.permissionsTitle,
        description = strings.permissionsDesc
    ) {
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
    }
}

@Composable
private fun PermissionListItem(
    permission: Permission,
    isGranted: Boolean,
    onGrantClick: () -> Unit
) {
    val strings = LocalStrings.current
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
            val badgeText = if (permission.isMandatory) strings.required else strings.optional
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
                text = permission.getLocalizedTitle(strings),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = permission.getLocalizedDescription(strings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isGranted) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = strings.granted,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            WTonalButton(
                text = strings.grant,
                onClick = onGrantClick,
                modifier = Modifier.height(36.dp)
            )
        }
    }
}