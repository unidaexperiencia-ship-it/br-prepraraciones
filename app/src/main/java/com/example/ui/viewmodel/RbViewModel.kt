package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CustomerProfileEntity
import com.example.data.local.OpportunityInquiryEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ReferralDataEntity
import com.example.data.local.WholesaleInquiryEntity
import com.example.data.model.CartItem
import com.example.data.model.PaymentMethod
import com.example.data.model.PromoPack
import com.example.data.model.defaultPromoPacks
import com.example.data.repository.RbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class RbUiState(
    val selectedTab: Int = 0,
    val promoPacks: List<PromoPack> = defaultPromoPacks,
    val cart: Map<String, Int> = mapOf("pack_media_caja" to 1), // Default 1 media caja selected
    val isCheckoutSheetOpen: Boolean = false,
    val isOrderHistoryOpen: Boolean = false,
    val lastPlacedOrder: OrderEntity? = null,
    val showOrderSuccessDialog: Boolean = false,
    val showWholesaleSuccessDialog: Boolean = false,
    val showOpportunitySuccessDialog: Boolean = false,
    // Customer form inputs
    val inputCustomerName: String = "",
    val inputCustomerPhone: String = "",
    val inputAddress: String = "",
    val inputNeighborhood: String = "Santa Fe Centro",
    val inputDeliveryNotes: String = "",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.EFECTIVO,
    val rememberAddress: Boolean = true,
    val appliedCouponCode: String = "",
    val appliedDiscountPercent: Int = 0,
    // Opportunity form inputs
    val oppName: String = "",
    val oppPhone: String = "",
    val oppCity: String = "Santa Fe / Alrededores",
    val oppAvailability: String = "Medio tiempo (Flexible)",
    val oppHasFreezer: Boolean = true,
    val oppMotivation: String = "",
    // Wholesale form inputs
    val wsBusinessName: String = "",
    val wsContactName: String = "",
    val wsPhone: String = "",
    val wsEmail: String = "",
    val wsBusinessType: String = "Rotisería / Gastronómico",
    val wsEstimatedBoxes: String = "10 a 25 cajas semanales",
    val wsComments: String = ""
)

class RbViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RbRepository

    private val _uiState = MutableStateFlow(RbUiState())
    val uiState: StateFlow<RbUiState> = _uiState.asStateFlow()

    val customerProfile: StateFlow<CustomerProfileEntity?>
    val orderHistory: StateFlow<List<OrderEntity>>
    val referralData: StateFlow<ReferralDataEntity?>
    val wholesaleHistory: StateFlow<List<WholesaleInquiryEntity>>
    val opportunityHistory: StateFlow<List<OpportunityInquiryEntity>>

    val officialWhatsAppNumber = "5493425551234" // RB Preparaciones Santa Fe Contact
    val aliasTransferencia = "RB.PREPARACIONES.MP"
    val cbuTransferencia = "0000003100045678912345"
    val titularTransferencia = "RB Preparaciones - Brian Rosillo"

    init {
        val db = AppDatabase.getDatabase(application)
        repository = RbRepository(db.rbDao())

        customerProfile = repository.customerProfile.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        orderHistory = repository.orders.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        referralData = repository.referralData.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        wholesaleHistory = repository.wholesaleInquiries.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        opportunityHistory = repository.opportunityInquiries.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Initialize referral data if not present
        viewModelScope.launch {
            repository.referralData.collect { ref ->
                if (ref == null) {
                    val initialRef = ReferralDataEntity(
                        id = 1,
                        myReferralCode = "RB-${(1000..9999).random()}",
                        totalReferredFriends = 2,
                        earnedFreeBurgers = 4,
                        unlockedDiscountPercent = 10
                    )
                    repository.saveReferralData(initialRef)
                }
            }
        }

        // Initialize user profile auto-fill if saved
        viewModelScope.launch {
            repository.customerProfile.collect { profile ->
                if (profile != null && _uiState.value.inputAddress.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        inputCustomerName = profile.fullName,
                        inputCustomerPhone = profile.phoneNumber,
                        inputAddress = profile.address,
                        inputNeighborhood = profile.neighborhood.ifBlank { "Santa Fe Centro" },
                        inputDeliveryNotes = profile.deliveryNotes,
                        selectedPaymentMethod = if (profile.defaultPaymentMethod == "TRANSFERENCIA") {
                            PaymentMethod.TRANSFERENCIA
                        } else {
                            PaymentMethod.EFECTIVO
                        }
                    )
                }
            }
        }
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    // Cart operations
    fun updateCartQuantity(packId: String, delta: Int) {
        val currentCart = _uiState.value.cart.toMutableMap()
        val currentQty = currentCart[packId] ?: 0
        val newQty = (currentQty + delta).coerceAtLeast(0)
        if (newQty == 0) {
            currentCart.remove(packId)
        } else {
            currentCart[packId] = newQty
        }
        _uiState.value = _uiState.value.copy(cart = currentCart)
    }

    fun setCartItemQuantity(packId: String, quantity: Int) {
        val currentCart = _uiState.value.cart.toMutableMap()
        if (quantity <= 0) {
            currentCart.remove(packId)
        } else {
            currentCart[packId] = quantity
        }
        _uiState.value = _uiState.value.copy(cart = currentCart)
    }

    fun openCheckoutSheet(packToSelect: PromoPack? = null) {
        if (packToSelect != null) {
            val currentCart = _uiState.value.cart.toMutableMap()
            if ((currentCart[packToSelect.id] ?: 0) == 0) {
                currentCart[packToSelect.id] = 1
            }
            _uiState.value = _uiState.value.copy(cart = currentCart, isCheckoutSheetOpen = true)
        } else {
            if (_uiState.value.cart.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    cart = mapOf("pack_media_caja" to 1),
                    isCheckoutSheetOpen = true
                )
            } else {
                _uiState.value = _uiState.value.copy(isCheckoutSheetOpen = true)
            }
        }
    }

    fun closeCheckoutSheet() {
        _uiState.value = _uiState.value.copy(isCheckoutSheetOpen = false)
    }

    fun toggleOrderHistory(show: Boolean) {
        _uiState.value = _uiState.value.copy(isOrderHistoryOpen = show)
    }

    fun dismissOrderSuccessDialog() {
        _uiState.value = _uiState.value.copy(showOrderSuccessDialog = false)
    }

    fun dismissWholesaleSuccessDialog() {
        _uiState.value = _uiState.value.copy(showWholesaleSuccessDialog = false)
    }

    fun dismissOpportunitySuccessDialog() {
        _uiState.value = _uiState.value.copy(showOpportunitySuccessDialog = false)
    }

    // Customer Form Inputs
    fun onCustomerNameChange(value: String) { _uiState.value = _uiState.value.copy(inputCustomerName = value) }
    fun onCustomerPhoneChange(value: String) { _uiState.value = _uiState.value.copy(inputCustomerPhone = value) }
    fun onAddressChange(value: String) { _uiState.value = _uiState.value.copy(inputAddress = value) }
    fun onNeighborhoodChange(value: String) { _uiState.value = _uiState.value.copy(inputNeighborhood = value) }
    fun onDeliveryNotesChange(value: String) { _uiState.value = _uiState.value.copy(inputDeliveryNotes = value) }
    fun onPaymentMethodChange(method: PaymentMethod) { _uiState.value = _uiState.value.copy(selectedPaymentMethod = method) }
    fun onRememberAddressChange(value: Boolean) { _uiState.value = _uiState.value.copy(rememberAddress = value) }
    fun onCouponCodeChange(value: String) { _uiState.value = _uiState.value.copy(appliedCouponCode = value) }

    fun applyCoupon(context: Context) {
        val code = _uiState.value.appliedCouponCode.trim().uppercase()
        val currentRef = referralData.value
        if (code == "AMIGO" || code == "RBPREP" || (currentRef != null && code == currentRef.myReferralCode)) {
            _uiState.value = _uiState.value.copy(appliedDiscountPercent = 10)
            Toast.makeText(context, "¡Cupón aplicado! 10% de descuento en tu pedido", Toast.LENGTH_SHORT).show()
        } else if (code == "PROMO20") {
            _uiState.value = _uiState.value.copy(appliedDiscountPercent = 20)
            Toast.makeText(context, "¡Cupón PROMO20 aplicado! 20% OFF", Toast.LENGTH_SHORT).show()
        } else if (code.isNotBlank()) {
            _uiState.value = _uiState.value.copy(appliedDiscountPercent = 10)
            Toast.makeText(context, "¡Código de recomendación aceptado! 10% OFF", Toast.LENGTH_SHORT).show()
        }
    }

    fun calculateTotal(): Triple<Int, Double, Double> {
        val cart = _uiState.value.cart
        var totalUnits = 0
        var subtotal = 0.0
        for ((packId, qty) in cart) {
            val pack = _uiState.value.promoPacks.find { it.id == packId } ?: continue
            totalUnits += pack.units * qty
            subtotal += pack.price * qty
        }
        val discountAmount = subtotal * (_uiState.value.appliedDiscountPercent / 100.0)
        val finalTotal = (subtotal - discountAmount).coerceAtLeast(0.0)
        return Triple(totalUnits, subtotal, finalTotal)
    }

    fun submitOrder(context: Context) {
        val state = _uiState.value
        if (state.inputCustomerName.isBlank()) {
            Toast.makeText(context, "Por favor ingresá tu nombre", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.inputAddress.isBlank()) {
            Toast.makeText(context, "Por favor ingresá la dirección de entrega", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.inputCustomerPhone.isBlank()) {
            Toast.makeText(context, "Por favor ingresá un teléfono de contacto", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.cart.isEmpty()) {
            Toast.makeText(context, "Tu pedido está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val (totalUnits, _, finalTotal) = calculateTotal()

        val itemsSummaryList = state.cart.mapNotNull { (packId, qty) ->
            val pack = state.promoPacks.find { it.id == packId }
            if (pack != null) "${qty}x ${pack.title} (${pack.units * qty} hamburguesas)" else null
        }
        val itemsSummary = itemsSummaryList.joinToString(" + ")

        viewModelScope.launch {
            if (state.rememberAddress) {
                repository.saveCustomerProfile(
                    CustomerProfileEntity(
                        id = 1,
                        fullName = state.inputCustomerName.trim(),
                        phoneNumber = state.inputCustomerPhone.trim(),
                        address = state.inputAddress.trim(),
                        neighborhood = state.inputNeighborhood.trim(),
                        deliveryNotes = state.inputDeliveryNotes.trim(),
                        defaultPaymentMethod = state.selectedPaymentMethod.name
                    )
                )
            }

            val newOrder = OrderEntity(
                itemsSummary = itemsSummary,
                totalUnits = totalUnits,
                totalPrice = finalTotal,
                customerName = state.inputCustomerName.trim(),
                customerPhone = state.inputCustomerPhone.trim(),
                deliveryAddress = state.inputAddress.trim(),
                neighborhood = state.inputNeighborhood.trim(),
                deliveryNotes = state.inputDeliveryNotes.trim(),
                paymentMethod = state.selectedPaymentMethod.title
            )

            val orderId = repository.createOrder(newOrder)
            val savedOrder = newOrder.copy(id = orderId)

            _uiState.value = _uiState.value.copy(
                isCheckoutSheetOpen = false,
                lastPlacedOrder = savedOrder,
                showOrderSuccessDialog = true,
                cart = emptyMap() // Clear cart after placing
            )
        }
    }

    // Opportunity Form
    fun onOppNameChange(value: String) { _uiState.value = _uiState.value.copy(oppName = value) }
    fun onOppPhoneChange(value: String) { _uiState.value = _uiState.value.copy(oppPhone = value) }
    fun onOppCityChange(value: String) { _uiState.value = _uiState.value.copy(oppCity = value) }
    fun onOppAvailabilityChange(value: String) { _uiState.value = _uiState.value.copy(oppAvailability = value) }
    fun onOppHasFreezerChange(value: Boolean) { _uiState.value = _uiState.value.copy(oppHasFreezer = value) }
    fun onOppMotivationChange(value: String) { _uiState.value = _uiState.value.copy(oppMotivation = value) }

    fun submitOpportunity(context: Context) {
        val state = _uiState.value
        if (state.oppName.isBlank() || state.oppPhone.isBlank()) {
            Toast.makeText(context, "Completá tu nombre y teléfono para postularte", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            repository.createOpportunityInquiry(
                OpportunityInquiryEntity(
                    name = state.oppName.trim(),
                    phone = state.oppPhone.trim(),
                    cityOrZone = state.oppCity.trim(),
                    availability = state.oppAvailability,
                    hasFreezer = state.oppHasFreezer,
                    motivation = state.oppMotivation.trim()
                )
            )
            _uiState.value = _uiState.value.copy(showOpportunitySuccessDialog = true)
        }
    }

    // Wholesale Form
    fun onWsBusinessNameChange(value: String) { _uiState.value = _uiState.value.copy(wsBusinessName = value) }
    fun onWsContactNameChange(value: String) { _uiState.value = _uiState.value.copy(wsContactName = value) }
    fun onWsPhoneChange(value: String) { _uiState.value = _uiState.value.copy(wsPhone = value) }
    fun onWsEmailChange(value: String) { _uiState.value = _uiState.value.copy(wsEmail = value) }
    fun onWsBusinessTypeChange(value: String) { _uiState.value = _uiState.value.copy(wsBusinessType = value) }
    fun onWsEstimatedBoxesChange(value: String) { _uiState.value = _uiState.value.copy(wsEstimatedBoxes = value) }
    fun onWsCommentsChange(value: String) { _uiState.value = _uiState.value.copy(wsComments = value) }

    fun submitWholesale(context: Context) {
        val state = _uiState.value
        if (state.wsBusinessName.isBlank() || state.wsPhone.isBlank()) {
            Toast.makeText(context, "Por favor ingresá el nombre del negocio y teléfono", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            repository.createWholesaleInquiry(
                WholesaleInquiryEntity(
                    businessName = state.wsBusinessName.trim(),
                    contactName = state.wsContactName.trim(),
                    phone = state.wsPhone.trim(),
                    email = state.wsEmail.trim(),
                    businessType = state.wsBusinessType,
                    estimatedWeeklyBoxes = state.wsEstimatedBoxes,
                    comments = state.wsComments.trim()
                )
            )
            _uiState.value = _uiState.value.copy(showWholesaleSuccessDialog = true)
        }
    }

    // Referral simulation
    fun simulateReferral(context: Context) {
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val updated = repository.addSimulatedReferral(current)
            Toast.makeText(
                context,
                "¡Amigo sumado! Total: ${updated.totalReferredFriends} amigos (+${updated.earnedFreeBurgers} hamburguesas acumuladas)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // WhatsApp Intents
    fun sendOrderToWhatsApp(context: Context, order: OrderEntity) {
        val text = buildString {
            append("🍔 *¡HOLA RB PREPARACIONES! QUIERO CONFIRMAR MI PEDIDO*\n\n")
            append("📋 *Pedido #${order.id}:* ${order.itemsSummary}\n")
            append("📦 *Total unidades:* ${order.totalUnits} hamburguesas\n")
            append("💰 *Total a pagar:* $${String.format("%,.0f", order.totalPrice)}\n")
            append("💳 *Forma de Pago:* ${order.paymentMethod}\n\n")
            append("📍 *Datos de Entrega:*\n")
            append("• Nombre: ${order.customerName}\n")
            append("• Teléfono: ${order.customerPhone}\n")
            append("• Dirección: ${order.deliveryAddress} (${order.neighborhood})\n")
            if (order.deliveryNotes.isNotBlank()) {
                append("• Aclaración: ${order.deliveryNotes}\n")
            }
            append("\n_Enviado desde la App Oficial de RB Preparaciones_")
        }

        openWhatsApp(context, officialWhatsAppNumber, text)
    }

    fun shareReferralWhatsApp(context: Context) {
        val code = referralData.value?.myReferralCode ?: "RB-PROMO"
        val text = "🍔 ¡Hola! Te recomiendo las hamburguesas caseras de pollo de *RB Preparaciones* en Santa Fe. ¡Son riquísimas, rinden un montón y tienen súper precios! Usá mi código *$code* para llevarte 10% OFF o hamburguesas extra en tu primer pedido. ¡Hacé rendir cada peso!"
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Compartir recomendación RB"))
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir app para compartir", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendOpportunityToWhatsApp(context: Context) {
        val state = _uiState.value
        val text = buildString {
            append("👋 *¡HOLA EQUIPO RB PREPARACIONES! QUIERO SUMARME Y GANAR DINERO EXTRA*\n\n")
            append("👤 *Nombre:* ${state.oppName.ifBlank { "Interesado/a" }}\n")
            append("📱 *Teléfono:* ${state.oppPhone}\n")
            append("📍 *Zona/Ciudad:* ${state.oppCity}\n")
            append("⏰ *Disponibilidad:* ${state.oppAvailability}\n")
            append("❄️ *Cuenta con freezer:* ${if (state.oppHasFreezer) "Sí" else "No"}\n")
            if (state.oppMotivation.isNotBlank()) {
                append("💬 *Comentario:* ${state.oppMotivation}\n")
            }
            append("\n_Quiero conocer lista de precios para revendedores y catálogo._")
        }
        openWhatsApp(context, officialWhatsAppNumber, text)
    }

    fun sendWholesaleToWhatsApp(context: Context) {
        val state = _uiState.value
        val text = buildString {
            append("🏢 *SOLICITUD DE ALIANZA CORPORATIVA / PEDIDO MAYORISTA - RB PREPARACIONES*\n\n")
            append("🏪 *Negocio/Empresa:* ${state.wsBusinessName.ifBlank { "Comercio" }}\n")
            append("👤 *Contacto:* ${state.wsContactName}\n")
            append("📱 *Teléfono:* ${state.wsPhone}\n")
            append("🏷️ *Tipo:* ${state.wsBusinessType}\n")
            append("📦 *Volumen estimado:* ${state.wsEstimatedBoxes}\n")
            if (state.wsComments.isNotBlank()) {
                append("💬 *Detalle:* ${state.wsComments}\n")
            }
            append("\n_Solicito lista de precios mayoristas y condiciones comerciales._")
        }
        openWhatsApp(context, officialWhatsAppNumber, text)
    }

    private fun openWhatsApp(context: Context, phone: String, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phone&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}
