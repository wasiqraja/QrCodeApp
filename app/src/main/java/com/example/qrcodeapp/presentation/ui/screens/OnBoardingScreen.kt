package com.example.qrcodeapp.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


// ── Data model ──────────────────────────────────────────────
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

// ── Replace drawables with your actual image resources ──────
val onboardingPages = listOf(
    OnboardingPage(
        imageRes = com.example.qrcodeapp.R.drawable.qr_background_3,   // your image
        title = "Scan Any QR Code",
        description = "Point your camera at any QR code\nand instantly get the result."
    ),
    OnboardingPage(
        imageRes = com.example.qrcodeapp.R.drawable.qr_background_3,
        title = "Generate Instantly",
        description = "Create custom QR codes for links,\ntext, contacts and much more."
    ),
    OnboardingPage(
        imageRes = com.example.qrcodeapp.R.drawable.qr_background_3,
        title = "Save Your History",
        description = "Every scan and generated code\nis saved for quick access later."
    )
)

// ── Main Screen ──────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardScreen(
    onFinish: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingPages.lastIndex

    // Animate background color per page
    val bgColors = listOf(
        Color(0xFF0A0E1A),
        Color(0xFF0D1A0F),
        Color(0xFF1A0A0E)
    )
    val animatedBg by animateColorAsState(
        targetValue = bgColors[pagerState.currentPage],
        animationSpec = tween(600),
        label = "bg"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP 70% — Pager + Dots ───────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.70f)   // ← exactly 70%
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Image Pager
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) { page ->
                        OnboardingPageItem(page = onboardingPages[page])
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dot Indicators
                    DotIndicators(
                        pageCount = onboardingPages.size,
                        currentPage = pagerState.currentPage
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ── BOTTOM 30% — Title, Description, Button ──────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()           // remaining 30%
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Title
                Text(
                    text = onboardingPages[pagerState.currentPage].title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )

                // Description
                Text(
                    text = onboardingPages[pagerState.currentPage].description,
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                // Next / Get Started Button
                Button(
                    onClick = {
                        if (isLastPage) {
                            onFinish()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E5A0)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = if (isLastPage) "Get Started" else "Next",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0A0E1A)
                    )
                }

                // Skip (only visible before last page)
                if (!isLastPage) {
                    TextButton(onClick = onFinish) {
                        Text(
                            text = "Skip",
                            color = Color.White.copy(alpha = 0.45f),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }
}

// ── Single Pager Page ────────────────────────────────────────
@Composable
fun OnboardingPageItem(page: OnboardingPage) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Image with rounded corners + overlay gradient
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(28.dp))
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bottom gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)),
                            startY = 300f
                        )
                    )
            )
        }
    }
}

// ── Dot Indicators ───────────────────────────────────────────
@Composable
fun DotIndicators(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) 28.dp else 8.dp,
                animationSpec = tween(300),
                label = "dotWidth"
            )
            val dotColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF00E5A0) else Color.White.copy(alpha = 0.3f),
                animationSpec = tween(300),
                label = "dotColor"
            )

            Box(
                modifier = Modifier
                    .width(dotWidth)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

