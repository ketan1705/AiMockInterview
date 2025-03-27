package com.ken.aimockinterview.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ken.aimockinterview.utils.OnBoardingUtils
import kotlinx.coroutines.launch

@Composable
fun BoardingFooter(
    pagerState: PagerState, onClick: () -> Unit,
    onBoardingUtils: OnBoardingUtils,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 50.dp)
    ) {
        if (pagerState.currentPage != 2) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }

                },
                modifier = modifier
                    .align(Alignment.Companion.BottomEnd),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ), shape = RoundedCornerShape(10.dp)

            ) {
                Text(
                    "Next", fontSize = 22.sp,
                    modifier = modifier.padding(5.dp),
                    fontWeight = FontWeight.Companion.Normal,
//                    color = Color.Companion.Black,
                    style = MaterialTheme.typography.titleMedium
                )

            }

            if (pagerState.currentPage != 0)
                Button(
                    onClick = {
                        scope.launch {
                            val back = pagerState.currentPage - 1
                            if (back >= 0)
                                pagerState.animateScrollToPage(back)
                        }

                    },
                    modifier = modifier
                        .align(Alignment.Companion.BottomStart),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), shape = RoundedCornerShape(10.dp)

                ) {
                    Text(
                        "Back", fontSize = 22.sp,
                        modifier = modifier.padding(5.dp),
                        fontWeight = FontWeight.Companion.Normal,
//                    color = Color.Companion.Black,
                        style = MaterialTheme.typography.titleMedium
                    )

                }

        } else {
            OutlinedButton(
                onClick = {
                    Log.d("OnBoarding", "BoardingFooter:")
                    onBoardingUtils.setOnBoardingDone()
                    onClick()
                },
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.Companion.BottomCenter),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color(0xFF3F51B5)
                )
            ) {
                Text(
                    "Get Started",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Companion.Normal,
                    color = Color.Companion.White,
                    modifier = modifier.padding(5.dp)
                )
            }
        }
    }
}