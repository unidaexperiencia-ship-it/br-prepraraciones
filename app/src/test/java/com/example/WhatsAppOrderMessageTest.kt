package com.example

import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class WhatsAppOrderMessageTest {

    @Test
    fun testWhatsAppOrderMessageFormattingAndEncoding() {
        val nombre = "Carlos González"
        val telefono = "342 555-1234"
        val direccion = "San Martín 1500, Barrio Centro"
        val pedido = "Media Caja (30u)"
        val total = "$12.250"

        val message = buildString {
            append("👋 *¡HOLA EQUIPO RB PREPARACIONES! QUIERO CONFIRMAR MI PEDIDO*\n\n")
            append("👤 *Nombre:* $nombre\n")
            append("📱 *Teléfono:* $telefono\n")
            append("📍 *Dirección:* $direccion\n")
            append("📦 *Pedido:* $pedido\n")
            append("💰 *Total a Pagar:* $total\n\n")
            append("Por favor, confirmen el horario de entrega. ¡Muchas gracias!")
        }

        val expectedHeader = "👋 *¡HOLA EQUIPO RB PREPARACIONES! QUIERO CONFIRMAR MI PEDIDO*\n\n"
        assertTrue(message.startsWith(expectedHeader))
        assertTrue(message.contains("👤 *Nombre:* $nombre"))
        assertTrue(message.contains("📱 *Teléfono:* $telefono"))
        assertTrue(message.contains("📍 *Dirección:* $direccion"))
        assertTrue(message.contains("📦 *Pedido:* $pedido"))
        assertTrue(message.contains("💰 *Total a Pagar:* $total"))
        assertTrue(message.endsWith("Por favor, confirmen el horario de entrega. ¡Muchas gracias!"))

        val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        val url = "https://wa.me/message/BZAOF6RLPQOKN1?text=$encodedMessage"

        assertTrue(url.startsWith("https://wa.me/message/BZAOF6RLPQOKN1?text="))
        assertTrue(url.contains("%F0%9F%91%8B")) // 👋 emoji
        assertTrue(url.contains("%F0%9F%93%A6")) // 📦 emoji
    }
}

