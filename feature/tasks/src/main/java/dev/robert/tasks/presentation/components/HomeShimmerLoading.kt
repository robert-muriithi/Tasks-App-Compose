/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.tasks.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.robert.tasks.presentation.screens.tasks.TasksScreenState

@Composable
fun HomeShimmerLoading(
    modifier: Modifier = Modifier,
    uiState: TasksScreenState,
    successContent: @Composable () -> Unit,
    errorContent: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit
) {
    val gridState = rememberLazyGridState()
    if (uiState.isLoading)
        Column(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()

            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(180.dp, 20.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    items(5) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 40.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                state = gridState
            ) {
                items(6) {
                    Box(
                        modifier = Modifier
                            .size(180.dp, 200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .shimmerEffect()
                    )
                }
            }
        }

    if (!uiState.isLoading && uiState.error == null && uiState.tasks.isNotEmpty()) successContent()
    if (!uiState.isLoading && uiState.error == null && uiState.tasks.isEmpty()) emptyContent()
    else errorContent()
}
