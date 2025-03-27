package com.ken.aimockinterview.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomizedTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    minLines: Int,
    placeHolderText: String,
    supportText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {

    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(10.dp),
        minLines = minLines,
        singleLine = singleLine,
        supportingText = {
            Text(
                supportText, color = Color.Red,
                fontSize = 14.sp, fontWeight = FontWeight.SemiBold
            )
        },
        placeholder = {
            Text(
                placeHolderText,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(0.8f), fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,

        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray.copy(0.2f),
            unfocusedContainerColor = Color.LightGray.copy(0.2f),
            disabledContainerColor = Color.LightGray.copy(0.2f),
            cursorColor = Color.White.copy(0.8f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White.copy(0.8f),
            unfocusedTextColor = Color.White.copy(0.8f)
        )
    )
}