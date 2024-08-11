package dev.robert.design_system.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.robert.design_system.R

@Composable
fun NavigationDrawerContent(
    modifier: Modifier,
    onTap: (String, Int) -> Unit = { _, _ -> },
    items: List<NavDrawerItem> = listOf(
        NavDrawerItem.Home,
        NavDrawerItem.Profile,
        NavDrawerItem.Settings,
        NavDrawerItem.Logout
    ),
    user: UserObject,
    selectedItem: Int = 0
) {
    var isHeaderExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DrawerHeader(user = user)
        HorizontalDivider()
        items.groupBy { it.section }.toList().forEachIndexed { sectionIndex, (section, items) ->
            DrawerSection(section.name) {
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        label = { Text(item.title) },
                        selected = if (sectionIndex < items.groupBy { it.section }.size) index == selectedItem else false,
                        onClick = {
                            onTap(item.title, index)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                    )
                }
            }
            if (sectionIndex < items.groupBy { it.section }.size) {
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
            }
        }
    }
}

@Composable
fun DrawerHeader(
    user: UserObject
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user.photoUrl?.let { AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.account_circle_24px),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp),
        ) }
        Spacer(modifier = Modifier.height(8.dp))
        user.displayName?.ifEmpty { "" }?.let { Text(text = it, style = MaterialTheme.typography.titleSmall) }
        user.email.ifEmpty { "" }.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
fun DrawerSection(title: String, content: @Composable () -> Unit) {
    if (title.isNotEmpty()) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp))
    }
    content()
}

sealed class NavDrawerItem(val title: String, val icon: Int, val section: Section = Section.Main) {
    data object Home : NavDrawerItem(HOME, R.drawable.home_24px, section = Section.Main)
    data object Profile : NavDrawerItem(PROFILE, R.drawable.account_circle_24px, section = Section.Main)
    data object Settings : NavDrawerItem(SETTINGS, R.drawable.settings_24px, section = Section.Main)
    data object Logout : NavDrawerItem(LOGOUT, R.drawable.logout_24px, section = Section.Secondary)

    companion object {
        const val HOME = "Home"
        const val PROFILE = "Profile"
        const val SETTINGS = "Settings"
        const val LOGOUT = "Logout"
    }
}

data class UserObject(
    val email: String,
    val displayName: String?,
    val photoUrl: String?
)

enum class Section {
    Main,
    Secondary,
    Tertiary
}

@Preview
@Composable
fun PreviewNavigationDrawerContent() {
    TDSurface {
        NavigationDrawerContent(modifier = Modifier.fillMaxWidth(), user = UserObject(email = "johndoe@gmal.com", displayName = "John Doe", photoUrl = null))
    }
}