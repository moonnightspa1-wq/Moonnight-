package com.example.data

import com.example.data.local.BookingEntity
import com.example.data.local.PartnerEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.SpaDao
import com.example.model.Booking
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication
import com.example.model.SpaService
import com.example.util.SpaUtils
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

    /** @return true if the booking was cancelled, false if it was not in a cancellable state. */
    suspend fun cancelBooking(id: String): Boolean {
        return dao.cancelConfirmedBooking(id) > 0
    }

    fun getPartners(): Flow<List<FemalePartnerApplication>> {
        return dao.getAllPartners().map { list -> list.map { it.toDomain() } }
    }

    suspend fun registerPartner(partner: FemalePartnerApplication) {
        dao.insertPartner(PartnerEntity.fromDomain(partner))
    }

    fun getReviews(): Flow<List<CustomerReview>> {
        return dao.getAllReviews().map { list ->
            if (list.isEmpty()) {
                SpaDataDefaults.sampleReviews
            } else {
                list.map { entity ->
                    val review = entity.toDomain()
                    val ts = reviewTimestamp(review)
                    if (ts > 0L) review.copy(date = SpaUtils.relativeDate(ts)) else review
                }.sortedWith(compareByDescending<CustomerReview> { reviewTimestamp(it) }.thenBy { it.id })
            }
        }
    }

    /** User-written reviews are stored with id "rev_<epochMillis>"; the bundled samples are not. */
    private fun reviewTimestamp(review: CustomerReview): Long =
        review.id.removePrefix("rev_").toLongOrNull()?.takeIf { it > 1_000_000_000_000L } ?: 0L

    suspend fun addReview(review: CustomerReview) {
        dao.insertReview(ReviewEntity.fromDomain(review))
    }

    suspend fun seedInitialReviewsIfEmpty() {
        if (dao.getReviewCount() == 0) {
            dao.insertReviews(SpaDataDefaults.sampleReviews.map { ReviewEntity.fromDomain(it) })
        }
    }
}
