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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.robert.settings.R

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    name: String = "",
    email: String = "",
    onClick: () -> Unit = {}
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            MaterialTheme.shapes.small
        )
        .padding(8.dp)
        .clickable {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(dev.robert.design_system.R.drawable.account_circle_24px),
                contentDescription = stringResource(dev.robert.design_system.R.string.profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp),
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = name.ifEmpty { "U" }.firstOrNull()?.uppercase().toString(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = name)
            Text(email)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Navigate to profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSectionPreview() {
    ProfileSection(
        imageUrl = "https://randomuser",
        name = "Robert",
        email = "robert@gmail.com",
        onClick = {}
    )
}
