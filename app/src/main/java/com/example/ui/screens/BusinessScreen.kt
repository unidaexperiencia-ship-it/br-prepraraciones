package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun BusinessScreen(
    viewModel: RbViewModel,
    uiState: RbUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isBusinessTypeDropdownExpanded by remember { mutableStateOf(false) }
    var isVolumeDropdownExpanded by remember { mutableStateOf(false) }

    val businessTypes = listOf(
        "Rotisería / Casa de Comidas",
        "Bar / Hamburguesería / Restaurant",
        "Almacén / Autoservicio / Supermercado",
        "Servicio de Catering / Eventos",
        "Comedor Escolar / Universitario / Empresa",
        "Alianza Corporativa / Beneficio Empleados"
    )

    val volumeTiers = listOf(
        "5 a 15 cajas por semana",
        "16 a 30 cajas por semana",
        "31 a 60 cajas por semana",
        "Más de 60 cajas por semana (Gran volumen)",
        "Pedido puntual para evento masivo"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GbLightBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
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
                        color = GbTertiaryContainer
                    ) {
                        Text(
                            text = "CANAL MAYORISTA Y EMPRESAS",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = GbTertiaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Alianzas y Pedidos\nal por Mayor",
                        color = GbDarkText,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Abastecimiento constante con precios preferenciales para rotiserías, bares, caterings, supermercados y convenios corporativos en Santa Fe.",
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
                            painter = painterResource(id = R.drawable.img_wholesale_partner),
                            contentDescription = "Alianzas mayoristas RB Preparaciones",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // B2B Value Pillars
        item {
            Text(
                text = "Ventajas para tu Comercio",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = GbDarkText
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BusinessPillar(
                    icon = Icons.Default.ReceiptLong,
                    title = "Precios Directos de Producción",
                    description = "Escalas de descuento progresivo según volumen mensual para maximizar el margen de tu carta o góndola.",
                    containerColor = GbPrimaryContainer.copy(alpha = 0.6f),
                    iconTint = GbPrimary
                )
                BusinessPillar(
                    icon = Icons.Default.Verified,
                    title = "Estandarización y Calidad Homogénea",
                    description = "Mismo gramaje, grosor y sazón en cada lote. Listas para cocción rápida en plancha, horno o freidora.",
                    containerColor = GbSuccessContainer,
                    iconTint = GbSuccessGreen
                )
                BusinessPillar(
                    icon = Icons.Default.LocalShipping,
                    title = "Logística y Reparto Programado",
                    description = "Entregas fijas en tu local en Santa Fe y alrededores para que nunca te quedes sin stock en momentos pico.",
                    containerColor = GbTertiaryContainer.copy(alpha = 0.6f),
                    iconTint = GbTertiaryBlue
                )
                BusinessPillar(
                    icon = Icons.Default.Inventory2,
                    title = "Presentación en Cajas Cerradas",
                    description = "Empaque higiénico y hermético ideal para freezer comercial, con separadores anti-adherencia.",
                    containerColor = GbPrimaryContainer.copy(alpha = 0.6f),
                    iconTint = GbPrimary
                )
            }
        }

        // Quote & Inquiry Form
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
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Solicitar Cotización Mayorista",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = GbDarkText
                    )
                    Text(
                        text = "Envíanos los datos de tu empresa o comercio para recibir el tarifario B2B personalizado.",
                        fontSize = 12.sp,
                        color = GbSecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.wsBusinessName,
                        onValueChange = { viewModel.onWsBusinessNameChange(it) },
                        label = { Text("Nombre del Negocio o Razón Social") },
                        leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null, tint = GbTertiaryBlue) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ws_business"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.wsContactName,
                        onValueChange = { viewModel.onWsContactNameChange(it) },
                        label = { Text("Persona de Contacto") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GbTertiaryBlue) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ws_contact"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.wsPhone,
                        onValueChange = { viewModel.onWsPhoneChange(it) },
                        label = { Text("Teléfono de Contacto / WhatsApp") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GbTertiaryBlue) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ws_phone"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.wsEmail,
                        onValueChange = { viewModel.onWsEmailChange(it) },
                        label = { Text("Correo Electrónico (Opcional)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GbTertiaryBlue) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Business Type Selector
                    ExposedDropdownMenuBox(
                        expanded = isBusinessTypeDropdownExpanded,
                        onExpandedChange = { isBusinessTypeDropdownExpanded = !isBusinessTypeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.wsBusinessType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rubro o Tipo de Comercio") },
                            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = GbTertiaryBlue) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isBusinessTypeDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isBusinessTypeDropdownExpanded,
                            onDismissRequest = { isBusinessTypeDropdownExpanded = false }
                        ) {
                            businessTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.onWsBusinessTypeChange(type)
                                        isBusinessTypeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Volume Tier Selector
                    ExposedDropdownMenuBox(
                        expanded = isVolumeDropdownExpanded,
                        onExpandedChange = { isVolumeDropdownExpanded = !isVolumeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.wsEstimatedBoxes,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Volumen estimado semanal") },
                            leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null, tint = GbTertiaryBlue) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isVolumeDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isVolumeDropdownExpanded,
                            onDismissRequest = { isVolumeDropdownExpanded = false }
                        ) {
                            volumeTiers.forEach { tier ->
                                DropdownMenuItem(
                                    text = { Text(tier, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.onWsEstimatedBoxesChange(tier)
                                        isVolumeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.wsComments,
                        onValueChange = { viewModel.onWsCommentsChange(it) },
                        label = { Text("Consultas específicas o requisitos (Opcional)") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.submitWholesale(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = GbTertiaryBlue),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_wholesale")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enviar Solicitud Mayorista",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.sendWholesaleToWhatsApp(context) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = GbWhatsApp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Contactar Ventas B2B por WhatsApp",
                            color = GbDarkText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Success Dialog for Wholesale
    if (uiState.showWholesaleSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissWholesaleSuccessDialog() },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendWholesaleToWhatsApp(context)
                        viewModel.dismissWholesaleSuccessDialog()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GbWhatsApp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Abrir WhatsApp con Cotización", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.dismissWholesaleSuccessDialog() }) {
                    Text("Cerrar", color = GbDarkText)
                }
            },
            title = {
                Text(
                    text = "¡Solicitud Registrada!",
                    fontWeight = FontWeight.Black,
                    color = GbDarkText
                )
            },
            text = {
                Text(
                    text = "Hemos guardado la consulta de ${uiState.wsBusinessName}. Nuestro ejecutivo de cuentas comerciales en Santa Fe te enviará la propuesta y lista de precios mayorista.",
                    fontSize = 13.sp,
                    color = GbSecondaryText
                )
            },
            shape = RoundedCornerShape(22.dp),
            containerColor = Color.White
        )
    }
}

@Composable
private fun BusinessPillar(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    containerColor: Color,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GbBorderMuted)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = containerColor,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Black, fontSize = 13.sp, color = GbDarkText)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, fontSize = 11.sp, color = GbSecondaryText, lineHeight = 15.sp)
            }
        }
    }
}

