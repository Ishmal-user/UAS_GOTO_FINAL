package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val balance: Double = 250000.0,
    val isDriver: Boolean = false,
    val vehicleName: String? = null,
    val plateNumber: String? = null,
    val rating: Float = 4.8f,
    val avatarUrl: String? = null
) : Serializable

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val driverId: Int,
    val driverName: String,
    val driverPhone: String,
    val vehicleName: String,
    val plateNumber: String,
    val origin: String,
    val destination: String,
    val date: String,
    val time: String,
    val price: Double,
    val availableSeats: Int,
    val totalSeats: Int,
    val status: String = "Mendatang" // "Mendatang", "Berlangsung", "Selesai", "Dibatalkan"
) : Serializable

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val passengerId: Int,
    val passengerName: String,
    val seatsBooked: Int,
    val totalPrice: Double,
    val paymentMethod: String = "Saldo GoTogether",
    val status: String = "Mendatang", // "Mendatang", "Berlangsung", "Selesai", "Dibatalkan"
    val ratingGiven: Float? = null,
    val reviewGiven: String? = null
) : Serializable

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val senderId: Int,
    val senderName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "saved_addresses")
data class SavedAddress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val label: String, // "Rumah", "Kampus UMA", "Kos"
    val address: String,
    val district: String,
    val isDefault: Boolean = false
) : Serializable
