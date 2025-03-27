package com.ken.aimockinterview.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Confirm Logout",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 22.sp,
            )
        },
        text = {
            Text(
                "Are you sure you want to log out?",
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelMedium
            )
        },
        confirmButton = {
            TextButton(onClick = {
                // Handle logout logic here
                onConfirm()
            }) {
                Text(
                    "Yes", color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp

                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "No", color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    )
}
