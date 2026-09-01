package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PromoPack
import com.example.ui.theme.GbBorderMuted
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbLavenderAccent
import com.example.ui.theme.GbLavenderContainer
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbPrimaryContainer
import com.example.ui.theme.GbSecondaryText
import com.example.ui.theme.GbSuccessContainer
import com.example.ui.theme.GbSuccessGreen

@Composable
fun PackItemCard(
    pack: PromoPack,
    quantityInCart: Int,
    onQuantityChange: (Int) -> Unit,
    onQuickOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = quantityInCart > 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("pack_card_${pack.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pack.isPopular) Color(0xFFFFF9F5) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected || pack.isPopular) 2.dp else 1.dp,
            color = when {
                isSelected -> GbPrimary
                pack.isPopular -> GbPrimary.copy(alpha = 0.5f)
                else -> GbBorderMuted
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header with badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pack Title Pill
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (pack.isPopular) GbPrimaryContainer else Color(0xFFF4ECE6)
                ) {
                    Text(
                        text = pack.title,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = GbDarkText
                    )
                }

                if (pack.badge != null) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (pack.isPopular) GbPrimary else GbSuccessGreen
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (pack.isPopular) Icons.Default.Star else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = pack.badge,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Units & Price display with Geometric Balance styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Large Unit Counter Display
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${pack.units}",
                        fontWeight = FontWeight.Black,
                        fontSize = 38.sp,
                        color = GbPrimary,
                        lineHeight = 38.sp
                    )
                    Text(
                        text = "u",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GbDarkText,
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${pack.weightInfo})",
                        fontSize = 12.sp,
                        color = GbSecondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Price
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${String.format("%,.0f", pack.price)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = GbDarkText
                    )
                    Text(
                        text = pack.savingsText,
                        fontSize = 11.sp,
                        color = GbSuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pack.description,
                fontSize = 12.sp,
                color = GbSecondaryText,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Row: Quantity counter and Quick Order button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stepper Counter
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF7EFE9),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        FilledTonalIconButton(
                            onClick = { onQuantityChange(quantityInCart - 1) },
                            enabled = quantityInCart > 0,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_minus_${pack.id}"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Restar cantidad",
                                modifier = Modifier.size(16.dp),
                                tint = if (quantityInCart > 0) GbPrimary else Color.Gray
                            )
                        }

                        Text(
                            text = "$quantityInCart",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = GbDarkText,
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .testTag("qty_text_${pack.id}")
                        )

                        FilledTonalIconButton(
                            onClick = { onQuantityChange(quantityInCart + 1) },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_plus_${pack.id}"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = GbPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Sumar cantidad",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Quick Order Button
                Button(
                    onClick = onQuickOrderClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) GbSuccessGreen else GbPrimary
                    ),
                    modifier = Modifier.testTag("btn_order_${pack.id}")
                ) {
                    Text(
                        text = if (isSelected) "Pedir ($quantityInCart)" else "Elegir",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

