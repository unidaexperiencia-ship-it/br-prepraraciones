package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PackItemCard
import com.example.ui.components.PromoBannerCard
import com.example.ui.theme.GbBorderMuted
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbLavenderContainer
import com.example.ui.theme.GbLightBg
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbPrimaryContainer
import com.example.ui.theme.GbSecondaryText
import com.example.ui.theme.GbSuccessContainer
import com.example.ui.theme.GbSuccessGreen
import com.example.ui.theme.GbTertiaryBlue
import com.example.ui.theme.GbTertiaryContainer
import com.example.ui.viewmodel.RbUiState
import com.example.ui.viewmodel.RbViewModel

@Composable
fun PromotionsScreen(
    viewModel: RbViewModel,
    uiState: RbUiState,
    modifier: Modifier = Modifier
) {
    val (totalUnits, subtotal, finalTotal) = viewModel.calculateTotal()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GbLightBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = if (uiState.cart.isNotEmpty()) 105.dp else 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Flyer Banner Card
            item {
                PromoBannerCard()
            }

            // Quick Info Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        color = GbPrimaryContainer.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, GbBorderMuted)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GbPrimary,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.DeliveryDining,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Envíos en el día",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Santa Fe y zona",
                                    fontSize = 10.sp,
                                    color = GbSecondaryText
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        color = GbTertiaryContainer.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, GbBorderMuted)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GbTertiaryBlue,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Efectivo / Transf.",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Pagá fácil y rápido",
                                    fontSize = 10.sp,
                                    color = GbSecondaryText
                                )
                            }
                        }
                    }
                }
            }

            // Section Title
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Elegí tu Promo",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = GbDarkText
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = GbSuccessContainer
                        ) {
                            Text(
                                text = "100% Pollo Casero",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GbSuccessGreen
                            )
                        }
                    }
                    Text(
                        text = "Seleccioná la cantidad y armá tu pedido en segundos",
                        fontSize = 12.sp,
                        color = GbSecondaryText
                    )
                }
            }

            // The 3 Promo Packs: 12u ($5.000), 30u ($12.250), 60u ($24.500)
            items(uiState.promoPacks) { pack ->
                val qty = uiState.cart[pack.id] ?: 0
                PackItemCard(
                    pack = pack,
                    quantityInCart = qty,
                    onQuantityChange = { newQty ->
                        viewModel.setCartItemQuantity(pack.id, newQty)
                    },
                    onQuickOrderClick = {
                        viewModel.openCheckoutSheet(pack)
                    }
                )
            }

            // Homemade Guarantee Note
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = GbPrimaryContainer,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = GbPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "¿Por qué elegir RB Preparaciones?",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = GbDarkText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Elaboración casera con pechuga y muslo de pollo seleccionados, condimentos frescos y sin conservantes químicos. ¡Listas para cocinar y disfrutar!",
                                fontSize = 11.sp,
                                color = GbSecondaryText,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Floating Sticky Order Summary Bar
        if (uiState.cart.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = GbDarkText,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "$totalUnits Hamburguesas seleccionadas",
                            color = Color(0xFFFFDBC1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "$${String.format("%,.0f", finalTotal)}",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.openCheckoutSheet() },
                        colors = ButtonDefaults.buttonColors(containerColor = GbPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("btn_view_cart_checkout")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pedir Ahora",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

