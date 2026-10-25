const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

const PROJECT_ID = "demo-no-project";
const DATABASE_ID = "ai-studio-android-moonnigh-d6bfbede-af04-48ae-9326-74189204e97c";

let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      host: "127.0.0.1",
      port: 8085,
      rules: fs.readFileSync("firestore.rules", "utf8")
    }
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write user profiles", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthedDb.collection("users").doc("user_123").get());
  await assertFails(unauthedDb.collection("users").doc("user_123").set({
    userId: "user_123",
    name: "Priya Sharma",
    phone: "+91 98765 43210",
    createdAt: new Date()
  }));
});

test("Authenticated user can create and read their own profile", async () => {
  const authedDb = testEnv.authenticatedContext("user_123").firestore();
  await assertSucceeds(authedDb.collection("users").doc("user_123").set({
    userId: "user_123",
    name: "Priya Sharma",
    phone: "+91 98765 43210",
    isVip: true,
    referralCode: "MOON399",
    createdAt: new Date()
  }));

  await assertSucceeds(authedDb.collection("users").doc("user_123").get());
});

test("Cross-user access is denied on private user profile", async () => {
  const user1Db = testEnv.authenticatedContext("user_1").firestore();
  const user2Db = testEnv.authenticatedContext("user_2").firestore();

  await assertSucceeds(user1Db.collection("users").doc("user_1").set({
    userId: "user_1",
    name: "Ananya",
    phone: "+91 98765 11111",
    createdAt: new Date()
  }));

  await assertFails(user2Db.collection("users").doc("user_1").get());
});

test("Authenticated user can create a booking in their subcollection", async () => {
  const authedDb = testEnv.authenticatedContext("user_123").firestore();
  await assertSucceeds(authedDb.collection("users").doc("user_123").collection("bookings").doc("booking_001").set({
    userId: "user_123",
    serviceId: "swedish",
    serviceTitle: "Swedish Massage",
    durationMinutes: 60,
    pricePaid: 2800,
    date: "Today, Oct 25",
    timeSlot: "02:30 PM - 03:30 PM",
    customerName: "Priya Sharma",
    customerPhone: "+91 98765 43210",
    address: "Bandra West, Mumbai",
    status: "CONFIRMED",
    freeFaceMaskIncluded: true,
    isVipBooking: false,
    createdAt: new Date()
  }));
});

test("Cross-user booking access is denied", async () => {
  const user1Db = testEnv.authenticatedContext("user_1").firestore();
  const user2Db = testEnv.authenticatedContext("user_2").firestore();

  await assertSucceeds(user1Db.collection("users").doc("user_1").collection("bookings").doc("b_1").set({
    userId: "user_1",
    serviceId: "thai",
    serviceTitle: "Thai Massage",
    durationMinutes: 60,
    pricePaid: 3200,
    date: "Tomorrow",
    timeSlot: "04:00 PM",
    customerName: "User One",
    customerPhone: "+91 98111 22222",
    address: "Delhi",
    status: "CONFIRMED",
    createdAt: new Date()
  }));

  await assertFails(user2Db.collection("users").doc("user_1").collection("bookings").doc("b_1").get());
});

test("Authenticated user can submit review and signed-in users can read reviews", async () => {
  const authorDb = testEnv.authenticatedContext("user_author").firestore();
  const readerDb = testEnv.authenticatedContext("user_reader").firestore();

  await assertSucceeds(authorDb.collection("reviews").doc("rev_001").set({
    userId: "user_author",
    customerName: "Meera Patel",
    maskedPhone: "+91 98XXX XXXXX",
    rating: 5,
    serviceName: "Aromatherapy",
    comment: "Absolutely relaxing! Verified therapist arrived on time.",
    date: "Yesterday",
    isVerified: true,
    createdAt: new Date()
  }));

  await assertSucceeds(readerDb.collection("reviews").doc("rev_001").get());
});
