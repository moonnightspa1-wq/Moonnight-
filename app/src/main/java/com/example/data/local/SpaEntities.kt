package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val serviceId: String,
    val serviceTitle: String,
    val durationMinutes: Int,
    val pricePaid: Int,
    val date: String,
    val timeSlot: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val specialNotes: String,
    val status: String,
    val freeFaceMaskIncluded: Boolean,
    val isVipBooking: Boolean,
    val timestamp: Long
) {
    fun toDomain(): Booking = Booking(
        id = id,
        serviceId = serviceId,
        serviceTitle = serviceTitle,
        durationMinutes = durationMinutes,
        pricePaid = pricePaid,
        date = date,
        timeSlot = timeSlot,
        customerName = customerName,
        customerPhone = customerPhone,
        address = address,
        specialNotes = specialNotes,
        status = runCatching { BookingStatus.valueOf(status) }.getOrDefault(BookingStatus.CONFIRMED),
        freeFaceMaskIncluded = freeFaceMaskIncluded,
        isVipBooking = isVipBooking,
        timestamp = timestamp
    )

    companion object {
        fun fromDomain(b: Booking): BookingEntity = BookingEntity(
            id = b.id,
            serviceId = b.serviceId,
            serviceTitle = b.serviceTitle,
            durationMinutes = b.durationMinutes,
            pricePaid = b.pricePaid,
            date = b.date,
            timeSlot = b.timeSlot,
            customerName = b.customerName,
            customerPhone = b.customerPhone,
            address = b.address,
            specialNotes = b.specialNotes,
            status = b.status.name,
            freeFaceMaskIncluded = b.freeFaceMaskIncluded,
            isVipBooking = b.isVipBooking,
            timestamp = b.timestamp
        )
    }
}

@Entity(tableName = "partners")
data class PartnerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val age: Int,
    val bio: String,
    val experienceYears: Int,
    val specialty: String,
    val photoUri: String?,
    val feePaidAmount: Int,
    val status: String,
    val submittedAt: Long
) {
    fun toDomain(): FemalePartnerApplication = FemalePartnerApplication(
        id = id,
        name = name,
        phone = phone,
        age = age,
        bio = bio,
        experienceYears = experienceYears,
        specialty = specialty,
        photoUri = photoUri,
        feePaidAmount = feePaidAmount,
        status = status,
        submittedAt = submittedAt
    )

    companion object {
        fun fromDomain(p: FemalePartnerApplication): PartnerEntity = PartnerEntity(
            id = p.id,
            name = p.name,
            phone = p.phone,
            age = p.age,
            bio = p.bio,
            experienceYears = p.experienceYears,
            specialty = p.specialty,
            photoUri = p.photoUri,
            feePaidAmount = p.feePaidAmount,
            status = p.status,
            submittedAt = p.submittedAt
        )
    }
}

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val maskedPhone: String,
    val rating: Int,
    val serviceName: String,
    val comment: String,
    val date: String,
    val isVerified: Boolean
) {
    fun toDomain(): CustomerReview = CustomerReview(
        id = id,
        customerName = customerName,
        maskedPhone = maskedPhone,
        rating = rating,
        serviceName = serviceName,
        comment = comment,
        date = date,
        isVerified = isVerified
    )

    companion object {
        fun fromDomain(r: CustomerReview): ReviewEntity = ReviewEntity(
            id = r.id,
            customerName = r.customerName,
            maskedPhone = r.maskedPhone,
            rating = r.rating,
            serviceName = r.serviceName,
            comment = r.comment,
            date = r.date,
            isVerified = r.isVerified
        )
    }
}
