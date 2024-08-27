package dev.robert.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.robert.design_system.presentation.theme.Theme as AppTheme
import java.util.Locale

@Composable
fun ThemeToggleChips(
    themeOptions: List<ThemeOption>,
    onSelectTheme: (Theme) -> Unit,
    theme: Int,
    modifier: Modifier = Modifier
) {
    val selectedTheme = remember { mutableStateOf(theme.themeFromValue()) }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        themeOptions.let { cat ->
            items(cat.size) { index ->
                ThemeChip(
                    text = cat[index].theme.name,
                    onClick = {
                        selectedTheme.value = cat[index].theme.themeFromValue()
                        onSelectTheme(cat[index].theme)
                    },
                    selected = selectedTheme.value == cat[index].theme.themeFromValue(),
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun ThemeChip(
    text: String,
    onClick: (String) -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .border(
            width = 0.dp,
            color = MaterialTheme.colorScheme.secondary,
            shape = MaterialTheme.shapes.small
        )
        .background(
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            },
            shape = MaterialTheme.shapes.small
        )
        .padding(10.dp)
        .clickable {
            onClick(text)
        }
    ) {
        Text(
            text = text.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight(800)),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
        )
    }
}

data class ThemeOption(
    val theme: Theme,
)

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM,
    MATERIAL_YOU
}

fun Theme.themeFromValue(): AppTheme {
    return when (this) {
        Theme.LIGHT -> AppTheme.LIGHT_THEME
        Theme.DARK -> AppTheme.DARK_THEME
        Theme.SYSTEM -> AppTheme.FOLLOW_SYSTEM
        Theme.MATERIAL_YOU -> AppTheme.MATERIAL_YOU
    }
}

fun AppTheme.themeToValue(): Theme {
    return when (this) {
        AppTheme.LIGHT_THEME -> Theme.LIGHT
        AppTheme.DARK_THEME -> Theme.DARK
        AppTheme.FOLLOW_SYSTEM -> Theme.SYSTEM
        AppTheme.MATERIAL_YOU -> Theme.MATERIAL_YOU
    }
}

fun Int.themeFromValue(): AppTheme {
    return when (this) {
        AppTheme.LIGHT_THEME.themeValue -> AppTheme.LIGHT_THEME
        AppTheme.DARK_THEME.themeValue -> AppTheme.DARK_THEME
        AppTheme.FOLLOW_SYSTEM.themeValue -> AppTheme.FOLLOW_SYSTEM
        AppTheme.MATERIAL_YOU.themeValue -> AppTheme.MATERIAL_YOU
        else -> AppTheme.FOLLOW_SYSTEM
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeToggleChipsPreview() {
    Surface {
        ThemeToggleChips(
            themeOptions = listOf(
                ThemeOption(Theme.SYSTEM),
                ThemeOption(Theme.LIGHT),
                ThemeOption(Theme.DARK),
                ThemeOption(Theme.MATERIAL_YOU)
            ),
            onSelectTheme = {},
            theme = AppTheme.MATERIAL_YOU.themeValue
        )
    }
}
