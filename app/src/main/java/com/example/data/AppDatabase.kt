package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Trip::class,
        Booking::class,
        ChatMessage::class,
        SavedAddress::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun bookingDao(): BookingDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun savedAddressDao(): SavedAddressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Load SQLCipher libraries
                net.sqlcipher.database.SQLiteDatabase.loadLibs(context.applicationContext)
                
                val dbName = "gotogether_database"
                val dbFile = context.getDatabasePath(dbName)
                if (dbFile.exists()) {
                    try {
                        // Attempt to open the database using SQLCipher to verify key/format integrity
                        val db = net.sqlcipher.database.SQLiteDatabase.openDatabase(
                            dbFile.absolutePath,
                            "GoTogetherSecurePassphrase2026!",
                            null,
                            net.sqlcipher.database.SQLiteDatabase.OPEN_READWRITE
                        )
                        db.close()
                    } catch (e: Exception) {
                        // File exists but is not a valid encrypted SQLCipher database (e.g. was unencrypted).
                        // Delete to allow Room to recreate it securely.
                        context.deleteDatabase(dbName)
                    }
                }

                // Set up the encryption factory
                val passphrase = "GoTogetherSecurePassphrase2026!".toByteArray()
                val factory = net.sqlcipher.database.SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    dbName
                )
                .openHelperFactory(factory)
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: AppDatabase) {
            val userDao = db.userDao()
            val tripDao = db.tripDao()
            val addressDao = db.savedAddressDao()
            val bookingDao = db.bookingDao()
            val chatDao = db.chatMessageDao()

            // 1. Seed Current User (Syafira Aulia)
            val currentUserId = userDao.insertUser(
                User(
                    id = 1,
                    name = "Syafira Aulia",
                    email = "syafiraa@gmail.com",
                    phone = "0812-3456-7890",
                    balance = 250000.0,
                    isDriver = false,
                    avatarUrl = null
                )
            ).toInt()

            // 2. Seed Driver Users (Ahmad Rizky, Dewi Anjani, Budi Santoso)
            val ahmadId = userDao.insertUser(
                User(
                    id = 2,
                    name = "Ahmad Rizky",
                    email = "ahmad.rizky@gmail.com",
                    phone = "0811-2222-3333",
                    balance = 150000.0,
                    isDriver = true,
                    vehicleName = "Toyota Avanza",
                    plateNumber = "BK 1234 ABC",
                    rating = 4.8f
                )
            ).toInt()

            val dewiId = userDao.insertUser(
                User(
                    id = 3,
                    name = "Dewi Anjani",
                    email = "dewi.anjani@gmail.com",
                    phone = "0811-4444-5555",
                    balance = 50000.0,
                    isDriver = true,
                    vehicleName = "Honda Brio",
                    plateNumber = "BK 5678 XYZ",
                    rating = 4.9f
                )
            ).toInt()

            val budiId = userDao.insertUser(
                User(
                    id = 4,
                    name = "Budi Santoso",
                    email = "budi.santoso@gmail.com",
                    phone = "0811-6666-7777",
                    balance = 75000.0,
                    isDriver = true,
                    vehicleName = "Mitsubishi Xpander",
                    plateNumber = "BK 9999 DEF",
                    rating = 4.7f
                )
            ).toInt()

            // 3. Seed Trips for 25 Mei 2024 & 27 Mei 2024
            val trip1Id = tripDao.insertTrip(
                Trip(
                    id = 1,
                    driverId = ahmadId,
                    driverName = "Ahmad Rizky",
                    driverPhone = "0811-2222-3333",
                    vehicleName = "Toyota Avanza",
                    plateNumber = "BK 1234 ABC",
                    origin = "Kampus UMA",
                    destination = "Plaza Medan Fair",
                    date = "25 Mei 2024",
                    time = "08:00",
                    price = 15000.0,
                    availableSeats = 3,
                    totalSeats = 4,
                    status = "Mendatang"
                )
            ).toInt()

            val trip2Id = tripDao.insertTrip(
                Trip(
                    id = 2,
                    driverId = dewiId,
                    driverName = "Dewi Anjani",
                    driverPhone = "0811-4444-5555",
                    vehicleName = "Honda Brio",
                    plateNumber = "BK 5678 XYZ",
                    origin = "Kampus UMA",
                    destination = "Plaza Medan Fair",
                    date = "25 Mei 2024",
                    time = "08:15",
                    price = 15000.0,
                    availableSeats = 2,
                    totalSeats = 4,
                    status = "Mendatang"
                )
            ).toInt()

            val trip3Id = tripDao.insertTrip(
                Trip(
                    id = 3,
                    driverId = budiId,
                    driverName = "Budi Santoso",
                    driverPhone = "0811-6666-7777",
                    vehicleName = "Mitsubishi Xpander",
                    plateNumber = "BK 9999 DEF",
                    origin = "Kampus UMA",
                    destination = "Plaza Medan Fair",
                    date = "25 Mei 2024",
                    time = "08:30",
                    price = 15000.0,
                    availableSeats = 4,
                    totalSeats = 4,
                    status = "Mendatang"
                )
            ).toInt()

            // Trip 4 is scheduled on 27 Mei 2024 to Ringroad City Walks
            val trip4Id = tripDao.insertTrip(
                Trip(
                    id = 4,
                    driverId = ahmadId,
                    driverName = "Ahmad Rizky",
                    driverPhone = "0811-2222-3333",
                    vehicleName = "Toyota Avanza",
                    plateNumber = "BK 1234 ABC",
                    origin = "Kampus UMA",
                    destination = "Ringroad City Walks",
                    date = "27 Mei 2024",
                    time = "17:00",
                    price = 15000.0,
                    availableSeats = 3,
                    totalSeats = 4,
                    status = "Mendatang"
                )
            ).toInt()

            // 4. Seed Saved Addresses
            addressDao.insertAddress(
                SavedAddress(
                    id = 1,
                    userId = currentUserId,
                    label = "Rumah",
                    address = "Jl. Setiabudi No.123, Medan Selayang, Medan, Sumatera Utara",
                    district = "Sumatera Utara",
                    isDefault = true
                )
            )

            addressDao.insertAddress(
                SavedAddress(
                    id = 2,
                    userId = currentUserId,
                    label = "Kampus UMA",
                    address = "Jl. Kolam No.1, Medan Estate, Medan, Sumatera Utara",
                    district = "Sumatera Utara",
                    isDefault = false
                )
            )

            addressDao.insertAddress(
                SavedAddress(
                    id = 3,
                    userId = currentUserId,
                    label = "Kos",
                    address = "Jl. Bunga Raya No.45, Tuntungan, Medan, Sumatera Utara",
                    district = "Sumatera Utara",
                    isDefault = false
                )
            )

            // 5. Seed some chat messages for Trip 1
            chatDao.insertMessage(
                ChatMessage(
                    tripId = 1,
                    senderId = currentUserId,
                    senderName = "Syafira Aulia",
                    message = "Halo kak, saya ingin bergabung di perjalanan besok ya.",
                    timestamp = System.currentTimeMillis() - 600000
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    tripId = 1,
                    senderId = ahmadId,
                    senderName = "Ahmad Rizky",
                    message = "Halo, boleh. Nanti kita bertemu di titik jemput ya.",
                    timestamp = System.currentTimeMillis() - 500000
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    tripId = 1,
                    senderId = currentUserId,
                    senderName = "Syafira Aulia",
                    message = "Baik kak, terima kasih 🙏",
                    timestamp = System.currentTimeMillis() - 400000
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    tripId = 1,
                    senderId = ahmadId,
                    senderName = "Ahmad Rizky",
                    message = "Sama-sama. Sampai jumpa besok!",
                    timestamp = System.currentTimeMillis() - 300000
                )
            )
        }
    }
}
