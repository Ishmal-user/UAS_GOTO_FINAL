package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserByIdSync(id: Int): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmailSync(email: String): User?

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>
}

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: Int): Flow<Trip?>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripByIdSync(id: Int): Trip?

    @Query("SELECT * FROM trips ORDER BY id DESC")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE origin LIKE '%' || :origin || '%' AND destination LIKE '%' || :destination || '%' AND date = :date AND status = 'Mendatang' ORDER BY id DESC")
    fun searchTrips(origin: String, destination: String, date: String): Flow<List<Trip>>

    @Query("SELECT * FROM trips WHERE driverId = :driverId ORDER BY id DESC")
    fun getTripsByDriver(driverId: Int): Flow<List<Trip>>
}

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking): Long

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("SELECT * FROM bookings WHERE id = :id")
    fun getBookingById(id: Int): Flow<Booking?>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingByIdSync(id: Int): Booking?

    @Query("SELECT * FROM bookings WHERE passengerId = :passengerId ORDER BY id DESC")
    fun getBookingsForPassenger(passengerId: Int): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE tripId = :tripId ORDER BY id DESC")
    fun getBookingsForTrip(tripId: Int): Flow<List<Booking>>
}

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Query("SELECT * FROM chat_messages WHERE tripId = :tripId ORDER BY timestamp ASC")
    fun getMessagesForTrip(tripId: Int): Flow<List<ChatMessage>>
}

@Dao
interface SavedAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: SavedAddress): Long

    @Update
    suspend fun updateAddress(address: SavedAddress)

    @Query("SELECT * FROM saved_addresses WHERE userId = :userId ORDER BY id DESC")
    fun getAddressesForUser(userId: Int): Flow<List<SavedAddress>>

    @Query("DELETE FROM saved_addresses WHERE id = :id")
    suspend fun deleteAddressById(id: Int)
}
