package com.example.animelist.presentation.screen.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.presentation.components.StatusChip

// поиск и фильтры
@Composable
fun SearchSection(
    searchQuery: String,
    selectedStatus: AnimeStatus?,
    onSearchQueryChange: (String) -> Unit,
    onStatusSelected: (AnimeStatus?) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // поле поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск аниме...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск"
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Фильтры по статусу
        StatusFilterRow(
            selectedStatus = selectedStatus,
            onStatusSelected = onStatusSelected
        )
    }
}

@Composable
private fun StatusFilterRow(
    selectedStatus: AnimeStatus?,
    onStatusSelected: (AnimeStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusChip(
            label = "Все",
            isSelected = selectedStatus == null,
            onClick = { onStatusSelected(null) }
        )

        AnimeStatus.entries.forEach { status ->
            StatusChip(
                label = when (status) {
                    AnimeStatus.WATCHING -> "Смотрю"
                    AnimeStatus.COMPLETED -> "Просмотрено"
                    AnimeStatus.PLANNED -> "В планах"
                    AnimeStatus.DROPPED -> "Брошено"
                    AnimeStatus.FAVORITE -> "Любимое"
                },
                isSelected = selectedStatus == status,
                onClick = { onStatusSelected(status) }
            )
        }
    }
}