package dev.robert.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsActionsOptions(
    sections: List<Section>
) {
    Box(
        modifier = Modifier.wrapContentHeight()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            sections.forEach { section ->
                item {
                    Column(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    ) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                section.items.forEach { item ->
                    item {
                        SettingsActionOption(
                            title = item.title,
                            subTitle = item.subTitle,
                            icon = item.icon,
                            action = item.action
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsActionOption(
    title: String,
    subTitle: String,
    icon: Int,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                action()
            }
            .background(
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                MaterialTheme.shapes.small
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    MaterialTheme.shapes.small
                )
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subTitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = action) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate to profile"
            )
        }
    }
}

data class Section(
    val title: String,
    val items: List<SectionItem>
)

data class SectionItem(
    val title: String,
    val subTitle: String,
    val icon: Int,
    val action: () -> Unit
)