package com.example.data

import com.example.model.CustomerReview
import com.example.model.SpaService

object SpaDataDefaults {

    val services = listOf(
        SpaService(
            id = "swedish",
            title = "Swedish Massage",
            subtitle = "Gentle, flowing strokes",
            description = "Gentle, flowing strokes to improve circulation and tension. Perfect for total relaxation and mind renewal.",
            price30 = 1800,
            price60 = 2800,
            price90 = 3800,
            benefits = listOf("Improves Blood Circulation", "Relieves Gentle Tension", "Enhances Sleep Quality"),
            badge = "Most Popular"
        ),
        SpaService(
            id = "deep_tissue",
            title = "Deep Tissue Massage",
            subtitle = "Targets deep muscle layers",
            description = "Targets deep muscle layers, chronic knots, and postural stress with firm deliberate pressure.",
            price30 = 2000,
            price60 = 3200,
            price90 = 4200,
            benefits = listOf("Breaks Muscle Adhesions", "Relieves Chronic Back Pain", "Speeds Recovery"),
            badge = "Deep Relief"
        ),
        SpaService(
            id = "aromatherapy",
            title = "Aromatherapy",
            subtitle = "Calming pure essential oils",
            description = "Essential oils blended to calm mind and mood, combined with light lymphatic massage techniques.",
            price30 = 2000,
            price60 = 3000,
            price90 = 4000,
            benefits = listOf("Reduces Cortisol & Stress", "Mood Uplift & Clarity", "Silky Nourished Skin"),
            badge = "Soothing Scent"
        ),
        SpaService(
            id = "hot_stone",
            title = "Hot Stone Therapy",
            subtitle = "Warm volcanic basalt stones",
            description = "Warm basalt stones melt away tension, penetrate tight muscle tissue, and expand blood vessels.",
            price30 = 2200,
            price60 = 3500,
            price90 = 4500,
            benefits = listOf("Deep Thermal Penetration", "Accelerated Detoxification", "Transcendent Calm"),
            badge = "Signature Luxury"
        ),
        SpaService(
            id = "thai",
            title = "Thai Massage",
            subtitle = "Stretching & energy lines",
            description = "Stretching and acupressure line work along energy meridians to increase flexibility and vitality.",
            price30 = 2000,
            price60 = 3200,
            price90 = 4200,
            benefits = listOf("Increases Joint Mobility", "Unblocks Energy Sen Lines", "Post-Workout Rebound"),
            badge = "Invigorating"
        ),
        SpaService(
            id = "balinese",
            title = "Balinese Massage",
            subtitle = "Deep pressure & gentle rolling",
            description = "Balancing deep tissue and gentle strokes with skin rolling and warming exotic oils from Bali.",
            price30 = 2200,
            price60 = 3400,
            price90 = 4400,
            benefits = listOf("Relieves Stiff Shoulders", "Boosts Lymphatic Flow", "Holistic Mind Harmony"),
            badge = "Exotic Touch"
        ),
        SpaService(
            id = "foot_reflexology",
            title = "Foot Reflexology",
            subtitle = "Restorative sole acupressure",
            description = "Pressure points on feet mapped to internal organs, releasing fatigue and boosting bodily equilibrium.",
            price30 = 1500,
            price60 = 2200,
            price90 = 2800,
            benefits = listOf("Alleviates Heavy Foot Fatigue", "Stimulates Internal Organs", "Promotes Deep Rest"),
            badge = "Quick Reset"
        ),
        SpaService(
            id = "shiatsu",
            title = "Shiatsu Massage",
            subtitle = "Japanese finger pressure",
            description = "Japanese finger pressure technique applied methodically along meridian channels without oils.",
            price30 = 1800,
            price60 = 2800,
            price90 = 3600,
            benefits = listOf("Restores Natural Ki Energy", "Improves Body Posture", "Eases Tension Headaches"),
            badge = "Traditional Zen"
        ),
        SpaService(
            id = "ayurvedic",
            title = "Ayurvedic Massage",
            subtitle = "Herbal oils & detox techniques",
            description = "Herbal oils & traditional detox techniques tailored to your body dosha for holistic rejuvenation.",
            price30 = 2200,
            price60 = 3500,
            price90 = 4500,
            benefits = listOf("Flushes Deep Toxins (Ama)", "Rebalances Body Doshas", "Nourishes Vital Tissues"),
            badge = "Ancient Wellness"
        )
    )

    fun formatMaskedPhone(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        val last10 = if (digits.length >= 10) digits.takeLast(10) else digits
        return if (last10.length >= 4) {
            val series = last10.take(4)
            "+91 ${series}X XXXXX"
        } else {
            "+91 9820X XXXXX"
        }
    }

    val sampleReviews = listOf(
        CustomerReview(
            id = "rev_1",
            customerName = "Pooja Sharma",
            maskedPhone = "+91 9820X XXXXX",
            rating = 5,
            serviceName = "Swedish Massage (60 Min)",
            comment = "Therapist Priya arrived punctually with a sanitized massage bed, warm lavender oil, and soft music. The complimentary rose face mask was heavenly! Felt like a 7-star resort inside my own bedroom.",
            date = "Yesterday",
            isVerified = true
        ),
        CustomerReview(
            id = "rev_2",
            customerName = "Ananya Desai",
            maskedPhone = "+91 9718X XXXXX",
            rating = 5,
            serviceName = "Aromatherapy (90 Min)",
            comment = "Absolute bliss! Verified female therapist, pristine hygiene kit, and she wore a fresh mask and sanitized everything before stepping in. 10/10 comfort and privacy.",
            date = "2 days ago",
            isVerified = true
        ),
        CustomerReview(
            id = "rev_3",
            customerName = "Rituja Sen",
            maskedPhone = "+91 9934X XXXXX",
            rating = 5,
            serviceName = "Hot Stone Therapy (60 Min)",
            comment = "The hot stones melted my upper back knots completely. Moonnight is my go-to luxury weekend ritual now. Highly recommend the VIP membership!",
            date = "4 days ago",
            isVerified = true
        ),
        CustomerReview(
            id = "rev_4",
            customerName = "Meera Kapoor",
            maskedPhone = "+91 9650X XXXXX",
            rating = 5,
            serviceName = "Balinese Massage (60 Min)",
            comment = "Super respectful, calm demeanor, and completely professional. My mother and I both booked back-to-back sessions. The free face mask was a delightful surprise.",
            date = "Last week",
            isVerified = true
        )
    )

    val timeSlots = listOf(
        "10:00 AM - 11:00 AM",
        "11:30 AM - 12:30 PM",
        "01:00 PM - 02:00 PM",
        "02:30 PM - 03:30 PM",
        "04:00 PM - 05:00 PM",
        "05:30 PM - 06:30 PM",
        "07:00 PM - 08:00 PM",
        "08:30 PM - 09:30 PM"
    )
}
