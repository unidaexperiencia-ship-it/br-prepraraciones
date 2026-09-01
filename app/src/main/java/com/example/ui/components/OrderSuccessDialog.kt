package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.theme.GbBorderMuted
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbLightBg
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbSecondaryText
import com.example.ui.theme.GbSuccessContainer
import com.example.ui.theme.GbSuccessGreen
import com.example.ui.theme.GbWhatsApp
import com.example.ui.viewmodel.RbViewModel

@Composable
fun OrderSuccessDialog(
    order: OrderEntity,
    viewModel: RbViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    viewModel.sendOrderToWhatsApp(context, order)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GbWhatsApp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_dialog_whatsapp")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar Pedido a WhatsApp", fontWeight = FontWeight.Black, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar y Ver Menú", color = GbDarkText, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = GbSuccessContainer,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = GbSuccessGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "¡Pedido Registrado\ncon Éxito!",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = GbDarkText,
                    lineHeight = 24.sp
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¡Gracias por elegir RB Preparaciones! Tu pedido #${order.id} ya fue guardado en el sistema.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = GbSecondaryText
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GbLightBg),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Resumen del Pedido:",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = GbPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "• ${order.itemsSummary}", fontSize = 12.sp, color = GbDarkText, fontWeight = FontWeight.Medium)
                        Text(text = "• Total: $${String.format("%,.0f", order.totalPrice)}", fontWeight = FontWeight.Black, fontSize = 13.sp, color = GbDarkText)
                        Text(text = "• Pago: ${order.paymentMethod}", fontSize = 12.sp, color = GbSecondaryText)
                        Text(text = "• Entrega: ${order.deliveryAddress} (${order.neighborhood})", fontSize = 12.sp, color = GbSecondaryText)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Presioná el botón verde abajo para notificar al repartidor por WhatsApp al instante.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = GbSuccessGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        modifier = modifier
    )
}

