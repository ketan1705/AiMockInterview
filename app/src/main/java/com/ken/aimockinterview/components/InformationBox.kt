package com.ken.aimockinterview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InformationBox(modifier: Modifier = Modifier) {

    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        color = Color(0XFFFFF7B7),
        border = BorderStroke(1.dp, Color.Yellow),
    ) {
        Column(
            modifier = modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0XFFDBA812)
                )
                Text(
                    "Information",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0XFFDBA812),
                    modifier = modifier.padding(start = 10.dp)
                )
            }

            Text(
                "Enable Video Web Cam and Microphone to Start your AI Generated Mock Interview. It has 5 question which you can answer and at the last you will get the report on the basis of your answer, Note: We never record your video, web cam access you can disable at any time if you want.",
                modifier.padding(vertical = 5.dp, horizontal = 15.dp),
                fontSize = 16.sp,
                color = Color(0XFFDBA812),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}