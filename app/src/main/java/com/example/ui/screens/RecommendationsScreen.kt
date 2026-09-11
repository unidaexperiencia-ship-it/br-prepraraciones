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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.formatCurrency
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
    val uiState by viewModel.uiState.collectAsState()
    val referralData by viewModel.referralData.collectAsState()
    val referralCode = referralData?.myReferralCode ?: "RB-AMIGO"
    val friendsCount = referralData?.totalReferredFriends ?: 0
    val freeBurgers = referralData?.earnedFreeBurgers ?: 0
    val discountPercent = referralData?.unlockedDiscountPercent ?: 0
    val creditRb = referralData?.earnedCreditRb ?: 0.0

    var isPinVisible by remember { mutableStateOf(false) }

    // Admin Authentication Dialog
    if (uiState.showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeAdminLoginDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = GbPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acceso Creador / Admin",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = GbDarkText
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Ingresá el PIN de creador para habilitar la edición interactiva de métricas de recompensa:",
                        fontSize = 13.sp,
                        color = GbSecondaryText
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = uiState.adminPinInput,
                        onValueChange = { viewModel.onAdminPinChange(it) },
                        label = { Text("PIN de Creador") },
                        placeholder = { Text("Ej: 1234") },
                        singleLine = true,
                        isError = uiState.adminPinError != null,
                        visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPinVisible = !isPinVisible }) {
                                Icon(
                                    imageVector = if (isPinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (isPinVisible) "Ocultar PIN" else "Mostrar PIN"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_pin")
                    )
                    if (uiState.adminPinError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = uiState.adminPinError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.submitAdminPin(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = GbPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_confirm_admin_pin")
                ) {
                    Text("Ingresar", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeAdminLoginDialog() }) {
                    Text("Cancelar", color = GbSecondaryText)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

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
                        text = "¡Recomendá y Ganá\nMilanesas Gratis!",
                        color = GbDarkText,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Si recomendás a tus amigos, familiares o vecinos, ganás descuentos directos o más milanesas en tu próximo pedido.",
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

        // Live Referral Stats Header & Admin Mode Switch
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recompensa Acumulada",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = GbDarkText
                    )
                    Text(
                        text = if (uiState.isAdminMode) "Modo Creador (Edición Exclusiva)" else "Vista de Solo Lectura",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (uiState.isAdminMode) GbPrimary else GbSecondaryText
                    )
                }

                // Discreto botón de administración/creador con autenticación por PIN
                if (!uiState.isAdminMode) {
                    FilledTonalButton(
                        onClick = { viewModel.openAdminLoginDialog() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GbBorderMuted.copy(alpha = 0.5f),
                            contentColor = GbDarkText
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_creator_mode_login")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Acceso Creador",
                            modifier = Modifier.size(15.dp),
                            tint = GbDarkText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Admin",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.exitAdminMode(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = GbDarkText),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_creator_mode_exit")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Salir de Modo Admin",
                            modifier = Modifier.size(15.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Salir",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 3 Puntos de Métrica de Recompensa Acumulada
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Amigos Sumados (Contador de Referidos)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbTertiaryContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GbTertiaryBlue.copy(alpha = 0.15f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = "Amigos Sumados",
                                        tint = GbTertiaryBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Amigos Sumados",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Contador de referidos vinculados",
                                    fontSize = 11.sp,
                                    color = GbSecondaryText
                                )
                            }
                        }

                        // Métrica visual & Controles
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.isAdminMode) {
                                IconButton(
                                    onClick = { viewModel.modifyReferredFriends(-1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_minus_friends"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Restar amigo",
                                        tint = GbTertiaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, GbBorderMuted)
                            ) {
                                Text(
                                    text = "$friendsCount",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    color = GbTertiaryBlue,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            if (uiState.isAdminMode) {
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.modifyReferredFriends(1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_plus_friends"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = GbTertiaryBlue,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Sumar amigo",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Milanesas Gratis (Recompensas en Producto)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbPrimaryContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GbPrimary.copy(alpha = 0.15f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Fastfood,
                                        contentDescription = "Milanesas Gratis",
                                        tint = GbPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Milanesas Gratis",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Unidades/cajas acumuladas de regalo",
                                    fontSize = 11.sp,
                                    color = GbSecondaryText
                                )
                            }
                        }

                        // Métrica visual & Controles
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.isAdminMode) {
                                IconButton(
                                    onClick = { viewModel.modifyEarnedFreeBurgers(-1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_minus_burgers"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Restar milanesa gratis",
                                        tint = GbPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, GbBorderMuted)
                            ) {
                                Text(
                                    text = "+$freeBurgers",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    color = GbPrimary,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            if (uiState.isAdminMode) {
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.modifyEarnedFreeBurgers(1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_plus_burgers"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = GbPrimary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Sumar milanesa gratis",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Descuento / Crédito RB (Monto Acumulado en Pesos)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GbSuccessContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, GbBorderMuted)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GbSuccessGreen.copy(alpha = 0.15f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Discount,
                                        contentDescription = "Crédito RB",
                                        tint = GbSuccessGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Descuento / Crédito RB",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GbDarkText
                                )
                                Text(
                                    text = "Saldo acumulado disponible en pesos",
                                    fontSize = 11.sp,
                                    color = GbSecondaryText
                                )
                            }
                        }

                        // Métrica visual & Controles (+ / - de a $500)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.isAdminMode) {
                                IconButton(
                                    onClick = { viewModel.modifyEarnedCreditRb(-500.0) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_minus_credit"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Restar $500 crédito",
                                        tint = GbSuccessGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, GbBorderMuted)
                            ) {
                                Text(
                                    text = formatCurrency(creditRb),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = GbSuccessGreen,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            if (uiState.isAdminMode) {
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.modifyEarnedCreditRb(500.0) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .testTag("btn_plus_credit"),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = GbSuccessGreen,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Sumar $500 crédito",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
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
                        title = "¡Vos recibís milanesas o descuento!",
                        description = "En tu próximo pedido te sumamos milanesas de regalo o descontamos de tu total."
                    )
                }
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

