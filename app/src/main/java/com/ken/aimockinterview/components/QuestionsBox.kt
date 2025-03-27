package com.ken.aimockinterview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ken.aimockinterview.models.QuesAnsModel
import com.ken.aimockinterview.ui.theme.darkBlue

@Composable
fun QuestionsBox(
    modifier: Modifier = Modifier,
    questionList: List<QuesAnsModel>,
    currentQuestionIndex: Int,
    onSpeak: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .heightIn(min = LocalConfiguration.current.screenHeightDp.dp * 0.16f)
            .padding(end = 10.dp),
        color = Color.LightGray.copy(0.4f),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                "Question: ${currentQuestionIndex + 1} out of ${questionList.size}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(10.dp))

            if (currentQuestionIndex < questionList.size) {
                val questionText =
                    questionList.getOrNull(currentQuestionIndex)?.question
                        ?: "No Question Available"
//                state.value[currentQuestionIndex].question
                Text(
                    text = questionText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = darkBlue.copy(0.8f),
                    fontWeight = FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Speak",
                modifier = modifier.clickable(
                    onClick = {
                        onSpeak()
                    }
                )
            )


        }
    }
}
