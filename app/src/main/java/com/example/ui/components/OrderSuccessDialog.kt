package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbSecondaryText
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
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("btn_open_whatsapp_now")
            ) {
                Text(
                    text = "Abrir WhatsApp Ahora",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cerrar", color = GbDarkText)
            }
        },
        title = {
            Text(
                text = "¡Pedido Listo!",
                fontWeight = FontWeight.Black,
                color = GbDarkText
            )
        },
        text = {
            Text(
                text = "Recibimos tus datos para coordinar la entrega. Haz clic abajo para abrir WhatsApp y enviar el resumen final de tu pedido.",
                fontSize = 13.sp,
                color = GbSecondaryText
            )
        },
        shape = RoundedCornerShape(22.dp),
        containerColor = Color.White,
        modifier = modifier
    )
}

