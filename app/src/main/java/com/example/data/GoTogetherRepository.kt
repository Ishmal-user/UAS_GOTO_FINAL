package com.example.data

import kotlinx.coroutines.flow.Flow

class GoTogetherRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val tripDao = database.tripDao()
    private val bookingDao = database.bookingDao()
    private val chatMessageDao = database.chatMessageDao()
    private val savedAddressDao = database.savedAddressDao()

    // --- Users ---
    fun getUserById(id: Int): Flow<User?> = userDao.getUserById(id)
    suspend fun getUserByIdSync(id: Int): User? = userDao.getUserByIdSync(id)
    suspend fun getUserByEmailSync(email: String): User? = userDao.getUserByEmailSync(email)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    fun getAllUsers(): Flow<List<User>> = userDao.getUsers()

    // --- Trips ---
    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()
    fun getTripById(id: Int): Flow<Trip?> = tripDao.getTripById(id)
    suspend fun getTripByIdSync(id: Int): Trip? = tripDao.getTripByIdSync(id)
    suspend fun insertTrip(trip: Trip): Long = tripDao.insertTrip(trip)
    suspend fun updateTrip(trip: Trip) = tripDao.updateTrip(trip)
    fun searchTrips(origin: String, destination: String, date: String): Flow<List<Trip>> =
        tripDao.searchTrips(origin, destination, date)
    fun getTripsByDriver(driverId: Int): Flow<List<Trip>> = tripDao.getTripsByDriver(driverId)

    // --- Bookings ---
    fun getBookingById(id: Int): Flow<Booking?> = bookingDao.getBookingById(id)
    suspend fun getBookingByIdSync(id: Int): Booking? = bookingDao.getBookingByIdSync(id)
    suspend fun insertBooking(booking: Booking): Long = bookingDao.insertBooking(booking)
    suspend fun updateBooking(booking: Booking) = bookingDao.updateBooking(booking)
    fun getBookingsForPassenger(passengerId: Int): Flow<List<Booking>> =
        bookingDao.getBookingsForPassenger(passengerId)
    fun getBookingsForTrip(tripId: Int): Flow<List<Booking>> =
        bookingDao.getBookingsForTrip(tripId)

    // --- Chat Messages ---
    suspend fun insertMessage(message: ChatMessage): Long = chatMessageDao.insertMessage(message)
    fun getMessagesForTrip(tripId: Int): Flow<List<ChatMessage>> =
        chatMessageDao.getMessagesForTrip(tripId)

    // --- Saved Addresses ---
    suspend fun insertAddress(address: SavedAddress): Long = savedAddressDao.insertAddress(address)
    suspend fun updateAddress(address: SavedAddress) = savedAddressDao.updateAddress(address)
    fun getAddressesForUser(userId: Int): Flow<List<SavedAddress>> =
        savedAddressDao.getAddressesForUser(userId)
    suspend fun deleteAddressById(id: Int) = savedAddressDao.deleteAddressById(id)
}
