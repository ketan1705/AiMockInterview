package com.ken.aimockinterview.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ken.aimockinterview.states.VoiceToTextParserState
import com.ken.aimockinterview.ui.theme.lightBlue
import com.ken.aimockinterview.ui.theme.mediumBlue

@Composable
fun CameraColumn(
    modifier: Modifier,
    voiceToTextState: State<VoiceToTextParserState>,
    onClick: () -> Unit,
    nextButtonClick: () -> Unit,
) {
    var isVisible by remember { mutableStateOf(true) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomCamera()
        if (isVisible)
            OutlinedButton(
                onClick = {
                    onClick()
//                        if (currentQuestionIndex.intValue < state.value.size - 1) {
//                            currentQuestionIndex.intValue++
//                        }
                },
                modifier
                    .fillMaxWidth(0.5f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightBlue,
                ),
                border = BorderStroke(color = Color.LightGray, width = 1.dp)
            )
            {
                Row(
                    modifier = modifier.padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AnimatedContent(targetState = voiceToTextState.value.isSpeaking) { isSpeaking ->

                        Icon(
                            if (isSpeaking) Icons.Filled.Stop else Icons.Filled.Mic,
                            contentDescription = "Record",
                            tint = Color.White,
                        )
                    }
                    AnimatedContent(targetState = voiceToTextState.value.isSpeaking) { isSpeaking ->

                        Text(
                            if (isSpeaking) "Stop" else "Record",
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = modifier.padding(vertical = 8.dp, horizontal = 5.dp)
                        )
                    }
                }
            }

        OutlinedButton(
            onClick = {
                nextButtonClick()

//                        if (currentQuestionIndex.intValue < state.value.size - 1) {
//                            currentQuestionIndex.intValue++
//                        }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mediumBlue,
            ),
            border = BorderStroke(color = Color.LightGray, width = 1.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
        )
        {
            Text(
                "Next",
                style = MaterialTheme.typography.labelMedium,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun CustomCamera(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .fillMaxWidth(0.70f)
            .height(130.dp),
        shape = RoundedCornerShape(15.dp),
        color = Color.LightGray.copy(0.4f),
        border = BorderStroke(2.dp, Color.LightGray),

        ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = null,
            tint = Color.White,
            modifier = modifier.padding(10.dp),
        )
    }
}
