package com.devanshdroid.disasterbuddy.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devanshdroid.disasterbuddy.core.utils.Constants

@Composable
fun RoleChipGroup(
    selectedRole: String,
    onRoleSelected: (String) -> Unit
) {
    Row {
        FilterChip(
            selected = selectedRole == Constants.ROLE_CITIZEN,
            onClick = { onRoleSelected(Constants.ROLE_CITIZEN) },
            label = { Text("Citizen") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        FilterChip(
            selected = selectedRole == Constants.ROLE_VOLUNTEER,
            onClick = { onRoleSelected(Constants.ROLE_VOLUNTEER) },
            label = { Text("Volunteer") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        )
    }
}