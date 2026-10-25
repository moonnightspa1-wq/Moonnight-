# 🌙 Moonnight - Luxury In-Home Spa & Wellness Android App

[![Download APK](https://img.shields.io/badge/Download-app--debug.apk-E85D88?style=for-the-badge&logo=android&logoColor=white)](apk/app-debug.apk)

**Moonnight** is a luxury on-demand in-home spa & wellness Android application built with **Kotlin** and **Jetpack Compose**. It features an elegant **Luxury Pink & Gold accent theme** (`#140A14`, `#E85D88`, `#E5B869`), verified female therapists bringing professional spa equipment directly to customer homes, transparent duration-based pricing, and VIP membership perks.

---

## 📲 Direct APK Download Link

Once pushed to your GitHub repository, you can download the APK directly onto your Android device:

### Raw Download URL:
```text
https://github.com/<YOUR-USERNAME>/<YOUR-REPO-NAME>/raw/main/apk/app-debug.apk
```
*(Or via raw CDN:* `https://raw.githubusercontent.com/<YOUR-USERNAME>/<YOUR-REPO-NAME>/main/apk/app-debug.apk`*)*

👉 Simply replace `<YOUR-USERNAME>` and `<YOUR-REPO-NAME>` with your GitHub username and repository name!

---

## ✨ Key Features

1. **User Authentication & Profile:** Fast guest login with Name, Mobile Number, optional Email, and persistent home address.
2. **Top Header & VIP Elite Badge:** Serif luxury branding, lotus emblem, and live VIP status indicator.
3. **Bottom Navigation:** M3 bottom navigation with Home, Services, Bookings, Partner Join, and Profile tabs.
4. **"I Will Come" In-Home Spa Experience:** Highlighting portable luxury massage tables, sterile linens, organic oils, and ambient soundscapes.
   - **Special Banner:** *"Every massage includes a FREE Face Mask! 🎭"*
5. **Curated Service Menu & 30/60/90 Min Pricing Matrix:**
   - **Swedish Massage:** 30 Min (₹1,800) | 60 Min (₹2,800) | 90 Min (₹3,800)
   - **Deep Tissue Massage:** 30 Min (₹2,000) | 60 Min (₹3,200) | 90 Min (₹4,200)
   - **Aromatherapy:** 30 Min (₹2,000) | 60 Min (₹3,000) | 90 Min (₹4,000)
   - **Hot Stone Therapy:** 30 Min (₹2,200) | 60 Min (₹3,500) | 90 Min (₹4,500)
   - **Thai Massage:** 30 Min (₹2,000) | 60 Min (₹3,200) | 90 Min (₹4,200)
   - **Balinese Massage:** 30 Min (₹2,200) | 60 Min (₹3,400) | 90 Min (₹4,400)
   - **Foot Reflexology:** 30 Min (₹1,500) | 60 Min (₹2,200) | 90 Min (₹2,800)
   - **Shiatsu Massage:** 30 Min (₹1,800) | 60 Min (₹2,800) | 90 Min (₹3,600)
   - **Ayurvedic Massage:** 30 Min (₹2,200) | 60 Min (₹3,500) | 90 Min (₹4,500)
6. **Booking System & Calendar:** Instant booking scheduling with date/time picker, address validation, and direct cancel booking option.
7. **⚡ Urgent/Last-Minute Slots Widget:** Real-time pulse indicator showing available slots today with quick booking.
8. **💼 Female Partner Onboarding (₹500 Fee):** Onboarding form with Name, Phone, Age, Experience, Specialty, and photo upload placeholder.
9. **⭐ Verified Customer Reviews:** 5-star ratings with masked phone numbers (`+91 98****1234`) and review submission dialog.
10. **🔒 Hygiene & Privacy Assurance:** Sanitized single-use linen, temperature checks, and 100% customer data privacy.
11. **🎁 Refer & Earn (₹399):** One-tap copyable referral link UI.
12. **👑 V.I.P Elite Membership (₹8,000):** 20% discount on all bookings, priority female therapists, and free cancellation.

---

## 🛠️ Architecture & Tech Stack

- **Language:** 100% Kotlin
- **UI Toolkit:** Jetpack Compose (Material 3)
- **Architecture:** MVVM + Clean Architecture + Repository Pattern
- **Local Persistence:** Room Database (`SpaDatabase`, `SpaDao`)
- **Image & Icon Assets:** Custom Adaptive Launcher Icon & Hero banner
