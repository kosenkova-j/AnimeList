// presentation/screen/detail/StatusSelectionDialog.kt
package com.example.animelist.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animelist.domain.model.AnimeStatus

@Composable
fun StatusSelectionDialog(
    currentStatus: AnimeStatus?,
    onStatusSelected: (AnimeStatus?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите статус") },
        text = {
            Column {
                // Опция "Не в списке"
                StatusOption(
                    title = "Не в списке",
                    isSelected = currentStatus == null,
                    onClick = { onStatusSelected(null) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Все статусы
                AnimeStatus.entries
                    .filter { it != AnimeStatus.FAVORITE }  // Избранное отдельно
                    .forEach { status ->
                        StatusOption(
                            title = getStatusDisplayName(status),
                            isSelected = currentStatus == status,
                            onClick = { onStatusSelected(status) }
                        )
                    }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun StatusOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getStatusDisplayName(status: AnimeStatus): String {
    return when (status) {
        AnimeStatus.WATCHING -> "Смотрю"
        AnimeStatus.COMPLETED -> "Просмотрено"
        AnimeStatus.PLANNED -> "В планах"
        AnimeStatus.DROPPED -> "Брошено"
        AnimeStatus.FAVORITE -> "Любимое"
    }
}