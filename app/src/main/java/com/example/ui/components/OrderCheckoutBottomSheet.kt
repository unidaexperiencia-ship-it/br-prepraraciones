package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentMethod
import com.example.ui.theme.GbBorderMuted
import com.example.ui.theme.GbDarkText
import com.example.ui.theme.GbLightBg
import com.example.ui.theme.GbPrimary
import com.example.ui.theme.GbPrimaryContainer
import com.example.ui.theme.GbSecondaryText
import com.example.ui.theme.GbSuccessContainer
import com.example.ui.theme.GbSuccessGreen
import com.example.ui.theme.GbTertiaryBlue
import com.example.ui.theme.GbTertiaryContainer
import com.example.ui.theme.GbWhatsApp
import com.example.ui.viewmodel.RbUiState
import com.example.ui.viewmodel.RbViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCheckoutBottomSheet(
    viewModel: RbViewModel,
    uiState: RbUiState,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val (totalUnits, subtotal, finalTotal) = viewModel.calculateTotal()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GbLightBg,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GbPrimaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = GbPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Completar Pedido",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = GbDarkText
                    )
                    Text(
                        text = "RB Preparaciones • Delivery Santa Fe",
                        fontSize = 12.sp,
                        color = GbSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cart Items Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GbBorderMuted)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Detalle del Pedido:",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = GbDarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.cart.isEmpty()) {
                        Text(
                            text = "No has seleccionado ningún pack aún.",
                            fontSize = 12.sp,
                            color = GbSecondaryText
                        )
                    } else {
                        uiState.cart.forEach { (packId, qty) ->
                            val pack = uiState.promoPacks.find { it.id == packId }
                            if (pack != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${qty}x ${pack.title} (${pack.units * qty}u)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = GbDarkText
                                    )
                                    Text(
                                        text = "$${String.format("%,.0f", pack.price * qty)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GbPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Frequent buyer saved address badge indicator
            val savedProfile = viewModel.customerProfile.value
            if (savedProfile != null && savedProfile.address.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GbSuccessContainer,
                    border = BorderStroke(1.dp, GbSuccessGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GbSuccessGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¡Cliente Frecuente! Completamos tu dirección guardada automáticamente.",
                            fontSize = 11.sp,
                            color = GbSuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Customer Name & Phone
            Text(
                text = "1. Tus Datos de Contacto",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = GbDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputCustomerName,
                onValueChange = { viewModel.onCustomerNameChange(it) },
                label = { Text("Nombre y Apellido") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GbPrimary) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_name"),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputCustomerPhone,
                onValueChange = { viewModel.onCustomerPhoneChange(it) },
                label = { Text("Teléfono / WhatsApp") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GbPrimary) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_phone"),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Address
            Text(
                text = "2. Dirección de Entrega",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = GbDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputAddress,
                onValueChange = { viewModel.onAddressChange(it) },
                label = { Text("Calle, Altura y Piso/Dpto") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = GbPrimary) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_address"),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.inputNeighborhood,
                    onValueChange = { viewModel.onNeighborhoodChange(it) },
                    label = { Text("Barrio / Zona") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = GbPrimary) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_customer_neighborhood"),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputDeliveryNotes,
                onValueChange = { viewModel.onDeliveryNotesChange(it) },
                label = { Text("Aclaración para el repartidor (Opcional)") },
                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = GbPrimary) },
                placeholder = { Text("Ej: Tocar timbre 2B, portón negro") },
                singleLine = false,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_delivery_notes"),
                shape = RoundedCornerShape(16.dp)
            )

            // Save address toggle for repeat buyers
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onRememberAddressChange(!uiState.rememberAddress) }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = uiState.rememberAddress,
                    onCheckedChange = { viewModel.onRememberAddressChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = GbPrimary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Guardar mis datos para compras frecuentes",
                    fontSize = 12.sp,
                    color = GbDarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Method Selection
            Text(
                text = "3. Forma de Pago",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = GbDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Option 1: Efectivo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onPaymentMethodChange(PaymentMethod.EFECTIVO) }
                    .testTag("payment_cash"),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(
                    1.5.dp,
                    if (uiState.selectedPaymentMethod == PaymentMethod.EFECTIVO) GbPrimary else GbBorderMuted
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.selectedPaymentMethod == PaymentMethod.EFECTIVO) GbPrimaryContainer.copy(alpha = 0.5f) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.selectedPaymentMethod == PaymentMethod.EFECTIVO,
                        onClick = { viewModel.onPaymentMethodChange(PaymentMethod.EFECTIVO) },
                        colors = RadioButtonDefaults.colors(selectedColor = GbPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GbSuccessContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.LocalAtm,
                                contentDescription = null,
                                tint = GbSuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Efectivo al Recibir",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = GbDarkText
                        )
                        Text(
                            text = "Pagás en mano al repartidor cuando llega tu pedido",
                            fontSize = 11.sp,
                            color = GbSecondaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Option 2: Transferencia / Mercado Pago
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onPaymentMethodChange(PaymentMethod.TRANSFERENCIA) }
                    .testTag("payment_transfer"),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(
                    1.5.dp,
                    if (uiState.selectedPaymentMethod == PaymentMethod.TRANSFERENCIA) GbPrimary else GbBorderMuted
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.selectedPaymentMethod == PaymentMethod.TRANSFERENCIA) GbPrimaryContainer.copy(alpha = 0.5f) else Color.White
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.selectedPaymentMethod == PaymentMethod.TRANSFERENCIA,
                            onClick = { viewModel.onPaymentMethodChange(PaymentMethod.TRANSFERENCIA) },
                            colors = RadioButtonDefaults.colors(selectedColor = GbPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GbTertiaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = GbTertiaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Transferencia / Mercado Pago",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = GbDarkText
                            )
                            Text(
                                text = "Transferís con CBU o Alias y nos enviás comprobante",
                                fontSize = 11.sp,
                                color = GbSecondaryText
                            )
                        }
                    }

                    AnimatedVisibility(visible = uiState.selectedPaymentMethod == PaymentMethod.TRANSFERENCIA) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 8.dp, end = 8.dp)
                                .background(GbLightBg, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Datos de Transferencia:",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = GbDarkText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Alias: ${viewModel.aliasTransferencia}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GbPrimary
                                )
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Alias", viewModel.aliasTransferencia)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Alias copiado al portapapeles", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar Alias",
                                        modifier = Modifier.size(18.dp),
                                        tint = GbPrimary
                                    )
                                }
                            }
                            Text(
                                text = "Titular: ${viewModel.titularTransferencia}",
                                fontSize = 11.sp,
                                color = GbSecondaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coupon Code / Referral Code Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.appliedCouponCode,
                    onValueChange = { viewModel.onCouponCodeChange(it) },
                    label = { Text("Código de descuento / Amigo") },
                    leadingIcon = { Icon(Icons.Default.Discount, contentDescription = null, tint = GbPrimary) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_coupon"),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.applyCoupon(context) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GbDarkText),
                    modifier = Modifier.height(54.dp)
                ) {
                    Text("Aplicar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = GbBorderMuted)
            Spacer(modifier = Modifier.height(12.dp))

            // Order Price Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total unidades:", fontSize = 13.sp, color = GbSecondaryText)
                Text(text = "$totalUnits hamburguesas", fontSize = 13.sp, fontWeight = FontWeight.Black, color = GbDarkText)
            }

            if (uiState.appliedDiscountPercent > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Descuento (${uiState.appliedDiscountPercent}% OFF):", fontSize = 13.sp, color = GbSuccessGreen)
                    Text(
                        text = "-$${String.format("%,.0f", subtotal * (uiState.appliedDiscountPercent / 100.0))}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = GbSuccessGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total a Pagar:",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GbDarkText
                )
                Text(
                    text = "$${String.format("%,.0f", finalTotal)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 26.sp,
                    color = GbPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = { viewModel.submitOrder(context) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GbWhatsApp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_confirm_order")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirmar Pedido",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}

