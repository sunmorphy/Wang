package com.andikas.wang.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.andikas.wang.R

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andikas.wang.ui.theme.WangTheme

@Composable
fun WangLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.wang),
        contentDescription = "Wang",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun WangLogoPreview() {
    WangTheme {
        Surface {
            WangLogo(modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WangLogoDarkPreview() {
    WangTheme {
        Surface {
            WangLogo(modifier = Modifier.padding(16.dp))
        }
    }
}
