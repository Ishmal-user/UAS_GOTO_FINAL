package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GoTogetherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    // 1. Splash Screen
                    composable("splash") {
                        SplashScreen(
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. Login Screen
                    composable("login") {
                        LoginScreen(
                            viewModel = viewModel,
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register")
                            },
                            onNavigateToForgotPassword = {
                                navController.navigate("forgot_password")
                            }
                        )
                    }

                    // Register Screen
                    composable("register") {
                        RegisterScreen(
                            viewModel = viewModel,
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // Forgot Password Screen
                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            viewModel = viewModel,
                            onNavigateToOtp = { email ->
                                navController.navigate("otp_verification/$email")
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // OTP Verification Screen
                    composable("otp_verification/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        OtpVerificationScreen(
                            email = email,
                            viewModel = viewModel,
                            onNavigateToResetPassword = { verifiedEmail ->
                                navController.navigate("reset_password/$verifiedEmail") {
                                    popUpTo("forgot_password") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // Reset Password Screen
                    composable("reset_password/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ResetPasswordScreen(
                            email = email,
                            viewModel = viewModel,
                            onNavigateToLogin = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // 3. Main Navigation Hub (Home/Beranda, Perjalanan Saya, Chat, Profil)
                    composable("home") {
                        MainAppScreen(
                            viewModel = viewModel,
                            onNavigateToCariPerjalanan = { navController.navigate("cari_perjalanan") },
                            onNavigateToTawarkanPerjalanan = { navController.navigate("tawarkan_perjalanan") },
                            onNavigateToSavedAddresses = { navController.navigate("alamat_tersimpan") },
                            onNavigateToTopup = { navController.navigate("topup") },
                            onNavigateToSettings = { navController.navigate("pengaturan") },
                            onNavigateToKeamanan = { navController.navigate("keamanan") },
                            onNavigateToBantuan = { navController.navigate("bantuan") },
                            onNavigateToTentang = { navController.navigate("tentang") },
                            onNavigateToRiwayat = { navController.navigate("riwayat_perjalanan") },
                            onNavigateToPembayaran = { navController.navigate("pembayaran") },
                            onNavigateToTripDetail = { tripId ->
                                viewModel.selectTrip(tripId)
                                navController.navigate("detail_perjalanan")
                            },
                            onNavigateToChatDetail = { tripId ->
                                viewModel.selectTrip(tripId)
                                navController.navigate("chat_driver")
                            },
                            onNavigateToBookingDetail = { bookingId ->
                                viewModel.selectBooking(bookingId)
                                navController.navigate("booking_detail")
                            },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onNavigateToDriverTripDetail = { tripId ->
                                viewModel.selectTrip(tripId)
                                navController.navigate("driver_trip_detail")
                            }
                        )
                    }

                    // 4. Cari Perjalanan Screen
                    composable("cari_perjalanan") {
                        CariPerjalananScreen(
                            viewModel = viewModel,
                            onNavigateToHasilPencarian = { navController.navigate("hasil_pencarian") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 5. Hasil Pencarian Screen
                    composable("hasil_pencarian") {
                        HasilPencarianScreen(
                            viewModel = viewModel,
                            onNavigateToTripDetail = { tripId ->
                                viewModel.selectTrip(tripId)
                                navController.navigate("detail_perjalanan")
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 6. Detail Perjalanan Screen
                    composable("detail_perjalanan") {
                        DetailPerjalananScreen(
                            viewModel = viewModel,
                            onNavigateToKonfirmasi = { navController.navigate("konfirmasi_bergabung") },
                            onNavigateToChat = { navController.navigate("chat_driver") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 7. Konfirmasi Bergabung Screen
                    composable("konfirmasi_bergabung") {
                        KonfirmasiBergabungScreen(
                            viewModel = viewModel,
                            onNavigateToSuccess = {
                                navController.navigate("berhasil_bergabung") {
                                    popUpTo("detail_perjalanan") { inclusive = true }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 8. Berhasil Bergabung Screen
                    composable("berhasil_bergabung") {
                        BerhasilBergabungScreen(
                            viewModel = viewModel,
                            onNavigateToTrips = {
                                viewModel.mainSelectedTab.value = 1
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = false }
                                }
                            },
                            onNavigateToChat = {
                                navController.navigate("chat_driver")
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 9. Chat Driver Screen
                    composable("chat_driver") {
                        ChatDriverScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 10. Tawarkan Perjalanan Screen
                    composable("tawarkan_perjalanan") {
                        TawarkanPerjalananScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 10a. Driver Trip Detail Screen
                    composable("driver_trip_detail") {
                        DriverTripDetailScreen(
                            viewModel = viewModel,
                            onNavigateToEdit = { navController.navigate("edit_trip") },
                            onNavigateToChat = { navController.navigate("chat_driver") },
                            onNavigateToCancel = { navController.navigate("batalkan_perjalanan_driver") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 10b. Edit Trip Screen
                    composable("edit_trip") {
                        EditTripScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 10c. Driver Batalkan Perjalanan Screen
                    composable("batalkan_perjalanan_driver") {
                        BatalkanPerjalananDriverScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 11. Booking/Trip Active State Detail Screen
                    composable("booking_detail") {
                        BookingDetailScreen(
                            viewModel = viewModel,
                            onNavigateToRating = { navController.navigate("beri_rating") },
                            onNavigateToChat = { navController.navigate("chat_driver") },
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToCancel = { bookingId ->
                                navController.navigate("batalkan_perjalanan/$bookingId")
                            }
                        )
                    }

                    // 12. Beri Rating & Ulasan Screen
                    composable("beri_rating") {
                        BeriRatingScreen(
                            viewModel = viewModel,
                            onNavigateBack = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = false }
                                }
                            }
                        )
                    }

                    // 13. Alamat Tersimpan Screen
                    composable("alamat_tersimpan") {
                        SavedAddressesScreen(
                            viewModel = viewModel,
                            onNavigateToTambahAlamat = { navController.navigate("tambah_alamat") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 14. Tambah Alamat Screen
                    composable("tambah_alamat") {
                        TambahAlamatScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 15. Top Up Saldo Screen
                    composable("topup") {
                        TopupScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 16. Pengaturan Screen
                    composable("pengaturan") {
                        SettingsScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 17. Keamanan Screen
                    composable("keamanan") {
                        KeamananScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 18. Bantuan Screen
                    composable("bantuan") {
                        BantuanScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 19. Tentang Screen
                    composable("tentang") {
                        TentangScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 20. Riwayat Perjalanan Screen
                    composable("riwayat_perjalanan") {
                        RiwayatPerjalananScreen(
                            viewModel = viewModel,
                            onNavigateToBookingDetail = { bookingId ->
                                viewModel.selectBooking(bookingId)
                                navController.navigate("booking_detail")
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 21. Pembayaran Screen
                    composable("pembayaran") {
                        PembayaranScreen(
                            viewModel = viewModel,
                            onNavigateToTopup = { navController.navigate("topup") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // 22. Batalkan Perjalanan Screen
                    composable("batalkan_perjalanan/{bookingId}") { backStackEntry ->
                        val bookingId = backStackEntry.arguments?.getString("bookingId")?.toIntOrNull() ?: 0
                        BatalkanPerjalananScreen(
                            bookingId = bookingId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
