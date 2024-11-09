package com.example.flashcardapp.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.AuthUtils
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.presentation.DecksViewModel
import com.example.flashcardapp.ui.composable.CantLoadUserInformation
import com.example.flashcardapp.ui.composable.OnFailure
import com.example.flashcardapp.ui.composable.ProgressIndicator
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun DeckScreen(deckId: String?, navController: NavController, windowSize: WindowSizeClass) {
    val decksViewModel: DecksViewModel = hiltViewModel()
    val cardsState by decksViewModel.cardsState.collectAsState()
    val currentDeckState by decksViewModel.currentDeck.collectAsState()

    val uid = AuthUtils.currentId

    if (uid != null) {
        LaunchedEffect(Unit) {
            if (deckId != null) {
                decksViewModel.getDeckCards(uid, deckId)
                decksViewModel.getCurrentDeck(uid, deckId)
            }
        }
        currentDeckState?.let { result ->
            result.fold(
                onSuccess = { deck ->
                    cardsState?.let { result ->
                        result.fold(
                            onSuccess = { cards ->
                                Box(modifier = Modifier.fillMaxSize()) {
                                    val pagerState =
                                        rememberPagerState(initialPage = deck.lastCard) { cards.size }
                                    LaunchedEffect(pagerState.currentPage) {
                                        decksViewModel.updateLastCard(
                                            uid,
                                            deckId!!,
                                            pagerState.currentPage
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(deck.lastCard.toString())
                                        Text(deck.id)
                                        HorizontalPager(state = pagerState) { page ->
                                            val card = cards[page]
                                            DeckCard(card, windowSize)
                                        }
                                        Spacer(modifier = Modifier.height(50.dp))
                                        Row(
                                            Modifier
                                                .wrapContentHeight()
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            repeat(pagerState.pageCount) { iteration ->
                                                val color =
                                                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                                                Box(
                                                    modifier = Modifier
                                                        .padding(2.dp)
                                                        .clip(CircleShape)
                                                        .background(color)
                                                        .size(8.dp)
                                                )
                                            }
                                        }
                                    }

                                }

                            },
                            onFailure = { error ->
                                OnFailure(error)
                            }
                        )
                    }
                },
                onFailure = { error ->
                    OnFailure(error)
                }
            )
        } ?: ProgressIndicator()
    } else {
        CantLoadUserInformation()
    }
}

@Composable
fun DeckCard(card: Card, windowSize: WindowSizeClass) {

    var rotated by remember { mutableStateOf(false) }


    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )

    val colorAnimationBlack by animateColorAsState(
        targetValue = if (rotated) Color(0xFF213C99) else Color(0xFFAF1010),
        animationSpec = tween(500), label = ""
    )

    val colorAnimationFront by animateColorAsState(
        targetValue = if (!rotated) Color(0xFFAF1010) else Color(0xFF213C99),
        animationSpec = tween(500), label = ""
    )

    val modifierMobile = Modifier
        .height(300.dp)
        .fillMaxWidth()
        .padding(horizontal = AppTheme.size.medium)
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 8 * density
        }
        .clickable {
            rotated = !rotated
        }

    val modifierTablet = Modifier
        .height(400.dp)
        .width(600.dp)
        .graphicsLayer {
            rotationY = rotation
            cameraDistance = 8 * density
        }
        .clickable {
            rotated = !rotated
        }

    Card(
        modifier = if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) modifierMobile else modifierTablet,
        shape = RoundedCornerShape(10),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(4.dp, if (!rotated) colorAnimationFront else colorAnimationBlack),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        if (!rotated) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Question",
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = animateFront
                        },
                    fontWeight = FontWeight.Normal,
                    fontSize = 25.sp
                )
                Text(
                    text = card.question,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = animateFront
                        },
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Answer",
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animateBack
                            rotationY = rotation
                        },
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animateBack
                            rotationY = rotation
                        }, text = card.answer
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = animateBack
                                rotationY = rotation
                            }
                            .clip(RoundedCornerShape(50))
                            .size(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Ok",
                            tint = Color.Green,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .graphicsLayer {
                                rotationY = rotation
                            }
                            .clip(RoundedCornerShape(50))
                            .size(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Wrong",
                            tint = Color.Red,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

            }
        }
    }
}