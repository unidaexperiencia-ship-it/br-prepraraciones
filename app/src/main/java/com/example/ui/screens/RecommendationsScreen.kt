package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GbBorderMuted
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbLavenderAccent
import com.example.ui.theme.GbLavenderContainer
import com.example.ui.theme.GbLightBg
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbPrimaryContainer
import com.example.ui.theme.GbSecondaryText
import com.example.ui.theme.GbSuccessContainer
import com.example.ui.theme.GbSuccessGreen
import com.example.ui.theme.GbTertiaryBlue
import com.example.ui.theme.GbTertiaryContainer
import com.example.ui.theme.GbWhatsApp
import com.example.ui.viewmodel.RbViewModel

@Composable
fun RecommendationsScreen(
    viewModel: RbViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val referralData by viewModel.referralData.collectAsState()
    val referralCode = referralData?.myReferralCode ?: "RB-AMIGO"
    val friendsCount = referralData?.totalReferredFriends ?: 0
    val freeBurgers = referralData?.earnedFreeBurgers ?: 0
    val discountPercent = referralData?.unlockedDiscountPercent ?: 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GbLightBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Geometric Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, GbBorderMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = GbLavenderContainer
                    ) {
                        Text(
                            text = "PROGRAMA DE RECOMENDACIONES",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = GbLavenderAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "¡Recomendá y Ganá\nHamburguesas Gratis!",
                        color = GbDarkText,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Si recomendás a tus amigos, familiares o vecinos, ganás descuentos directos o más hamburguesas en tu próximo pedido.",
                        color = GbSecondaryText,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_referral_reward),
                            contentDescription = "Programa de referidos RB Preparaciones",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Referral Code Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, GbBorderMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tu Código Único de Recomendación:",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = GbDarkText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GbPrimaryContainer.copy(alpha = 0.5f),
                        border = BorderStroke(1.5.dp, GbPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = referralCode,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    letterSpacing = 2.sp,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Da 10% OFF a quien lo use",
                                    fontSize = 11.sp,
                                    color = GbSuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Código RB", referralCode)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "¡Código $referralCode copiado!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("btn_copy_referral")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copiar código",
                                    tint = GbPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Share on WhatsApp button
                    Button(
                        onClick = { viewModel.shareReferralWhatsApp(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = GbWhatsApp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_share_referral_whatsapp")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Compartir por WhatsApp con Amigos",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Live Referral Stats Header
        item {
            Text(
                text = "Tus Recompensas Acumuladas",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = GbDarkText
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Friends
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbTertiaryContainer.copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = GbTertiaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$friendsCount",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = GbTertiaryBlue
                        )
                        Text(
                            text = "Amigos sumados",
                            fontSize = 11.sp,
                            color = GbDarkText,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Stat 2: Free burgers
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbPrimaryContainer.copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fastfood,
                            contentDescription = null,
                            tint = GbPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+$freeBurgers",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = GbPrimary
                        )
                        Text(
                            text = "Burgers gratis",
                            fontSize = 11.sp,
                            color = GbDarkText,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Stat 3: Discount unlocked
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbSuccessContainer),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Discount,
                            contentDescription = null,
                            tint = GbSuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$discountPercent%",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = GbSuccessGreen
                        )
                        Text(
                            text = "Descuento",
                            fontSize = 11.sp,
                            color = GbDarkText,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // How it works 3-step breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GbBorderMuted)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Cómo funciona el beneficio?",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = GbDarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    StepRow(
                        number = "1",
                        title = "Enviás tu código a un conocido",
                        description = "Compartí tu código de recomendación por WhatsApp o redes sociales."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    StepRow(
                        number = "2",
                        title = "Tu conocido hace su primer pedido",
                        description = "Ingresa tu código en la app o WhatsApp y recibe 10% de descuento inmediato."
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    StepRow(
                        number = "3",
                        title = "¡Vos recibís hamburguesas o descuento!",
                        description = "En tu próximo pedido te sumamos hamburguesas de regalo o descontamos de tu total."
                    )
                }
            }
        }

        // Interactive simulation button for testing
        item {
            OutlinedButton(
                onClick = { viewModel.simulateReferral(context) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_simulate_referral")
            ) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = GbPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simular Amigo que compró (Probar Recompensa)", color = GbDarkText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StepRow(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = GbPrimary,
            modifier = Modifier.size(26.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Black, fontSize = 13.sp, color = GbDarkText)
            Text(text = description, fontSize = 11.sp, color = GbSecondaryText)
        }
    }
}

