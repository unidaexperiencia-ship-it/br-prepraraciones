package com.example.data.model

data class PromoPack(
    val id: String,
    val title: String,
    val units: Int,
    val price: Double,
    val originalPrice: Double? = null,
    val badge: String? = null,
    val description: String,
    val savingsText: String,
    val weightInfo: String,
    val isPopular: Boolean = false
) {
    val unitPrice: Double get() = price / units
}

val defaultPromoPacks = listOf(
    PromoPack(
        id = "pack_1kg",
        title = "PACK 1 KG",
        units = 12,
        price = 5000.0,
        badge = "Ideal Familias Chicas",
        description = "12 sabrosas hamburguesas caseras de pollo sazonadas artesanalmente con pimientos y especias naturales.",
        savingsText = "$416 por unidad",
        weightInfo = "1.0 Kg aprox.",
        isPopular = false
    ),
    PromoPack(
        id = "pack_media_caja",
        title = "MEDIA CAJA",
        units = 30,
        price = 12250.0,
        originalPrice = 12500.0,
        badge = "¡Más Pedido! Ahorro",
        description = "30 unidades listas para freezer. Mismo sabor casero, mayor rendimiento semanal.",
        savingsText = "$408 por unidad • Ahorrá más",
        weightInfo = "2.5 Kg aprox.",
        isPopular = true
    ),
    PromoPack(
        id = "pack_caja_completa",
        title = "CAJA COMPLETA",
        units = 60,
        price = 24500.0,
        originalPrice = 25000.0,
        badge = "Máxima Economía",
        description = "60 unidades para eventos, reuniones familiares, catering o freezer mensual.",
        savingsText = "$408 por unidad • Máximo rinde",
        weightInfo = "5.0 Kg aprox.",
        isPopular = false
    )
)

enum class PaymentMethod(val title: String, val subtitle: String) {
    EFECTIVO("Efectivo al recibir", "Pagás cuando te entregamos el pedido en tu domicilio"),
    TRANSFERENCIA("Transferencia / Mercado Pago", "Alias: RB.PREPARACIONES.MP (Envío rápido tras comprobante)")
}

data class CartItem(
    val pack: PromoPack,
    var quantity: Int
)
