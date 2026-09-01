package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_profile")
data class CustomerProfileEntity(
    @PrimaryKey val id: Int = 1, // Single active profile for this device
    val fullName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val neighborhood: String = "Santa Fe Capital",
    val deliveryNotes: String = "",
    val defaultPaymentMethod: String = "EFECTIVO",
    val referralCodeUsed: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemsSummary: String,
    val totalUnits: Int,
    val totalPrice: Double,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val neighborhood: String,
    val deliveryNotes: String,
    val paymentMethod: String,
    val status: String = "En preparación", // En preparación, Enviado, Entregado
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "referral_data")
data class ReferralDataEntity(
    @PrimaryKey val id: Int = 1,
    val myReferralCode: String = "RB-" + (1000..9999).random(),
    val totalReferredFriends: Int = 0,
    val earnedFreeBurgers: Int = 0,
    val unlockedDiscountPercent: Int = 0,
    val completedRedemptions: Int = 0
)

@Entity(tableName = "opportunity_inquiries")
data class OpportunityInquiryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val cityOrZone: String,
    val availability: String,
    val hasFreezer: Boolean,
    val motivation: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wholesale_inquiries")
data class WholesaleInquiryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessName: String,
    val contactName: String,
    val phone: String,
    val email: String,
    val businessType: String,
    val estimatedWeeklyBoxes: String,
    val comments: String,
    val createdAt: Long = System.currentTimeMillis()
)
