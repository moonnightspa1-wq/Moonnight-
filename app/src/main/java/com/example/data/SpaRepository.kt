package com.example.data

import android.content.Context
import com.example.R
import com.example.data.local.BookingEntity
import com.example.data.local.PartnerEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.SpaDao
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.CustomerReview
import com.example.model.FemalePartnerApplication
import com.example.model.SpaService
import com.example.model.UserSession
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SpaRepository(
    private val db: FirebaseFirestore,
    private val localDao: SpaDao? = null
) {

    constructor(context: Context, localDao: SpaDao? = null) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        localDao
    )

    val services: List<SpaService> = SpaDataDefaults.services

    fun requireUserId(): String {
        return Firebase.auth.currentUser?.uid ?: error("User must be authenticated")
    }

    suspend fun seedInitialReviewsIfEmpty() {
        localDao?.let { dao ->
            if (dao.getReviewCount() == 0) {
                dao.insertReviews(SpaDataDefaults.sampleReviews.map { ReviewEntity.fromDomain(it) })
            }
        }
    }

    fun getBookings(userId: String): Flow<List<Booking>> = observeBookings(userId)
    fun getPartners(): Flow<List<FemalePartnerApplication>> = observePartners()
    fun getReviews(): Flow<List<CustomerReview>> = observeReviews()

    fun observeBookings(userId: String): Flow<List<Booking>> = callbackFlow {
        val collectionRef = db.collection("users").document(userId).collection("bookings")
        val listener = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, collectionRef.path)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val bookings = snapshot.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        Booking(
                            id = doc.id,
                            serviceId = data["serviceId"] as? String ?: "",
                            serviceTitle = data["serviceTitle"] as? String ?: "",
                            durationMinutes = (data["durationMinutes"] as? Number)?.toInt() ?: 60,
                            pricePaid = (data["pricePaid"] as? Number)?.toInt() ?: 0,
                            date = data["date"] as? String ?: "",
                            timeSlot = data["timeSlot"] as? String ?: "",
                            customerName = data["customerName"] as? String ?: "",
                            customerPhone = data["customerPhone"] as? String ?: "",
                            address = data["address"] as? String ?: "",
                            specialNotes = data["specialNotes"] as? String ?: "",
                            status = try {
                                BookingStatus.valueOf(data["status"] as? String ?: "CONFIRMED")
                            } catch (e: Exception) {
                                BookingStatus.CONFIRMED
                            },
                            freeFaceMaskIncluded = data["freeFaceMaskIncluded"] as? Boolean ?: true,
                            isVipBooking = data["isVipBooking"] as? Boolean ?: false,
                            timestamp = (data["createdAt"] as? Timestamp)?.toDate()?.time ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(bookings)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun createBooking(booking: Booking) {
        val uid = requireUserId()
        val docId = booking.id.ifEmpty { UUID.randomUUID().toString() }
        val docRef = db.collection("users").document(uid).collection("bookings").document(docId)
        val payload = mapOf(
            "userId" to uid,
            "serviceId" to booking.serviceId,
            "serviceTitle" to booking.serviceTitle,
            "durationMinutes" to booking.durationMinutes,
            "pricePaid" to booking.pricePaid,
            "date" to booking.date,
            "timeSlot" to booking.timeSlot,
            "customerName" to booking.customerName,
            "customerPhone" to booking.customerPhone,
            "address" to booking.address,
            "specialNotes" to booking.specialNotes,
            "status" to booking.status.name,
            "freeFaceMaskIncluded" to booking.freeFaceMaskIncluded,
            "isVipBooking" to booking.isVipBooking,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        try {
            docRef.set(payload).await()
            localDao?.insertBooking(BookingEntity.fromDomain(booking.copy(id = docId)))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun cancelBooking(bookingId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("bookings").document(bookingId)
        try {
            docRef.update(
                mapOf(
                    "status" to "CANCELLED",
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            localDao?.updateBookingStatus(bookingId, "CANCELLED")
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    fun observePartners(): Flow<List<FemalePartnerApplication>> = callbackFlow {
        val collectionRef = db.collection("partners")
        val listener = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, collectionRef.path)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    FemalePartnerApplication(
                        id = doc.id,
                        name = data["name"] as? String ?: "",
                        phone = data["phone"] as? String ?: "",
                        age = (data["age"] as? Number)?.toInt() ?: 25,
                        bio = data["bio"] as? String ?: "",
                        experienceYears = (data["experienceYears"] as? Number)?.toInt() ?: 1,
                        specialty = data["specialty"] as? String ?: "",
                        photoUri = data["photoUri"] as? String,
                        feePaidAmount = (data["feePaidAmount"] as? Number)?.toInt() ?: 500,
                        status = data["status"] as? String ?: "Under Verification",
                        submittedAt = (data["createdAt"] as? Timestamp)?.toDate()?.time ?: System.currentTimeMillis()
                    )
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun registerPartner(partner: FemalePartnerApplication) {
        val uid = requireUserId()
        val docId = partner.id.ifEmpty { UUID.randomUUID().toString() }
        val docRef = db.collection("partners").document(docId)
        val payload = mapOf(
            "userId" to uid,
            "name" to partner.name,
            "phone" to partner.phone,
            "age" to partner.age,
            "bio" to partner.bio,
            "experienceYears" to partner.experienceYears,
            "specialty" to partner.specialty,
            "photoUri" to partner.photoUri,
            "feePaidAmount" to partner.feePaidAmount,
            "status" to partner.status,
            "createdAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        try {
            docRef.set(payload).await()
            localDao?.insertPartner(PartnerEntity.fromDomain(partner.copy(id = docId)))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    fun observeReviews(): Flow<List<CustomerReview>> = callbackFlow {
        val collectionRef = db.collection("reviews")
        val listener = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, collectionRef.path)
                trySend(SpaDataDefaults.sampleReviews)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    CustomerReview(
                        id = doc.id,
                        customerName = data["customerName"] as? String ?: "",
                        maskedPhone = data["maskedPhone"] as? String ?: "+91 98XXX XXXXX",
                        rating = (data["rating"] as? Number)?.toInt() ?: 5,
                        serviceName = data["serviceName"] as? String ?: "",
                        comment = data["comment"] as? String ?: "",
                        date = data["date"] as? String ?: "",
                        isVerified = data["isVerified"] as? Boolean ?: true
                    )
                }
                if (list.isEmpty()) {
                    trySend(SpaDataDefaults.sampleReviews)
                } else {
                    trySend(list)
                }
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun addReview(review: CustomerReview) {
        val uid = requireUserId()
        val docId = review.id.ifEmpty { UUID.randomUUID().toString() }
        val docRef = db.collection("reviews").document(docId)
        val payload = mapOf(
            "userId" to uid,
            "customerName" to review.customerName,
            "maskedPhone" to review.maskedPhone,
            "rating" to review.rating,
            "serviceName" to review.serviceName,
            "comment" to review.comment,
            "date" to review.date,
            "isVerified" to review.isVerified,
            "createdAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        try {
            docRef.set(payload).await()
            localDao?.insertReview(ReviewEntity.fromDomain(review.copy(id = docId)))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun saveUserProfile(user: UserSession) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = mapOf(
            "userId" to uid,
            "name" to user.name,
            "phone" to user.phone,
            "email" to user.email,
            "address" to user.address,
            "isVip" to user.isVip,
            "referralCode" to user.referralCode,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }

        try {
            docRef.set(payload, SetOptions.merge()).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    suspend fun getUserProfile(userId: String): UserSession? {
        val docRef = db.collection("users").document(userId)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) {
                val data = snapshot.data ?: return null
                UserSession(
                    userId = userId,
                    name = data["name"] as? String ?: "",
                    phone = data["phone"] as? String ?: "",
                    email = data["email"] as? String ?: "",
                    address = data["address"] as? String ?: "",
                    isVip = data["isVip"] as? Boolean ?: false,
                    isLoggedIn = true,
                    referralCode = data["referralCode"] as? String ?: "MOON399"
                )
            } else {
                null
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            null
        }
    }
}
