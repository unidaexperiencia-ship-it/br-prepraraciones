package com.example.data.repository

import com.example.data.local.CustomerProfileEntity
import com.example.data.local.OpportunityInquiryEntity
import com.example.data.local.OrderEntity
import com.example.data.local.RbDao
import com.example.data.local.ReferralDataEntity
import com.example.data.local.WholesaleInquiryEntity
import kotlinx.coroutines.flow.Flow

class RbRepository(private val dao: RbDao) {

    val customerProfile: Flow<CustomerProfileEntity?> = dao.getCustomerProfile()
    val orders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val referralData: Flow<ReferralDataEntity?> = dao.getReferralData()
    val opportunityInquiries: Flow<List<OpportunityInquiryEntity>> = dao.getAllOpportunityInquiries()
    val wholesaleInquiries: Flow<List<WholesaleInquiryEntity>> = dao.getAllWholesaleInquiries()

    suspend fun saveCustomerProfile(profile: CustomerProfileEntity) {
        dao.saveCustomerProfile(profile)
    }

    suspend fun createOrder(order: OrderEntity): Long {
        return dao.insertOrder(order)
    }

    suspend fun deleteOrder(orderId: Long) {
        dao.deleteOrder(orderId)
    }

    suspend fun saveReferralData(data: ReferralDataEntity) {
        dao.saveReferralData(data)
    }

    suspend fun addSimulatedReferral(current: ReferralDataEntity): ReferralDataEntity {
        val newFriends = current.totalReferredFriends + 1
        // For every 2 friends referred, earn +2 free burgers & 10% discount
        val bonusBurgers = (newFriends * 2)
        val discount = (newFriends * 5).coerceAtMost(30)
        val updated = current.copy(
            totalReferredFriends = newFriends,
            earnedFreeBurgers = bonusBurgers,
            unlockedDiscountPercent = discount
        )
        dao.saveReferralData(updated)
        return updated
    }

    suspend fun createOpportunityInquiry(inquiry: OpportunityInquiryEntity): Long {
        return dao.insertOpportunityInquiry(inquiry)
    }

    suspend fun createWholesaleInquiry(inquiry: WholesaleInquiryEntity): Long {
        return dao.insertWholesaleInquiry(inquiry)
    }
}
