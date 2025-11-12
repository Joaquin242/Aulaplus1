package com.example.aulaplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTable(
    headers: List<String>,
    rows: List<List<String>>,
    columnWeights: List<Float> = List(headers.size) { 1f },
    rowPadding: Dp = 12.dp
) {
    require(headers.size == columnWeights.size) { "headers y columnWeights deben tener el mismo tamaño" }

    Surface(tonalElevation = 1.dp) {
        Column(Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                headers.forEachIndexed { i, h ->
                    Text(
                        text = h,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(columnWeights[i])
                    )
                }
            }
            HorizontalDivider()

            // Rows
            rows.forEachIndexed { idx, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = rowPadding / 2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEachIndexed { i, cell ->
                        Text(
                            text = cell,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(columnWeights[i])
                        )
                    }
                }
                if (idx != rows.lastIndex) {
                    HorizontalDivider(color = Color(0x1F000000)) // separador suave
                }
            }
        }
    }
}
