package dev.robert.design_system.presentation.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
