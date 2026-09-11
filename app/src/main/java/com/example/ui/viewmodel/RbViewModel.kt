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
import com.example.data.model.formatCurrency
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
    val wsComments: String = "",
    // Admin Mode & Rewards Metrics state
    val isAdminMode: Boolean = false,
    val showAdminLoginDialog: Boolean = false,
    val adminPinInput: String = "",
    val adminPinError: String? = null
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

    val officialWhatsAppNumber = "5493425662877" // RB Preparaciones Santa Fe
    val whatsAppDirectUrl = "https://wa.me/message/BZAOF6RLPQOKN1"
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
                        unlockedDiscountPercent = 10,
                        earnedCreditRb = 1500.0
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
        if (state.cart.isEmpty() || state.cart.values.all { it <= 0 }) {
            Toast.makeText(context, "Tu pedido está vacío. Elegí al menos un pack.", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.inputCustomerName.isBlank()) {
            Toast.makeText(context, "Por favor ingresá tu Nombre y Apellido", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.inputCustomerPhone.isBlank()) {
            Toast.makeText(context, "Por favor ingresá tu número de Teléfono / Celular", Toast.LENGTH_SHORT).show()
            return
        }
        if (state.inputAddress.isBlank()) {
            Toast.makeText(context, "Por favor ingresá tu dirección de entrega (Calle y Altura)", Toast.LENGTH_SHORT).show()
            return
        }

        val (totalUnits, subtotal, finalTotal) = calculateTotal()

        val paymentMethodText = when (state.selectedPaymentMethod) {
            PaymentMethod.EFECTIVO -> "Efectivo al Recibir"
            PaymentMethod.TRANSFERENCIA -> "Transferencia / Mercado Pago"
        }

        // Detalle de productos seleccionados para el resumen del pedido
        val selectedPacksList = state.cart.entries.mapNotNull { (packId, qty) ->
            val pack = state.promoPacks.find { it.id == packId }
            if (pack != null && qty > 0) {
                if (qty == 1) {
                    pack.title
                } else {
                    "${qty}x ${pack.title}"
                }
            } else null
        }
        val itemsSummary = if (selectedPacksList.isEmpty()) "Pack Milanesas Caseras" else selectedPacksList.joinToString(" + ")

        val clientName = state.inputCustomerName.trim().ifBlank { "Cliente" }
        val clientPhone = state.inputCustomerPhone.trim().ifBlank { "No informado" }
        val address = state.inputAddress.trim()
        val neighborhood = state.inputNeighborhood.trim().ifBlank { "Santa Fe" }
        val notes = state.inputDeliveryNotes.trim()

        viewModelScope.launch {
            if (state.rememberAddress) {
                repository.saveCustomerProfile(
                    CustomerProfileEntity(
                        id = 1,
                        fullName = clientName,
                        phoneNumber = clientPhone,
                        address = address,
                        neighborhood = neighborhood,
                        deliveryNotes = notes,
                        defaultPaymentMethod = state.selectedPaymentMethod.name
                    )
                )
            }

            val newOrder = OrderEntity(
                itemsSummary = itemsSummary,
                totalUnits = totalUnits,
                totalPrice = finalTotal,
                customerName = clientName,
                customerPhone = clientPhone,
                deliveryAddress = address,
                neighborhood = neighborhood,
                deliveryNotes = notes,
                paymentMethod = paymentMethodText
            )

            val orderId = repository.createOrder(newOrder)
            val savedOrder = newOrder.copy(id = orderId)

            _uiState.value = _uiState.value.copy(
                isCheckoutSheetOpen = false,
                lastPlacedOrder = savedOrder,
                showOrderSuccessDialog = true
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

    // Admin Mode & Recompensa Acumulada Management
    fun openAdminLoginDialog() {
        _uiState.value = _uiState.value.copy(
            showAdminLoginDialog = true,
            adminPinInput = "",
            adminPinError = null
        )
    }

    fun closeAdminLoginDialog() {
        _uiState.value = _uiState.value.copy(
            showAdminLoginDialog = false,
            adminPinInput = "",
            adminPinError = null
        )
    }

    fun onAdminPinChange(input: String) {
        _uiState.value = _uiState.value.copy(
            adminPinInput = input,
            adminPinError = null
        )
    }

    fun submitAdminPin(context: Context): Boolean {
        val pin = _uiState.value.adminPinInput.trim()
        // Default secret PIN for admin/creator: "1234" or "7777" or "admin"
        return if (pin == "1234" || pin.equals("admin", ignoreCase = true) || pin == "7777") {
            _uiState.value = _uiState.value.copy(
                isAdminMode = true,
                showAdminLoginDialog = false,
                adminPinInput = "",
                adminPinError = null
            )
            Toast.makeText(context, "Modo Administrador activado", Toast.LENGTH_SHORT).show()
            true
        } else {
            _uiState.value = _uiState.value.copy(
                adminPinError = "PIN incorrecto. Ingrese el PIN de creador."
            )
            false
        }
    }

    fun exitAdminMode(context: Context) {
        _uiState.value = _uiState.value.copy(isAdminMode = false)
        Toast.makeText(context, "Modo Usuario (Solo Lectura) activado", Toast.LENGTH_SHORT).show()
    }

    // Modify Recompensa Acumulada metrics (Admin only)
    fun modifyReferredFriends(delta: Int) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newFriends = (current.totalReferredFriends + delta).coerceAtLeast(0)
            val updated = current.copy(totalReferredFriends = newFriends)
            repository.saveReferralData(updated)
        }
    }

    fun setReferredFriends(count: Int) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newFriends = count.coerceAtLeast(0)
            val updated = current.copy(totalReferredFriends = newFriends)
            repository.saveReferralData(updated)
        }
    }

    fun modifyEarnedFreeBurgers(delta: Int) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newBurgers = (current.earnedFreeBurgers + delta).coerceAtLeast(0)
            val updated = current.copy(earnedFreeBurgers = newBurgers)
            repository.saveReferralData(updated)
        }
    }

    fun setEarnedFreeBurgers(count: Int) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newBurgers = count.coerceAtLeast(0)
            val updated = current.copy(earnedFreeBurgers = newBurgers)
            repository.saveReferralData(updated)
        }
    }

    fun modifyEarnedCreditRb(delta: Double) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newCredit = (current.earnedCreditRb + delta).coerceAtLeast(0.0)
            val updated = current.copy(earnedCreditRb = newCredit)
            repository.saveReferralData(updated)
        }
    }

    fun setEarnedCreditRb(credit: Double) {
        if (!_uiState.value.isAdminMode) return
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val newCredit = credit.coerceAtLeast(0.0)
            val updated = current.copy(earnedCreditRb = newCredit)
            repository.saveReferralData(updated)
        }
    }

    // Referral simulation
    fun simulateReferral(context: Context) {
        viewModelScope.launch {
            val current = referralData.value ?: ReferralDataEntity(id = 1)
            val updated = repository.addSimulatedReferral(current)
            Toast.makeText(
                context,
                "¡Amigo sumado! Total: ${updated.totalReferredFriends} amigos (+${updated.earnedFreeBurgers} milanesas acumuladas)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // WhatsApp Intents
    fun sendOrderToWhatsApp(context: Context, order: OrderEntity) {
        val formattedTotal = formatCurrency(order.totalPrice)
        val fullAddress = if (order.neighborhood.isNotBlank()) {
            "${order.deliveryAddress}, ${order.neighborhood}"
        } else {
            order.deliveryAddress
        }

        // Estructura exacta requerida:
        // 👋 *¡HOLA EQUIPO RB PREPARACIONES! QUIERO CONFIRMAR MI PEDIDO*
        //
        // 👤 *Nombre:* [Nombre y Apellido ingresado]
        // 📱 *Teléfono:* [Teléfono ingresado]
        // 📍 *Dirección:* [Dirección y Barrio ingresado]
        // 📦 *Pedido:* [1 Kg / Media Caja / Caja Completa seleccionada]
        // 💰 *Total a Pagar:* $[Monto Total Calculado]
        //
        // Por favor, confirmen el horario de entrega. ¡Muchas gracias!
        val message = buildString {
            append("👋 *¡HOLA EQUIPO RB PREPARACIONES! QUIERO CONFIRMAR MI PEDIDO*\n\n")
            append("👤 *Nombre:* ${order.customerName}\n")
            append("📱 *Teléfono:* ${order.customerPhone}\n")
            append("📍 *Dirección:* $fullAddress\n")
            append("📦 *Pedido:* ${order.itemsSummary}\n")
            append("💰 *Total a Pagar:* $formattedTotal\n\n")
            append("Por favor, confirmen el horario de entrega. ¡Muchas gracias!")
        }
        openWhatsAppDirect(context, message)

        // Limpiar el carrito una vez enviado a WhatsApp
        _uiState.value = _uiState.value.copy(cart = emptyMap())
    }

    fun shareReferralWhatsApp(context: Context) {
        val code = referralData.value?.myReferralCode ?: "RB-PROMO"
        val text = "🍗 ¡Hola! Te recomiendo las milanesas caseras de pollo de *RB Preparaciones* en Santa Fe. ¡Son riquísimas, rinden un montón y tienen súper precios! Usá mi código *$code* para llevarte 10% OFF o milanesas extra en tu primer pedido. ¡Hacé rendir cada peso!"
        
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

    fun openWhatsAppDirect(context: Context, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val url = "$whatsAppDirectUrl?text=$encodedMessage"
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
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
