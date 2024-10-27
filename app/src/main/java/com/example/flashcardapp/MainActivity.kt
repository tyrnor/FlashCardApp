package com.example.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
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
import com.example.flashcardapp.ui.theme.FlashCardAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardAppTheme {
                val windowSize = calculateWindowSizeClass(this)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF242323))
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AddCreditCard(windowSize)
                }
            }
        }
    }
}

@Composable
fun AddCreditCard(windowSize: WindowSizeClass) {

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
                                alpha = animateBack
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