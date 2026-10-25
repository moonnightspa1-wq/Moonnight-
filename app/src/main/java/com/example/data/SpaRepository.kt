package com.example.data

import com.example.data.local.BookingEntity
import com.example.data.local.PartnerEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.SpaDao
import com.example.model.Booking
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication
import com.example.model.SpaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpaRepository(private val dao: SpaDao) {

    val services: List<SpaService> = SpaDataDefaults.services

    fun getBookings(): Flow<List<Booking>> {
        return dao.getAllBookings().map { list -> list.map { it.toDomain() } }
    }

    suspend fun createBooking(booking: Booking) {
        dao.insertBooking(BookingEntity.fromDomain(booking))
    }

    suspend fun cancelBooking(id: String) {
        dao.updateBookingStatus(id, "CANCELLED")
    }

    fun getPartners(): Flow<List<FemalePartnerApplication>> {
        return dao.getAllPartners().map { list -> list.map { it.toDomain() } }
    }

    suspend fun registerPartner(partner: FemalePartnerApplication) {
        dao.insertPartner(PartnerEntity.fromDomain(partner))
    }

    fun getReviews(): Flow<List<CustomerReview>> {
        return dao.getAllReviews().map { list ->
            if (list.isEmpty()) SpaDataDefaults.sampleReviews else list.map { it.toDomain() }
        }
    }

    suspend fun addReview(review: CustomerReview) {
        dao.insertReview(ReviewEntity.fromDomain(review))
    }

    suspend fun seedInitialReviewsIfEmpty() {
        if (dao.getReviewCount() == 0) {
            dao.insertReviews(SpaDataDefaults.sampleReviews.map { ReviewEntity.fromDomain(it) })
        }
    }
}
