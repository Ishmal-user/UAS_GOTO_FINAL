package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class GoTogetherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoTogetherRepository

    // Initial user ID is 1 (Syafira Aulia)
    private val _currentUserId = MutableStateFlow(1)
    val currentUserId: StateFlow<Int> = _currentUserId.asStateFlow()

    // Active trip or booking for detail views
    val selectedTripId = MutableStateFlow<Int?>(null)
    val selectedBookingId = MutableStateFlow<Int?>(null)
    val mainSelectedTab = MutableStateFlow(0)
    val selectedRoleInPerjalananTab = MutableStateFlow(0)

    // Search query states
    val searchOrigin = MutableStateFlow("Kampus UMA")
    val searchDestination = MutableStateFlow("Plaza Medan Fair")
    val searchDate = MutableStateFlow("25 Mei 2024")
    val searchTime = MutableStateFlow("08:00")

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = GoTogetherRepository(database)
    }

    // --- Users ---
    val currentUser: StateFlow<User?> = _currentUserId
        .flatMapLatest { id -> repository.getUserById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val sharedPrefs = application.getSharedPreferences("gotogether_prefs", android.content.Context.MODE_PRIVATE)

    // Flow to hold the latest generated OTP and its target email for visual UI simulation
    private val _latestOtpState = MutableStateFlow<Pair<String, String>?>(null) // Pair(email, otp)
    val latestOtpState: StateFlow<Pair<String, String>?> = _latestOtpState.asStateFlow()

    fun loginWithEmail(email: String, passwordEntered: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                onResult(false, "Email tidak boleh kosong.")
                return@launch
            }
            if (passwordEntered.isBlank()) {
                onResult(false, "Password tidak boleh kosong.")
                return@launch
            }
            val user = repository.getUserByEmailSync(email.trim())
            if (user != null) {
                val emailKey = "pwd_" + user.email.lowercase().trim()
                val savedPassword = sharedPrefs.getString(emailKey, null)
                
                if (savedPassword == null) {
                    // First time login for populated user: save this as their password
                    sharedPrefs.edit().putString(emailKey, passwordEntered).apply()
                    _currentUserId.value = user.id
                    onResult(true, "Selamat datang kembali, ${user.name}!")
                } else if (savedPassword == passwordEntered) {
                    _currentUserId.value = user.id
                    onResult(true, "Selamat datang kembali, ${user.name}!")
                } else {
                    onResult(false, "Password salah. Silakan coba lagi.")
                }
            } else {
                onResult(false, "Email belum terdaftar. Silakan daftar terlebih dahulu.")
            }
        }
    }

    fun registerUser(name: String, email: String, phone: String, passwordEntered: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (name.isBlank() || email.isBlank() || phone.isBlank()) {
                onResult(false, "Semua bidang harus diisi.")
                return@launch
            }
            val existing = repository.getUserByEmailSync(email.trim())
            if (existing != null) {
                onResult(false, "Email sudah terdaftar. Silakan masuk.")
            } else {
                val newUser = User(
                    name = name.trim(),
                    email = email.trim(),
                    phone = phone.trim(),
                    balance = 250000.0,
                    isDriver = false
                )
                val newId = repository.insertUser(newUser)
                
                // Save password
                val emailKey = "pwd_" + email.trim().lowercase()
                sharedPrefs.edit().putString(emailKey, passwordEntered).apply()
                
                _currentUserId.value = newId.toInt()
                onResult(true, "Registrasi berhasil! Selamat datang.")
            }
        }
    }

    fun sendOtp(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                onResult(false, "Email tidak boleh kosong.")
                return@launch
            }
            val user = repository.getUserByEmailSync(email.trim())
            if (user == null) {
                onResult(false, "Email belum terdaftar. Silakan gunakan email lain.")
                return@launch
            }
            
            // Generate 4-digit OTP
            val otpCode = (1000..9999).random().toString()
            sharedPrefs.edit().putString("otp_" + email.trim().lowercase(), otpCode).apply()
            
            // Post to latest state so UI can simulate receiving OTP
            _latestOtpState.value = Pair(email.trim().lowercase(), otpCode)
            
            onResult(true, "Kode OTP berhasil dikirim ke $email.")
        }
    }

    fun verifyOtp(email: String, otp: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank() || otp.isBlank()) {
            onResult(false, "Email dan OTP tidak boleh kosong.")
            return
        }
        val savedOtp = sharedPrefs.getString("otp_" + email.trim().lowercase(), null)
        if (savedOtp != null && savedOtp == otp.trim()) {
            onResult(true, "OTP terverifikasi.")
        } else {
            onResult(false, "Kode OTP salah atau kedaluwarsa.")
        }
    }

    fun resetPassword(email: String, newPasswordEntered: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank() || newPasswordEntered.isBlank()) {
            onResult(false, "Email dan password baru tidak boleh kosong.")
            return
        }
        // Save new password
        val emailKey = "pwd_" + email.trim().lowercase()
        sharedPrefs.edit().putString(emailKey, newPasswordEntered).apply()
        
        // Clear OTP state
        sharedPrefs.edit().remove("otp_" + email.trim().lowercase()).apply()
        _latestOtpState.value = null
        
        onResult(true, "Password berhasil diperbarui! Silakan masuk dengan password baru.")
    }

    // --- Saved Addresses ---
    val savedAddresses: StateFlow<List<SavedAddress>> = _currentUserId
        .flatMapLatest { id -> repository.getAddressesForUser(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- All Trips ---
    val allTrips: StateFlow<List<Trip>> = repository.getAllTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Bookings ---
    val passengerBookings: StateFlow<List<Booking>> = _currentUserId
        .flatMapLatest { id -> repository.getBookingsForPassenger(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Trips offered by current user (as driver) ---
    val driverTrips: StateFlow<List<Trip>> = _currentUserId
        .flatMapLatest { id -> repository.getTripsByDriver(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Bookings belonging to the selected trip ---
    val activeTripBookings: StateFlow<List<Booking>> = selectedTripId
        .flatMapLatest { id ->
            if (id != null) repository.getBookingsForTrip(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Active Selected Trip Detail ---
    val activeTrip: StateFlow<Trip?> = selectedTripId
        .flatMapLatest { id ->
            if (id != null) repository.getTripById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- Active Selected Booking Detail ---
    val activeBooking: StateFlow<Booking?> = selectedBookingId
        .flatMapLatest { id ->
            if (id != null) repository.getBookingById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- Chat Messages ---
    val chatMessages: StateFlow<List<ChatMessage>> = selectedTripId
        .flatMapLatest { tripId ->
            if (tripId != null) repository.getMessagesForTrip(tripId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search Results ---
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    val searchResults: StateFlow<List<Trip>> = combine(
        searchOrigin,
        searchDestination,
        searchDate,
        _isSearching
    ) { origin, dest, date, isSearching ->
        Triple(origin, dest, date)
    }.flatMapLatest { (origin, dest, date) ->
        repository.searchTrips(origin, dest, date)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Core Actions ---

    fun executeSearch(origin: String, dest: String, date: String) {
        searchOrigin.value = origin
        searchDestination.value = dest
        searchDate.value = date
        _isSearching.value = true
    }

    fun selectTrip(tripId: Int) {
        selectedTripId.value = tripId
    }

    fun selectBooking(bookingId: Int) {
        selectedBookingId.value = bookingId
    }

    fun addTrip(
        origin: String,
        destination: String,
        date: String,
        time: String,
        seats: Int,
        price: Double
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val trip = Trip(
                driverId = user.id,
                driverName = user.name,
                driverPhone = user.phone,
                vehicleName = user.vehicleName ?: "Mobil Pribadi",
                plateNumber = user.plateNumber ?: "BK 1234 XX",
                origin = origin,
                destination = destination,
                date = date,
                time = time,
                price = price,
                availableSeats = seats,
                totalSeats = seats,
                status = "Mendatang"
            )
            repository.insertTrip(trip)
        }
    }

    fun addTripWithDetails(
        origin: String,
        destination: String,
        date: String,
        time: String,
        vehicleName: String,
        plateNumber: String,
        seats: Int,
        price: Double
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val trip = Trip(
                driverId = user.id,
                driverName = user.name,
                driverPhone = user.phone,
                vehicleName = vehicleName,
                plateNumber = plateNumber,
                origin = origin,
                destination = destination,
                date = date,
                time = time,
                price = price,
                availableSeats = seats,
                totalSeats = seats,
                status = "Mendatang"
            )
            repository.insertTrip(trip)
        }
    }

    fun cancelTrip(trip: Trip) {
        viewModelScope.launch {
            val updatedTrip = trip.copy(status = "Dibatalkan")
            repository.updateTrip(updatedTrip)
        }
    }

    fun updateTripDetails(
        trip: Trip,
        origin: String,
        destination: String,
        date: String,
        time: String,
        vehicleName: String,
        plateNumber: String,
        seats: Int,
        price: Double
    ) {
        viewModelScope.launch {
            val updatedTrip = trip.copy(
                origin = origin,
                destination = destination,
                date = date,
                time = time,
                vehicleName = vehicleName,
                plateNumber = plateNumber,
                availableSeats = seats,
                totalSeats = seats,
                price = price
            )
            repository.updateTrip(updatedTrip)
        }
    }

    fun bookTrip(trip: Trip, seats: Int, paymentMethod: String, onSuccess: (Booking) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val totalPrice = trip.price * seats

            if (paymentMethod == "Saldo GoTogether" && user.balance < totalPrice) {
                onError("Saldo GoTogether tidak mencukupi. Silakan top up.")
                return@launch
            }

            if (trip.availableSeats < seats) {
                onError("Kursi tidak mencukupi.")
                return@launch
            }

            // Deduct balance if using wallet
            if (paymentMethod == "Saldo GoTogether") {
                val updatedUser = user.copy(balance = user.balance - totalPrice)
                repository.updateUser(updatedUser)
            }

            // Create Booking
            val booking = Booking(
                tripId = trip.id,
                passengerId = user.id,
                passengerName = user.name,
                seatsBooked = seats,
                totalPrice = totalPrice,
                paymentMethod = paymentMethod,
                status = "Mendatang"
            )
            val bookingId = repository.insertBooking(booking)

            // Update Trip available seats
            val updatedTrip = trip.copy(availableSeats = trip.availableSeats - seats)
            repository.updateTrip(updatedTrip)

            // Initialize chat space by inserting a welcome message from the driver if there are no messages yet
            val welcomeMsg = ChatMessage(
                tripId = trip.id,
                senderId = trip.driverId,
                senderName = trip.driverName,
                message = "Halo ${user.name}! Terima kasih sudah memesan perjalanan bersama saya. Silakan kabari titik penjemputan Anda di sini ya. 😊"
            )
            repository.insertMessage(welcomeMsg)

            selectedBookingId.value = bookingId.toInt()
            onSuccess(booking.copy(id = bookingId.toInt()))
        }
    }

    fun cancelBooking(booking: Booking, reason: String? = null) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val trip = repository.getTripByIdSync(booking.tripId) ?: return@launch

            // Return balance if paid via wallet
            if (booking.paymentMethod == "Saldo GoTogether" && booking.status == "Mendatang") {
                val updatedUser = user.copy(balance = user.balance + booking.totalPrice)
                repository.updateUser(updatedUser)
            }

            // Update booking status and save cancellation reason
            val updatedBooking = booking.copy(
                status = "Dibatalkan",
                reviewGiven = reason ?: booking.reviewGiven
            )
            repository.updateBooking(updatedBooking)

            // Return available seats
            val updatedTrip = trip.copy(availableSeats = trip.availableSeats + booking.seatsBooked)
            repository.updateTrip(updatedTrip)
        }
    }

    fun updateBookingStatus(booking: Booking, newStatus: String) {
        viewModelScope.launch {
            val updatedBooking = booking.copy(status = newStatus)
            repository.updateBooking(updatedBooking)

            // If trip is completed, also complete the trip status
            val trip = repository.getTripByIdSync(booking.tripId)
            if (trip != null && newStatus == "Selesai") {
                val updatedTrip = trip.copy(status = "Selesai")
                repository.updateTrip(updatedTrip)
            }
        }
    }

    fun submitRating(booking: Booking, rating: Float, review: String) {
        viewModelScope.launch {
            val updatedBooking = booking.copy(ratingGiven = rating, reviewGiven = review)
            repository.updateBooking(updatedBooking)

            // Update driver user's average rating in the database
            val trip = repository.getTripByIdSync(booking.tripId)
            if (trip != null) {
                val driverUser = repository.getUserByIdSync(trip.driverId)
                if (driverUser != null) {
                    val currentRating = driverUser.rating
                    val newRating = ((currentRating * 15f) + rating) / 16f
                    // Format rating to 1 decimal place safely
                    val roundedRating = try {
                        String.format("%.1f", newRating).replace(",", ".").toFloat()
                    } catch (e: Exception) {
                        newRating
                    }
                    val updatedDriver = driverUser.copy(rating = roundedRating)
                    repository.updateUser(updatedDriver)
                }
            }
        }
    }

    fun sendChatMessage(tripId: Int, message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val chatMessage = ChatMessage(
                tripId = tripId,
                senderId = user.id,
                senderName = user.name,
                message = message
            )
            repository.insertMessage(chatMessage)

            val trip = repository.getTripByIdSync(tripId)
            if (trip != null) {
                if (trip.driverId != user.id) {
                    // Current user is a passenger, trigger driver auto-reply
                    delay(1500)
                    val replyText = generateAutoReply(message)
                    val autoReplyMessage = ChatMessage(
                        tripId = tripId,
                        senderId = trip.driverId,
                        senderName = trip.driverName,
                        message = replyText
                    )
                    repository.insertMessage(autoReplyMessage)
                } else {
                    // Current user is the driver, trigger passenger auto-reply!
                    val bookings = repository.getBookingsForTrip(tripId).first()
                    val passenger = bookings.firstOrNull()
                    if (passenger != null) {
                        delay(1500)
                        val replyText = generatePassengerAutoReply(message, passenger.passengerName)
                        val autoReplyMessage = ChatMessage(
                            tripId = tripId,
                            senderId = passenger.passengerId,
                            senderName = passenger.passengerName,
                            message = replyText
                        )
                        repository.insertMessage(autoReplyMessage)
                    }
                }
            }
        }
    }

    private fun generatePassengerAutoReply(text: String, passengerName: String): String {
        val lowerText = text.lowercase()
        return when {
            lowerText.contains("halo") || lowerText.contains("pagi") || lowerText.contains("siang") || lowerText.contains("sore") || lowerText.contains("malam") -> {
                "Halo juga kak! Terima kasih sudah tawarkan perjalanan ini."
            }
            lowerText.contains("berangkat") || lowerText.contains("jam") || lowerText.contains("waktu") -> {
                "Baik kak, saya siap berangkat sesuai jadwal. Nanti kabari ya kalau sudah dekat titik jemput."
            }
            lowerText.contains("jemput") || lowerText.contains("lokasi") || lowerText.contains("posisi") -> {
                "Saya menunggu di titik jemput sesuai aplikasi ya kak, terima kasih!"
            }
            lowerText.contains("ok") || lowerText.contains("siap") || lowerText.contains("baik") || lowerText.contains("oke") -> {
                "Siap kak! Sampai ketemu di jalan."
            }
            else -> "Baik kak, terima kasih informasinya. Saya akan segera bersiap-siap!"
        }
    }

    private fun generateAutoReply(text: String): String {
        val lowerText = text.lowercase()
        val replyList: List<String> = when {
            /* HALO */
            lowerText.contains("halo") || lowerText.contains("hai") || lowerText.contains("hi") -> {
                listOf(
                    "Halo 👋 Ada yang bisa saya bantu?",
                    "Halo juga 😊",
                    "Hai, selamat datang.",
                    "Halo, silakan bertanya ya."
                )
            }
            /* HARGA */
            lowerText.contains("harga") || lowerText.contains("biaya") -> {
                listOf(
                    "Harga sudah sesuai aplikasi 😊",
                    "Tidak ada biaya tambahan.",
                    "Harga sudah final ya."
                )
            }
            /* KURSI */
            lowerText.contains("kursi") || lowerText.contains("seat") -> {
                listOf(
                    "Masih tersedia beberapa kursi.",
                    "Masih ada kursi kosong 😊",
                    "Silakan booking sebelum penuh."
                )
            }
            /* JAM */
            lowerText.contains("jam") || lowerText.contains("berangkat") -> {
                listOf(
                    "Saya berangkat sesuai jadwal.",
                    "Mohon datang 10 menit sebelum berangkat.",
                    "Jangan sampai terlambat ya."
                )
            }
            /* LOKASI */
            lowerText.contains("lokasi") || lowerText.contains("jemput") -> {
                listOf(
                    "Nanti saya kirim lokasi jemput.",
                    "Kita bertemu di titik jemput aplikasi.",
                    "Saya share lokasi sebelum berangkat."
                )
            }
            /* TERIMA KASIH */
            lowerText.contains("makasih") || lowerText.contains("terima kasih") -> {
                listOf(
                    "Sama-sama 😊",
                    "Terima kasih kembali.",
                    "Senang bisa membantu."
                )
            }
            /* DEFAULT */
            else -> {
                listOf(
                    "Baik 😊",
                    "Siap 👍",
                    "Oke, nanti saya kabari.",
                    "Terima kasih atas informasinya.",
                    "Sampai bertemu nanti 🚗"
                )
            }
        }
        return replyList.random()
    }

    fun addSavedAddress(label: String, address: String, district: String, isDefault: Boolean) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val newAddress = SavedAddress(
                userId = user.id,
                label = label,
                address = address,
                district = district,
                isDefault = isDefault
            )
            repository.insertAddress(newAddress)
        }
    }

    fun deleteSavedAddress(addressId: Int) {
        viewModelScope.launch {
            repository.deleteAddressById(addressId)
        }
    }

    fun topUpBalance(amount: Double) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updatedUser = user.copy(balance = user.balance + amount)
            repository.updateUser(updatedUser)
        }
    }

    fun switchRole(isDriver: Boolean, vehicleName: String? = null, plateNumber: String? = null) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updatedUser = user.copy(
                isDriver = isDriver,
                vehicleName = vehicleName ?: user.vehicleName ?: "Toyota Avanza",
                plateNumber = plateNumber ?: user.plateNumber ?: "BK 1234 ABC"
            )
            repository.updateUser(updatedUser)
        }
    }
}
