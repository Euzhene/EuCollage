package com.euzhene.instagramcollage.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExitAlertDialog(onSave: () -> Unit, onExit: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                AlertButton(onClick = onSave, text = "Save")
                AlertButton(onClick = onExit, text = "Exit")
            }

        },
        title = { Text(text = "You haven't saved new data. Do you wanna do it?") },
    )
}

@Composable
fun AlertButton(onClick: () -> Unit, text: String) {
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)) {
        Text(text, color = Color.White)
    }
}