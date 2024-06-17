package dev.robert.design_system.presentation.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.robert.design_system.R
import dev.robert.design_system.presentation.theme.TodoTheme

@Composable
fun TDButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onClick: () -> Unit,
    text: String,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
    ) {
        Text(text = text)
    }
}
@Composable
fun SignInWithGoogleButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .height(48.dp)
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.google),
            contentDescription = "Google Icon",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Sign in with Google",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showBackground = true,
)
@Composable
fun ButtonPreview() {
    TodoTheme(
        theme = AppCompatDelegate.MODE_NIGHT_NO,
    ) {
        TDButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = { },
            text = "Button 1",
            enabled = true,
        )
    }
}

@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showBackground = true,
)
@Composable
fun GButtonPreview() {
    TodoTheme(
        theme = AppCompatDelegate.MODE_NIGHT_NO,
    ) {
        SignInWithGoogleButton(
            onClick = { },
        )
    }
}
