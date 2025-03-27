package com.ken.aimockinterview.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ken.aimockinterview.MainActivity
import com.ken.aimockinterview.components.BoardingFooter
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.utils.OnBoardingUtils
import com.ken.aimockinterview.components.PagerIndicator

@Composable
fun OnBoardScreen(
    context: MainActivity,
    onBoardingItems: Routes.OnBoarding,
    onClick: () -> Unit,
) {
    val onBoardingUtils by lazy {
        OnBoardingUtils(context)
    }
    val onBoardingItems = onBoardingItems.onboardingItems.toList()
    val pagerState = rememberPagerState(
        pageCount = {
            onBoardingItems.size
        }
    )
    Box(
        Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                HorizontalPager(state = pagerState) { currentPage ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp, bottom = 26.dp, start = 26.dp, end = 26.dp),
                        horizontalAlignment = Alignment.Companion.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Image(
                            painter = painterResource(id = onBoardingItems[currentPage].image),
                            modifier = Modifier
//                                .fillMaxWidth(0.5f)
//                                .fillMaxHeight(0.6f),
                                .size(300.dp, 200.dp),
                            alignment = Alignment.Companion.Center,
                            contentDescription = null
                        )

                        Text(
                            onBoardingItems[currentPage].title,
                            modifier = Modifier
                                .padding(top = 20.dp),
                            textAlign = TextAlign.Companion.Center,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Companion.Normal,
                            lineHeight = 35.sp,
                            color = Color.Companion.Black,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            onBoardingItems[currentPage].description,
                            modifier = Modifier.padding(top = 40.dp),
                            textAlign = TextAlign.Companion.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Companion.Normal,
                            lineHeight = 30.sp,
                            color = Color.Companion.Black,
                            style = MaterialTheme.typography.bodySmall
                        )

                        PagerIndicator(
                            pageCount = onBoardingItems.size,
                            currentPage = pagerState.currentPage,
                            modifier = Modifier.padding(top = 80.dp)
                        )
                    }
                }
            }

            BoardingFooter(
                pagerState = pagerState,
                onClick = onClick,
                onBoardingUtils
            )
        }
    }
}