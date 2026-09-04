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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
fun OpportunityScreen(
    viewModel: RbViewModel,
    uiState: RbUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAvailabilityDropdownExpanded by remember { mutableStateOf(false) }
    val availabilityOptions = listOf(
        "Medio tiempo (Flexible)",
        "Tiempo completo",
        "Fines de semana",
        "En ratos libres desde el celular"
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
                        color = GbSuccessContainer
                    ) {
                        Text(
                            text = "OPORTUNIDAD DE EMPRENDIMIENTO",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = GbSuccessGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "¡Ganá Dinero Extra\nDesde Tu Casa!",
                        color = GbDarkText,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sumate al equipo de revendedores oficiales de RB Preparaciones y generá ingresos vendiendo hamburguesas caseras de pollo de calidad comprobada.",
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
                            painter = painterResource(id = R.drawable.img_opportunity_reseller),
                            contentDescription = "Oportunidad de trabajo RB Preparaciones",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Key Benefits Grid
        item {
            Text(
                text = "¿Por qué sumarte al equipo RB?",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = GbDarkText
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BenefitItem(
                    icon = Icons.Default.AttachMoney,
                    title = "Excelente Margen de Ganancia",
                    description = "Comprás a precio diferencial mayorista y ganás un margen directo y limpio en cada venta.",
                    containerColor = GbSuccessContainer,
                    iconTint = GbSuccessGreen
                )
                BenefitItem(
                    icon = Icons.Default.Home,
                    title = "Trabajá 100% Desde Tu Casa",
                    description = "Ofrecé a vecinos, familiares, compañeros de trabajo, grupos de WhatsApp y redes sociales.",
                    containerColor = GbPrimaryContainer.copy(alpha = 0.6f),
                    iconTint = GbPrimary
                )
                BenefitItem(
                    icon = Icons.Default.TrendingUp,
                    title = "Producto de Alta Rotación",
                    description = "Las hamburguesas de pollo son un alimento diario que se consume y repone constantemente.",
                    containerColor = GbTertiaryContainer.copy(alpha = 0.6f),
                    iconTint = GbTertiaryBlue
                )
                BenefitItem(
                    icon = Icons.Default.Stars,
                    title = "Material Publicitario Incluido",
                    description = "Te entregamos flyers digitales, fotos de alta calidad y textos promocionales listos para publicar.",
                    containerColor = GbPrimaryContainer.copy(alpha = 0.6f),
                    iconTint = GbPrimary
                )
            }
        }

        // Application Form
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
                        text = "Formulario de Postulación",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = GbDarkText
                    )
                    Text(
                        text = "Completá tus datos y un coordinador del equipo se pondrá en contacto con vos.",
                        fontSize = 12.sp,
                        color = GbSecondaryText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = uiState.oppName,
                        onValueChange = { viewModel.onOppNameChange(it) },
                        label = { Text("Nombre y Apellido") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GbPrimary) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_opp_name"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.oppPhone,
                        onValueChange = { viewModel.onOppPhoneChange(it) },
                        label = { Text("Teléfono / WhatsApp") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GbPrimary) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_opp_phone"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.oppCity,
                        onValueChange = { viewModel.onOppCityChange(it) },
                        label = { Text("Ciudad / Barrio") },
                        leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null, tint = GbPrimary) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_opp_city"),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Availability Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isAvailabilityDropdownExpanded,
                        onExpandedChange = { isAvailabilityDropdownExpanded = !isAvailabilityDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.oppAvailability,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Disponibilidad de tiempo") },
                            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = GbPrimary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAvailabilityDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isAvailabilityDropdownExpanded,
                            onDismissRequest = { isAvailabilityDropdownExpanded = false }
                        ) {
                            availabilityOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.onOppAvailabilityChange(option)
                                        isAvailabilityDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Freezer Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = uiState.oppHasFreezer,
                            onCheckedChange = { viewModel.onOppHasFreezerChange(it) },
                            colors = CheckboxDefaults.colors(checkedColor = GbSuccessGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Dispongo de freezer / heladera para conservar stock",
                            fontSize = 12.sp,
                            color = GbDarkText,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.oppMotivation,
                        onValueChange = { viewModel.onOppMotivationChange(it) },
                        label = { Text("Experiencia o motivación (Opcional)") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.submitOpportunity(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = GbPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_opportunity")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enviar Postulación",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.sendOpportunityToWhatsApp(context) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = GbWhatsApp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Escribir Directo por WhatsApp",
                            color = GbDarkText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Success Dialog for Opportunity
    if (uiState.showOpportunitySuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissOpportunitySuccessDialog() },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendOpportunityToWhatsApp(context)
                        viewModel.dismissOpportunitySuccessDialog()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GbWhatsApp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Abrir WhatsApp Ahora", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.dismissOpportunitySuccessDialog() }) {
                    Text("Cerrar", color = GbDarkText)
                }
            },
            title = {
                Text(
                    text = "¡Postulación Enviada!",
                    fontWeight = FontWeight.Black,
                    color = GbDarkText
                )
            },
            text = {
                Text(
                    text = "Recibimos tus datos para sumarte al equipo de RB Preparaciones. En breve nos comunicaremos con vos para enviarte el catálogo de revendedores y lista de precios.",
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
private fun BenefitItem(
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

