package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RbDao {
    // Customer Profile
    @Query("SELECT * FROM customer_profile WHERE id = 1 LIMIT 1")
    fun getCustomerProfile(): Flow<CustomerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomerProfile(profile: CustomerProfileEntity)

    // Orders
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: Long)

    // Referrals
    @Query("SELECT * FROM referral_data WHERE id = 1 LIMIT 1")
    fun getReferralData(): Flow<ReferralDataEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReferralData(data: ReferralDataEntity)

    // Opportunities
    @Query("SELECT * FROM opportunity_inquiries ORDER BY createdAt DESC")
    fun getAllOpportunityInquiries(): Flow<List<OpportunityInquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunityInquiry(inquiry: OpportunityInquiryEntity): Long

    // Wholesale
    @Query("SELECT * FROM wholesale_inquiries ORDER BY createdAt DESC")
    fun getAllWholesaleInquiries(): Flow<List<WholesaleInquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWholesaleInquiry(inquiry: WholesaleInquiryEntity): Long
}
