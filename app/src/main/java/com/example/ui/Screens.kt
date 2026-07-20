package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ==========================================
// BRAND LOGO EMBLEM (CUSTOM DRAWN VIA CANVAS)
// ==========================================
@Composable
fun BrandLogo(modifier: Modifier = Modifier, tint: Color = GreenPrimary) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.size(84.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                
                // Draw 3 smiling passenger heads
                // Left Passenger
                drawCircle(
                    color = tint,
                    radius = w * 0.10f,
                    center = Offset(w * 0.28f, h * 0.32f)
                )
                // Center Passenger (Slightly larger / higher)
                drawCircle(
                    color = tint,
                    radius = w * 0.13f,
                    center = Offset(w * 0.5f, h * 0.24f)
                )
                // Right Passenger
                drawCircle(
                    color = tint,
                    radius = w * 0.10f,
                    center = Offset(w * 0.72f, h * 0.32f)
                )

                // Connected shoulders/bodies line (creating a unified loop of passengers)
                val bodyPath = Path().apply {
                    moveTo(w * 0.12f, h * 0.54f)
                    quadraticTo(w * 0.28f, h * 0.40f, w * 0.40f, h * 0.44f)
                    quadraticTo(w * 0.50f, h * 0.36f, w * 0.60f, h * 0.44f)
                    quadraticTo(w * 0.72f, h * 0.40f, w * 0.88f, h * 0.54f)
                }
                drawPath(
                    path = bodyPath,
                    color = tint,
                    style = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                
                // Car body enclosing them
                val carPath = Path().apply {
                    // Top curve
                    moveTo(w * 0.18f, h * 0.60f)
                    lineTo(w * 0.82f, h * 0.60f)
                    // Right curve
                    quadraticTo(w * 0.94f, h * 0.60f, w * 0.94f, h * 0.70f)
                    lineTo(w * 0.94f, h * 0.78f)
                    // Bottom
                    lineTo(w * 0.06f, h * 0.78f)
                    // Left curve
                    lineTo(w * 0.06f, h * 0.70f)
                    quadraticTo(w * 0.06f, h * 0.60f, w * 0.18f, h * 0.60f)
                }
                drawPath(
                    path = carPath,
                    color = tint
                )

                // White lights on the car emblem (just like the logo)
                drawCircle(
                    color = Color.White,
                    radius = w * 0.05f,
                    center = Offset(Offset(w * 0.22f, h * 0.70f).x, Offset(w * 0.22f, h * 0.70f).y)
                )
                drawCircle(
                    color = Color.White,
                    radius = w * 0.05f,
                    center = Offset(Offset(w * 0.78f, h * 0.70f).x, Offset(w * 0.78f, h * 0.70f).y)
                )
            }
        }
    }
}

// ==========================================
// 1. SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    // Glide and fade animation for the content card
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200, easing = EaseOutCubic),
        label = "logo_fade"
    )
    val offsetYAnim by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 40.dp,
        animationSpec = tween(1200, easing = EaseOutBack),
        label = "logo_offset"
    )

    // Progress bar animation
    var progressVal by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progressVal,
        animationSpec = tween(2200, easing = LinearEasing),
        label = "loading_progress"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        progressVal = 1f
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F9F6)), // Light mint background
        contentAlignment = Alignment.Center
    ) {
        // Fullscreen portrait background illustration
        Image(
            painter = painterResource(id = R.drawable.img_splash_illustration),
            contentDescription = "Background Illustration",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay with gradient to make text on top extremely readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color.White.copy(alpha = 0.75f),
                            Color.Transparent,
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 1200f
                    )
                )
        )

        // Main Header Brand Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 56.dp, start = 24.dp, end = 24.dp)
                .offset(y = offsetYAnim)
                .graphicsLayer(alpha = alphaAnim)
        ) {
            // Elegant brand emblem
            BrandLogo(tint = GreenPrimary)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "GoTogether",
                color = GreenPrimary,
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pergi bersama,\nkota lebih nyaman.",
                color = NeutralDark.copy(alpha = 0.85f),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }

        // Animated loader at the very bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 48.dp)
                .width(180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                color = GreenPrimary,
                trackColor = GreenPrimary.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Menghubungkan komuter...",
                color = NeutralDark.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ==========================================
// 2. LOGIN SCREEN
// ==========================================
@Composable
fun LoginScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("syafiraa@gmail.com") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFDFD))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(28.dp))
            
            // Welcome Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Selamat Datang 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Masuk untuk melanjutkan",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // Centered illustration banner matching Screen 2 of the mockup
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_splash_illustration),
                    contentDescription = "Car Illustration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Email, 
                        contentDescription = "Email",
                        tint = GreenPrimary
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Lock, 
                        contentDescription = "Password",
                        tint = GreenPrimary
                    ) 
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("password_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Lupa Password?",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Masuk button
            Button(
                onClick = {
                    viewModel.loginWithEmail(email, password) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) {
                            onNavigateToHome()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "atau masuk dengan", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))

            // Google sign-in button
            OutlinedButton(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeutralDark)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Google Icon style
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp).padding(end = 8.dp)
                    )
                    Text("Masuk dengan Google", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Belum punya akun? ", color = Color.Gray, fontSize = 15.sp)
                Text(
                    text = "Daftar",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable(onClick = onNavigateToRegister)
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFDFD))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Back button & Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = NeutralDark
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Daftar Akun",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Header info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Ayo Bergabung! 🤝",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lengkapi data diri Anda untuk menikmati perjalanan bersama",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Lengkap") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nama",
                        tint = GreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_name_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = GreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_email_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Input
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Nomor Telepon") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Telepon",
                        tint = GreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_phone_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password (Opsional)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = GreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_password_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedContainerColor = Color(0xFFF6FBF9),
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Daftar Button
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || phone.isBlank()) {
                        Toast.makeText(context, "Harap isi semua bidang wajib (Nama, Email, Telepon)", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.registerUser(name, email, phone, password) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (success) {
                                onNavigateToHome()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("register_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Daftar Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sudah memiliki akun? ", color = Color.Gray, fontSize = 15.sp)
                Text(
                    text = "Masuk",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable(onClick = onNavigateBack)
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

// ==========================================
// Forgot Password, OTP, and Reset Password Screens
// ==========================================

@Composable
fun ForgotPasswordScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToOtp: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Lupa Password", onNavigateBack = onNavigateBack)
        },
        containerColor = Color(0xFFFCFDFD)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Icon Header Illustration
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(GreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Forgot Password Icon",
                    tint = GreenPrimary,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Atur Ulang Password",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Masukkan email terdaftar Anda. Kami akan mengirimkan kode OTP untuk melakukan verifikasi identitas Anda.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Alamat Email") },
                placeholder = { Text("contoh@email.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("forgot_password_email_input"),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = GreenPrimary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (email.isBlank()) {
                        Toast.makeText(context, "Harap masukkan alamat email.", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        viewModel.sendOtp(email) { success, msg ->
                            isLoading = false
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (success) {
                                onNavigateToOtp(email)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("send_otp_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Kirim OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OtpVerificationScreen(
    email: String,
    viewModel: GoTogetherViewModel,
    onNavigateToResetPassword: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val latestOtpState by viewModel.latestOtpState.collectAsState()

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Verifikasi OTP", onNavigateBack = onNavigateBack)
        },
        containerColor = Color(0xFFFCFDFD)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Simulasi SMS Banner
            if (latestOtpState != null && latestOtpState?.first == email.lowercase().trim()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                    border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Simulasi SMS",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Simulasi Email/SMS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "Kode OTP Anda: ${latestOtpState?.second}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NeutralDark
                            )
                        }
                        Button(
                            onClick = {
                                otp = latestOtpState?.second ?: ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF1C5)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Salin", color = Color(0xFFE65100), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(GreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "OTP Icon",
                    tint = GreenPrimary,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Masukkan Kode OTP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kami telah mengirimkan 4 digit kode verifikasi ke email $email. Silakan masukkan kode di bawah ini.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // OTP Input Field
            OutlinedTextField(
                value = otp,
                onValueChange = { input ->
                    if (input.length <= 4) {
                        otp = input.filter { char -> char.isDigit() }
                    }
                },
                label = { Text("Kode OTP (4 Digit)") },
                placeholder = { Text("1234") },
                modifier = Modifier
                    .width(180.dp)
                    .testTag("otp_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Verify Button
            Button(
                onClick = {
                    if (otp.length < 4) {
                        Toast.makeText(context, "Harap masukkan 4 digit kode OTP.", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        viewModel.verifyOtp(email, otp) { success, msg ->
                            isLoading = false
                            if (success) {
                                onNavigateToResetPassword(email)
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("verify_otp_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verifikasi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kirim Ulang OTP Option
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tidak menerima kode? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Kirim Ulang",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        viewModel.sendOtp(email) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ResetPasswordScreen(
    email: String,
    viewModel: GoTogetherViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Password Baru", onNavigateBack = onNavigateBack)
        },
        containerColor = Color(0xFFFCFDFD)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(GreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "New Password Icon",
                    tint = GreenPrimary,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Buat Password Baru",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Silakan tentukan password baru Anda untuk mengamankan akun Anda kembali.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // New Password input
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Password Baru") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_password_input"),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = GreenPrimary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password input
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Konfirmasi Password Baru") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_password_input"),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Confirm",
                        tint = GreenPrimary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeutralDark,
                    unfocusedTextColor = NeutralDark,
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = GreenPrimary
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Semua bidang harus diisi.", Toast.LENGTH_SHORT).show()
                    } else if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Password baru dan konfirmasi tidak cocok.", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        viewModel.resetPassword(email, newPassword) { success, msg ->
                            isLoading = false
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            if (success) {
                                onNavigateToLogin()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_new_password_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Simpan Password Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// MAIN APP CONTENT (WITH BOTTOM NAVIGATION)
// ==========================================
@Composable
fun MainAppScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToCariPerjalanan: () -> Unit,
    onNavigateToTawarkanPerjalanan: () -> Unit,
    onNavigateToSavedAddresses: () -> Unit,
    onNavigateToTopup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToKeamanan: () -> Unit,
    onNavigateToBantuan: () -> Unit,
    onNavigateToTentang: () -> Unit,
    onNavigateToRiwayat: () -> Unit,
    onNavigateToPembayaran: () -> Unit,
    onNavigateToTripDetail: (Int) -> Unit,
    onNavigateToChatDetail: (Int) -> Unit,
    onNavigateToBookingDetail: (Int) -> Unit,
    onLogout: () -> Unit,
    onNavigateToDriverTripDetail: (Int) -> Unit = {}
) {
    val selectedTab by viewModel.mainSelectedTab.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple("Beranda", Icons.Default.Home, Icons.Outlined.Home),
                    Triple("Perjalanan", Icons.Default.DirectionsCar, Icons.Outlined.DirectionsCar),
                    Triple("Chat", Icons.Default.Chat, Icons.Outlined.Chat),
                    Triple("Profil", Icons.Default.Person, Icons.Outlined.Person)
                )
                tabs.forEachIndexed { index, (label, filledIcon, outlinedIcon) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { viewModel.mainSelectedTab.value = index },
                        label = { Text(label) },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) filledIcon else outlinedIcon,
                                contentDescription = label
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            indicatorColor = GreenLight
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> BerandaTab(
                    viewModel = viewModel,
                    onNavigateToCariPerjalanan = onNavigateToCariPerjalanan,
                    onNavigateToTawarkanPerjalanan = onNavigateToTawarkanPerjalanan,
                    onNavigateToTripDetail = onNavigateToTripDetail,
                    onViewAllTrips = { viewModel.mainSelectedTab.value = 1 },
                    onNavigateToChat = { viewModel.mainSelectedTab.value = 2 }
                )
                1 -> PerjalananTab(
                    viewModel = viewModel,
                    onNavigateToBookingDetail = onNavigateToBookingDetail,
                    onNavigateToTripDetail = onNavigateToTripDetail,
                    onNavigateToTawarkanPerjalanan = onNavigateToTawarkanPerjalanan,
                    onNavigateToChatDetail = onNavigateToChatDetail,
                    onNavigateToDriverTripDetail = onNavigateToDriverTripDetail
                )
                2 -> ChatTab(
                    viewModel = viewModel,
                    onNavigateToTripDetail = onNavigateToTripDetail,
                    onNavigateToChatDetail = onNavigateToChatDetail
                )
                3 -> ProfilTab(
                    viewModel = viewModel,
                    onNavigateToRiwayat = onNavigateToRiwayat,
                    onNavigateToPembayaran = onNavigateToPembayaran,
                    onNavigateToSavedAddresses = onNavigateToSavedAddresses,
                    onNavigateToTopup = onNavigateToTopup,
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToKeamanan = onNavigateToKeamanan,
                    onNavigateToBantuan = onNavigateToBantuan,
                    onNavigateToTentang = onNavigateToTentang,
                    onLogout = onLogout
                )
            }
        }
    }
}

// ==========================================
// 3. BERANDA (HOME) TAB
// ==========================================
@Composable
fun BerandaTab(
    viewModel: GoTogetherViewModel,
    onNavigateToCariPerjalanan: () -> Unit,
    onNavigateToTawarkanPerjalanan: () -> Unit,
    onNavigateToTripDetail: (Int) -> Unit,
    onViewAllTrips: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val allTrips by viewModel.allTrips.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFDFD))
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        // Custom Top Header matching Screen 3 of mockup
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hamburger Menu Icon
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu Utama",
                        tint = NeutralDark,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                // Notification Icon with red dot badge
                Box {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifikasi",
                            tint = NeutralDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    // Small Red Badge
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Greeting & Subtitle
            Text(
                text = "Halo, ${user?.name ?: "Syafira"} 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mau pergi ke mana hari ini?",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Cari Perjalanan (Large Forest Green Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToCariPerjalanan),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cari Perjalanan",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Temukan teman perjalanan dengan rute yang sama.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = GreenPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tawarkan Perjalanan (Medium Light Green Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToTawarkanPerjalanan),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E8B57)), // SeaGreen
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tawarkan Perjalanan",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Punya kursi kosong? Ayo berbagi perjalanan!",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Tawarkan",
                            tint = Color(0xFF2E8B57),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Small Two-Column Grid: Perjalanan Saya & Chat
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Perjalanan Saya Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp)
                        .clickable(onClick = onViewAllTrips),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFEBF5EE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Kalender",
                                tint = GreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Perjalanan Saya",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Lihat riwayat perjalananmu.",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Chat Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp)
                        .clickable(onClick = onNavigateToChat),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFEBF5EE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat",
                                tint = GreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Chat",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Chat dengan driver atau penumpang.",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Available Rides List Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rekomendasi Perjalanan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Text(
                    text = "Lihat Semua",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary,
                    modifier = Modifier.clickable(onClick = onViewAllTrips)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (allTrips.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada rekomendasi perjalanan saat ini",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(allTrips.take(3)) { trip ->
                TripListItem(
                    trip = trip,
                    onClick = { onNavigateToTripDetail(trip.id) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// SIMULATED MAP COMPOSABLE (GPS GRAPHIC)
// ==========================================
@Composable
fun SimulatedMap(
    origin: String,
    destination: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, Color(0xFFE2EBE5)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4EE)) // Light green/blue map canvas
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background subtle road lines using Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                
                // Draw grid/city road network
                val strokeWidth = 8f
                val roadColor = Color.White.copy(alpha = 0.8f)
                
                // Horizontal roads
                drawLine(roadColor, Offset(0f, h * 0.25f), Offset(w, h * 0.25f), strokeWidth)
                drawLine(roadColor, Offset(0f, h * 0.55f), Offset(w, h * 0.55f), strokeWidth)
                drawLine(roadColor, Offset(0f, h * 0.85f), Offset(w, h * 0.85f), strokeWidth)
                
                // Vertical roads
                drawLine(roadColor, Offset(w * 0.2f, 0f), Offset(w * 0.2f, h), strokeWidth)
                drawLine(roadColor, Offset(w * 0.55f, 0f), Offset(w * 0.55f, h), strokeWidth)
                drawLine(roadColor, Offset(w * 0.85f, 0f), Offset(w * 0.85f, h), strokeWidth)

                // Draw route between Pin A and Pin B (dashed path)
                val routePath = Path().apply {
                    moveTo(w * 0.25f, h * 0.7f) // Pin A Kampus UMA
                    quadraticTo(w * 0.45f, h * 0.45f, w * 0.8f, h * 0.35f) // Pin B Plaza Medan Fair
                }
                drawPath(
                    path = routePath,
                    color = GreenPrimary,
                    style = Stroke(
                        width = 12f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f),
                        cap = StrokeCap.Round
                    )
                )
            }

            // GPS Simulation Mode Badge
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
                    .border(1.dp, GreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(GreenPrimary, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mode Simulasi GPS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
            }

            // Pin A (Kampus UMA) - Bottom Left-ish
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .border(2.5.dp, GreenPrimary, CircleShape)
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Asal",
                        tint = GreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Text(
                        text = origin,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        color = NeutralDark
                    )
                }
            }

            // Pin B (Plaza Medan Fair) - Top Right-ish
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .border(2.5.dp, WarningRed, CircleShape)
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Tujuan",
                        tint = WarningRed,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Text(
                        text = destination,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        color = NeutralDark
                    )
                }
            }
        }
    }
}

// ==========================================
// 4. CARI PERJALANAN (SEARCH TRIP) SCREEN
// ==========================================
@Composable
fun CariPerjalananScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToHasilPencarian: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var origin by remember { mutableStateOf("Kampus UMA") }
    var destination by remember { mutableStateOf("Plaza Medan Fair") }
    var date by remember { mutableStateOf("25 Mei 2024") }
    var time by remember { mutableStateOf("08:00") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Cari Perjalanan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCFDFD))
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Beautiful visual simulated map on the upper half
                SimulatedMap(
                    origin = origin,
                    destination = destination,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Input Card containing search criteria fields
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        // Origin Location
                        OutlinedTextField(
                            value = origin,
                            onValueChange = { origin = it },
                            label = { Text("Lokasi Asal") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Asal", tint = GreenPrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                focusedLabelColor = GreenPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Destination Location
                        OutlinedTextField(
                            value = destination,
                            onValueChange = { destination = it },
                            label = { Text("Tujuan") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Tujuan", tint = WarningRed) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                focusedLabelColor = GreenPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Date
                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text("Tanggal") },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Tanggal", tint = Color.Gray) },
                                modifier = Modifier
                                    .weight(1.1f)
                                    .padding(end = 8.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = NeutralDark,
                                    unfocusedTextColor = NeutralDark,
                                    focusedBorderColor = GreenPrimary,
                                    focusedLabelColor = GreenPrimary
                                ),
                                singleLine = true
                            )
                            // Time
                            OutlinedTextField(
                                value = time,
                                onValueChange = { time = it },
                                label = { Text("Jam") },
                                leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = "Jam", tint = Color.Gray) },
                                modifier = Modifier.weight(0.9f),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = NeutralDark,
                                    unfocusedTextColor = NeutralDark,
                                    focusedBorderColor = GreenPrimary,
                                    focusedLabelColor = GreenPrimary
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.executeSearch(origin, destination, date)
                    onNavigateToHasilPencarian()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("cari_perjalanan_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Cari Perjalanan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ==========================================
// 5. HASIL PENCARIAN (SEARCH RESULTS) SCREEN
// ==========================================
@Composable
fun HasilPencarianScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToTripDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val origin by viewModel.searchOrigin.collectAsState()
    val destination by viewModel.searchDestination.collectAsState()
    val date by viewModel.searchDate.collectAsState()

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Hasil Pencarian", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
        ) {
            // Header showing queried route
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Origin", tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = origin, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = "Ke", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.LocationOn, contentDescription = "Destination", tint = WarningRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = destination, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = date, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(start = 28.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Map preview card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GrayBorder)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    RouteMapCanvas(origin = origin, destination = destination)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ditemukan ${searchResults.size} perjalanan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (searchResults.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Maaf, tidak ada perjalanan yang cocok.", color = Color.Gray)
                        }
                    }
                } else {
                    items(searchResults) { trip ->
                        TripListItem(
                            trip = trip,
                            onClick = {
                                viewModel.selectTrip(trip.id)
                                onNavigateToTripDetail(trip.id)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. DETAIL PERJALANAN (TRIP DETAILS) SCREEN
// ==========================================
@Composable
fun DetailPerjalananScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToKonfirmasi: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Detail Perjalanan", onNavigateBack = onNavigateBack)
        },
        bottomBar = {
            if (trip != null) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToChat,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                            border = BorderStroke(1.5.dp, GreenPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Chat, contentDescription = "Chat")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chat", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onNavigateToKonfirmasi,
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp)
                                .testTag("gabung_trip_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Gabung", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (trip == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentTrip = trip!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Map preview
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    RouteMapCanvas(origin = currentTrip.origin, destination = currentTrip.destination)
                }

                // Trip general details
                Column(modifier = Modifier.padding(16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Driver Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .background(GreenLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Driver",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = currentTrip.driverName, fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 16.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = StarYellow, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "4.8 (32)", fontSize = 13.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(Color.Gray, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Online", fontSize = 13.sp, color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = GrayBorder)

                            // Vehicle Details
                            InfoRow(icon = Icons.Default.DirectionsCar, label = "Mobil", value = currentTrip.vehicleName)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.DirectionsCar, label = "Plat Nomor", value = currentTrip.plateNumber)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Asal", value = currentTrip.origin)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Tujuan", value = currentTrip.destination)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.CalendarToday, label = "Tanggal", value = currentTrip.date)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.AccessTime, label = "Jam Berangkat", value = currentTrip.time)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.Person, label = "Kursi Tersedia", value = "${currentTrip.availableSeats} kursi")
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.Payment, label = "Harga per kursi", value = "Rp ${String.format("%,.0f", currentTrip.price)}")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. KONFIRMASI BERGABUNG SCREEN
// ==========================================
@Composable
fun KonfirmasiBergabungScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var seatsSelected by remember { mutableStateOf(1) }
    var paymentMethod by remember { mutableStateOf("Saldo GoTogether") }
    var agreeToTerms by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Konfirmasi Bergabung", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (trip == null || user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentTrip = trip!!
            val currentUserData = user!!
            val totalPrice = currentTrip.price * seatsSelected

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Pastikan detail perjalanan Anda",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Main Journey Details Card (Screen 3 style)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Driver Row with Avatar and Rating
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dynamic Initials Avatar
                                val initials = currentTrip.driverName.split(" ")
                                    .mapNotNull { it.firstOrNull() }
                                    .joinToString("")
                                    .take(2)
                                    .uppercase()

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(GreenLight, CircleShape)
                                        .border(1.5.dp, GreenPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initials,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Driver",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = currentTrip.driverName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeutralDark
                                    )
                                }
                                // Simulated driver rating matching Screen 2
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(GreenLight, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = StarYellow,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "4.8",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = GrayBorder.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Details List with precise Icons
                            DetailRowWithIcon(icon = Icons.Default.DirectionsCar, label = "Mobil", value = currentTrip.vehicleName)
                            DetailRowWithIcon(icon = Icons.Default.ConfirmationNumber, label = "Plat Nomor", value = currentTrip.plateNumber)
                            DetailRowWithIcon(icon = Icons.Default.LocationOn, label = "Asal", value = currentTrip.origin)
                            DetailRowWithIcon(icon = Icons.Default.LocationOn, label = "Tujuan", value = currentTrip.destination, isWarning = true)
                            DetailRowWithIcon(icon = Icons.Default.CalendarToday, label = "Tanggal", value = currentTrip.date)
                            DetailRowWithIcon(icon = Icons.Default.AccessTime, label = "Jam Berangkat", value = currentTrip.time)
                            DetailRowWithIcon(icon = Icons.Default.LocalOffer, label = "Harga per kursi", value = "Rp ${String.format("%,.0f", currentTrip.price)}")
                            DetailRowWithIcon(icon = Icons.Default.Group, label = "Kursi Tersedia", value = "${currentTrip.availableSeats} kursi")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Seats selector card (Compact & modern)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Jumlah Kursi", fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 14.sp)
                                Text(text = "Pilih kapasitas kursi pesanan Anda", fontSize = 11.sp, color = Color.Gray)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(NeutralLight, RoundedCornerShape(24.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { if (seatsSelected > 1) seatsSelected-- },
                                    enabled = seatsSelected > 1,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Kurangi",
                                        tint = if (seatsSelected > 1) GreenPrimary else Color.LightGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "$seatsSelected",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    onClick = { if (seatsSelected < currentTrip.availableSeats) seatsSelected++ },
                                    enabled = seatsSelected < currentTrip.availableSeats,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah",
                                        tint = if (seatsSelected < currentTrip.availableSeats) GreenPrimary else Color.LightGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment method card (Screen 3 style)
                    Text(text = "Metode Pembayaran", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Option 1: Tunai
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = "Tunai" }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "Tunai",
                                    onClick = { paymentMethod = "Tunai" },
                                    colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Tunai", 
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = NeutralDark
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = "Tunai",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Divider(color = GrayBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 12.dp))

                            // Option 2: Saldo GoTogether
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = "Saldo GoTogether" }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "Saldo GoTogether",
                                    onClick = { paymentMethod = "Saldo GoTogether" },
                                    colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = "Saldo GoTogether", 
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = NeutralDark
                                        )
                                        Text(
                                            text = "Saldo: Rp ${String.format("%,.0f", currentUserData.balance)}", 
                                            fontSize = 12.sp, 
                                            color = GreenSecondary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = "Wallet",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Aturan Perjalanan Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { agreeToTerms = !agreeToTerms }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = agreeToTerms,
                            onCheckedChange = { agreeToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = GreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Saya setuju dengan aturan perjalanan", 
                            fontSize = 13.sp, 
                            color = NeutralDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Bottom Row of buttons (Screen 3 style)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    border = BorderStroke(1.dp, GrayBorder.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Total display matching design (optional helper)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Pembayaran (${seatsSelected} Kursi)",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Rp ${String.format("%,.0f", totalPrice)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenSecondary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.Gray)
                            ) {
                                Text(text = "Batalkan", color = Color.Gray, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    if (!agreeToTerms) {
                                        Toast.makeText(context, "Anda harus menyetujui aturan perjalanan", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    viewModel.bookTrip(
                                        trip = currentTrip,
                                        seats = seatsSelected,
                                        paymentMethod = paymentMethod,
                                        onSuccess = { _ ->
                                            Toast.makeText(context, "Pemesanan berhasil! Notifikasi dikirim ke Driver ${currentTrip.driverName}.", Toast.LENGTH_LONG).show()
                                            onNavigateToSuccess()
                                        },
                                        onError = { errorMsg ->
                                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(48.dp)
                                    .testTag("confirm_booking_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp),
                                enabled = agreeToTerms
                            ) {
                                Text("Konfirmasi Gabung", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper composable for Detail items in Confirm Screen
@Composable
fun DetailRowWithIcon(
    icon: ImageVector,
    label: String,
    value: String,
    isWarning: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    if (isWarning) Color(0xFFFDE8E8) else GreenLight,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isWarning) WarningRed else GreenPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark
            )
        }
    }
}

// ==========================================
// 8. BERHASIL BERGABUNG (SUCCESS) SCREEN
// ==========================================
@Composable
fun BerhasilBergabungScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToTrips: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val activeBooking by viewModel.activeBooking.collectAsState()
    val activeTrip by viewModel.activeTrip.collectAsState()

    val driverName = activeTrip?.driverName ?: "Ahmad Rizky"
    val origin = activeTrip?.origin ?: "Kampus UMA"
    val destination = activeTrip?.destination ?: "Plaza Medan Fair"
    val date = activeTrip?.date ?: "25 Mei 2024"
    val time = activeTrip?.time ?: "08:00"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFDFD))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Success checkmark icon (matching screen 4 badge)
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Color(0xFFE8F5E9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(GreenPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Berhasil Bergabung!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NeutralDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Permintaan Anda telah diterima. Driver akan melihat bahwa Anda bergabung pada perjalanan ini.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Detail Perjalanan Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Detail Perjalanan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Driver
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Driver",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Driver", fontSize = 11.sp, color = Color.Gray)
                        Text(driverName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Asal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE3F2FD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Asal",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Asal", fontSize = 11.sp, color = Color.Gray)
                        Text(origin, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tujuan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFFF3E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Tujuan",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tujuan", fontSize = 11.sp, color = Color.Gray)
                        Text(destination, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tanggal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF3E5F5), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Tanggal",
                            tint = Color(0xFF7B1FA2),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tanggal", fontSize = 11.sp, color = Color.Gray)
                        Text(date, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Jam Berangkat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Jam Berangkat",
                            tint = GreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Jam Berangkat", fontSize = 11.sp, color = Color.Gray)
                        Text(time, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = Color(0xFFECEFF1))

                Spacer(modifier = Modifier.height(14.dp))

                // Status: Menunggu Keberangkatan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF9C4), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Menunggu Keberangkatan",
                            color = Color(0xFFF57F17),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Chat Driver Button (Primary action in screen 4)
        Button(
            onClick = {
                // Select active trip so we can chat
                activeBooking?.let { b ->
                    viewModel.selectTrip(b.tripId)
                }
                onNavigateToChat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("success_chat_driver_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Chat Driver", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lihat Perjalanan Saya Button (Secondary action)
        OutlinedButton(
            onClick = onNavigateToTrips,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("success_confirm_btn"),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFB0BEC5))
        ) {
            Text(
                text = "Lihat Perjalanan Saya",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF455A64)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==========================================
// 9. CHAT DRIVER / CHAT DETAIL SCREEN
// ==========================================
@Composable
fun ChatDriverScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val bookings by viewModel.activeTripBookings.collectAsState()
    var messageText by remember { mutableStateOf("") }
    
    val chatPartnerName = if (trip != null && user != null && trip!!.driverId == user!!.id) {
        bookings.firstOrNull()?.passengerName ?: "Dewi Anjani"
    } else {
        trip?.driverName ?: "Ahmad Rizky"
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = NeutralDark
                        )
                    }
                    
                    // Circular Avatar with GreenLight background
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = GreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = chatPartnerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NeutralDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF34A853), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Online",
                                fontSize = 12.sp,
                                color = Color(0xFF34A853),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    IconButton(onClick = { /* Additional menu options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = NeutralDark
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
        ) {
            // Chat Bubble List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chatMessages) { msg ->
                    val isCurrentUser = msg.senderId == viewModel.currentUserId.value
                    ChatBubble(msg = msg, isCurrentUser = isCurrentUser)
                }
            }

            // Quick reply template chips matching passenger requirements (Step 6 of flow)
            val quickReplies = listOf(
                "Tentukan titik penjemputan 📍",
                "Konfirmasi jam keberangkatan ⏰",
                "Saya sudah sampai di lokasi 👋",
                "Saya sedikit terlambat 🙏"
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickReplies) { reply ->
                    SuggestionChip(
                        onClick = {
                            if (trip != null) {
                                viewModel.sendChatMessage(trip!!.id, reply)
                            }
                        },
                        label = { 
                            Text(
                                text = reply, 
                                fontSize = 12.sp, 
                                fontWeight = FontWeight.SemiBold,
                                color = GreenSecondary
                            ) 
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = GreenLight
                        ),
                        border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // Input Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Ketik pesan...", color = Color.Gray, fontSize = 15.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_text"),
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            IconButton(onClick = { /* Attachment action */ }) {
                                Icon(
                                    imageVector = Icons.Default.Attachment,
                                    contentDescription = "Lampiran",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeutralDark,
                            unfocusedTextColor = NeutralDark,
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (trip != null && messageText.isNotBlank()) {
                                viewModel.sendChatMessage(trip!!.id, messageText)
                                messageText = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(GreenPrimary, CircleShape)
                            .testTag("send_chat_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Kirim",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 10. TAWARKAN PERJALANAN (OFFER TRIP) SCREEN - MULTI-STEP FLOW
// ==========================================
@Composable
fun TawarkanPerjalananScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()

    var currentStep by remember { mutableStateOf(1) } // 1: Form, 2: Preview, 3: Loading, 4: Success

    var origin by remember { mutableStateOf("Kampus UMA") }
    var destination by remember { mutableStateOf("Plaza Medan Fair") }
    var date by remember { mutableStateOf("25 Mei 2024") }
    var time by remember { mutableStateOf("08:00") }
    var vehicle by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf(4) }
    var price by remember { mutableStateOf("15000") }

    var progressPercent by remember { mutableStateOf(0) }

    LaunchedEffect(user) {
        if (user != null) {
            vehicle = user!!.vehicleName ?: "Toyota Avanza"
            plate = user!!.plateNumber ?: "BK 1234 ABC"
        }
    }

    if (currentStep == 3) {
        LaunchedEffect(Unit) {
            for (p in 0..100 step 5) {
                progressPercent = p
                delay(80)
            }
            viewModel.addTripWithDetails(
                origin = origin,
                destination = destination,
                date = date,
                time = time,
                vehicleName = vehicle,
                plateNumber = plate,
                seats = seats,
                price = price.toDoubleOrNull() ?: 15000.0
            )
            currentStep = 4
        }
    }

    Scaffold(
        topBar = {
            val title = when (currentStep) {
                1 -> "Tawarkan Perjalanan"
                2 -> "Preview Perjalanan"
                3 -> "Mempublikasikan..."
                else -> "Berhasil!"
            }
            OptTopAppBar(
                title = title,
                onNavigateBack = {
                    if (currentStep == 2) {
                        currentStep = 1
                    } else if (currentStep == 1) {
                        onNavigateBack()
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCFDFD))
                .padding(paddingValues)
        ) {
            when (currentStep) {
                1 -> {
                    // STEP 1: FORM WITH FIXED BOTTOM BUTTON
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            SimulatedMap(
                                origin = origin,
                                destination = destination,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    OutlinedTextField(
                                        value = origin,
                                        onValueChange = { origin = it },
                                        label = { Text("Lokasi Asal") },
                                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Asal", tint = GreenPrimary) },
                                        modifier = Modifier.fillMaxWidth().testTag("offer_origin_input"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NeutralDark,
                                            unfocusedTextColor = NeutralDark,
                                            focusedBorderColor = GreenPrimary,
                                            focusedLabelColor = GreenPrimary
                                        ),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = destination,
                                        onValueChange = { destination = it },
                                        label = { Text("Tujuan") },
                                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Tujuan", tint = WarningRed) },
                                        modifier = Modifier.fillMaxWidth().testTag("offer_dest_input"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NeutralDark,
                                            unfocusedTextColor = NeutralDark,
                                            focusedBorderColor = GreenPrimary,
                                            focusedLabelColor = GreenPrimary
                                        ),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = date,
                                            onValueChange = { date = it },
                                            label = { Text("Tanggal") },
                                            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Tanggal", tint = Color.Gray) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 8.dp),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = NeutralDark,
                                                unfocusedTextColor = NeutralDark,
                                                focusedBorderColor = GreenPrimary,
                                                focusedLabelColor = GreenPrimary
                                            ),
                                            singleLine = true
                                        )
                                        OutlinedTextField(
                                            value = time,
                                            onValueChange = { time = it },
                                            label = { Text("Jam") },
                                            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = "Jam", tint = Color.Gray) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = NeutralDark,
                                                unfocusedTextColor = NeutralDark,
                                                focusedBorderColor = GreenPrimary,
                                                focusedLabelColor = GreenPrimary
                                            ),
                                            singleLine = true
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        OutlinedTextField(
                                            value = vehicle,
                                            onValueChange = { vehicle = it },
                                            label = { Text("Mobil") },
                                            leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Mobil", tint = Color.Gray) },
                                            modifier = Modifier
                                                .weight(1.2f)
                                                .padding(end = 8.dp),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = NeutralDark,
                                                unfocusedTextColor = NeutralDark,
                                                focusedBorderColor = GreenPrimary,
                                                focusedLabelColor = GreenPrimary
                                            ),
                                            singleLine = true
                                        )
                                        OutlinedTextField(
                                            value = plate,
                                            onValueChange = { plate = it },
                                            label = { Text("Plat Nomor") },
                                            leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = "Plat", tint = Color.Gray) },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = NeutralDark,
                                                unfocusedTextColor = NeutralDark,
                                                focusedBorderColor = GreenPrimary,
                                                focusedLabelColor = GreenPrimary
                                            ),
                                            singleLine = true
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Jumlah Kursi", fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 14.sp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = { if (seats > 1) seats-- },
                                                modifier = Modifier.background(Color(0xFFF0F4F2), CircleShape).size(36.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Kurang", tint = NeutralDark, modifier = Modifier.size(18.dp))
                                            }
                                            Text(text = "$seats", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 14.dp), fontSize = 16.sp)
                                            IconButton(
                                                onClick = { if (seats < 6) seats++ },
                                                modifier = Modifier.background(Color(0xFFF0F4F2), CircleShape).size(36.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = NeutralDark, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val currentPrice = price.toDoubleOrNull() ?: 0.0
                                                if (currentPrice >= 1000.0) {
                                                    price = (currentPrice - 1000.0).toInt().toString()
                                                } else {
                                                    price = "0"
                                                }
                                            },
                                            modifier = Modifier
                                                .background(Color(0xFFF0F4F2), CircleShape)
                                                .size(36.dp)
                                                .testTag("price_decrease_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Kurang Harga",
                                                tint = NeutralDark,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        OutlinedTextField(
                                            value = price,
                                            onValueChange = { price = it },
                                            label = { Text("Harga per kursi (Rp)") },
                                            leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = "Harga", tint = GreenPrimary) },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = NeutralDark,
                                                unfocusedTextColor = NeutralDark,
                                                focusedBorderColor = GreenPrimary,
                                                focusedLabelColor = GreenPrimary
                                            ),
                                            singleLine = true
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        IconButton(
                                            onClick = {
                                                val currentPrice = price.toDoubleOrNull() ?: 0.0
                                                price = (currentPrice + 1000.0).toInt().toString()
                                            },
                                            modifier = Modifier
                                                .background(Color(0xFFF0F4F2), CircleShape)
                                                .size(36.dp)
                                                .testTag("price_increase_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Tambah Harga",
                                                tint = NeutralDark,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Fixed footer for Step 1
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            tonalElevation = 8.dp,
                            shadowElevation = 8.dp
                        ) {
                            Button(
                                onClick = { currentStep = 2 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .height(52.dp)
                                    .testTag("continue_to_preview_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(14.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                            ) {
                                Text("Lanjutkan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
                2 -> {
                    // STEP 2: PREVIEW WITH FIXED BOTTOM BUTTON
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            SimulatedMap(
                                origin = origin,
                                destination = destination,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Pastikan detail perjalanan Anda",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NeutralDark
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text("Informasi Perjalanan", fontWeight = FontWeight.Bold, color = GreenSecondary, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    InfoRow(icon = Icons.Default.LocationOn, label = "Lokasi Asal", value = origin)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.LocationOn, label = "Tujuan", value = destination)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.CalendarToday, label = "Tanggal", value = date)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.AccessTime, label = "Jam Berangkat", value = time)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.DirectionsCar, label = "Mobil", value = vehicle)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.VpnKey, label = "Plat Nomor", value = plate)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.AirlineSeatReclineNormal, label = "Jumlah Kursi", value = "$seats Kursi")
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoRow(icon = Icons.Default.AttachMoney, label = "Harga per kursi", value = "Rp ${formatPrice(price.toDoubleOrNull() ?: 15000.0)} / kursi")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Dengan mempublikasikan perjalanan ini, Anda setuju dengan Syarat & Ketentuan GoTogether.",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        // Fixed footer for Step 2
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            tonalElevation = 8.dp,
                            shadowElevation = 8.dp
                        ) {
                            Button(
                                onClick = { currentStep = 3 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                                    .height(52.dp)
                                    .testTag("publish_trip_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(14.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                            ) {
                                Text("Publikasikan Perjalanan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
                3 -> {
                    // STEP 3: LOADING SCREEN
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = progressPercent / 100f,
                                    modifier = Modifier.size(100.dp),
                                    color = GreenPrimary,
                                    strokeWidth = 6.dp
                                )
                                Text(
                                    text = "$progressPercent%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Sedang mempublikasikan perjalanan Anda...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Mohon tunggu sebentar",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    // STEP 4: SUCCESS SCREEN
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(GreenLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Berhasil!",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Perjalanan Anda berhasil dipublikasikan!",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(GreenPrimary, CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = origin, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(WarningRed, CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = destination, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = Color(0xFFF5F5F5))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "${date} • ${time}", fontSize = 12.sp, color = Color.Gray)
                                        Text(text = "${seats} Kursi • Rp ${formatPrice(price.toDoubleOrNull() ?: 15000.0)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenSecondary)
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    viewModel.mainSelectedTab.value = 1
                                    viewModel.selectedRoleInPerjalananTab.value = 1
                                    onNavigateBack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("view_my_trips_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Lihat Perjalanan Saya", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = {
                                    viewModel.mainSelectedTab.value = 0
                                    onNavigateBack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("back_to_home_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GreenPrimary)
                            ) {
                                Text("Kembali ke Beranda", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper to format prices
fun formatPrice(price: Double): String {
    val formatter = java.text.DecimalFormat("#,###")
    return formatter.format(price).replace(",", ".")
}

// ==========================================
// DRIVER TRIP LIST COMPONENT & DETAIL & EDIT SCREENS
// ==========================================

@Composable
fun DriverTripListItem(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Tanggal",
                        tint = GreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${trip.date} • ${trip.time}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                }
                
                Box(
                    modifier = Modifier
                        .background(
                            color = when (trip.status) {
                                "Mendatang" -> GreenPrimary.copy(alpha = 0.15f)
                                "Berlangsung" -> Color(0xFFE2F0FD)
                                "Selesai" -> Color(0xFFE8F5E9)
                                else -> Color(0xFFFDE8E8)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (trip.status == "Dibatalkan") "Batal" else trip.status,
                        color = when (trip.status) {
                            "Mendatang" -> GreenPrimary
                            "Berlangsung" -> Color(0xFF1976D2)
                            "Selesai" -> Color(0xFF2E7D32)
                            else -> Color(0xFFC62828)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(GreenPrimary, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.5.dp)
                            .height(24.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(WarningRed, CircleShape)
                    )
                }

                Column {
                    Text(
                        text = trip.origin,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = trip.destination,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeutralDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = GrayBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Mobil",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = trip.vehicleName,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Text(
                    text = "${trip.availableSeats}/${trip.totalSeats} Kursi Tersedia",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (trip.availableSeats == 0) WarningRed else GreenPrimary
                )

                Text(
                    text = "Rp ${formatPrice(trip.price)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenSecondary
                )
            }
        }
    }
}

// Screen 7: Detail Perjalanan Driver
@Composable
fun DriverTripDetailScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToEdit: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToCancel: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()
    val bookings by viewModel.activeTripBookings.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Detail Perjalanan (Driver)", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (trip == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentTrip = trip!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Status Badge Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when (currentTrip.status) {
                                "Mendatang" -> GreenLight
                                "Berlangsung" -> Color(0xFFE2F0FD)
                                "Selesai" -> GreenLight
                                else -> Color(0xFFFDE8E8)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status Perjalanan Anda",
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when (currentTrip.status) {
                                            "Mendatang" -> GreenPrimary
                                            "Berlangsung" -> BlueChat
                                            "Selesai" -> GreenSecondary
                                            else -> WarningRed
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (currentTrip.status == "Dibatalkan") "Batal" else currentTrip.status,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detail rute driver
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Rute & Informasi Kendaraan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GreenSecondary)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Asal", value = currentTrip.origin)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Tujuan", value = currentTrip.destination)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.CalendarToday, label = "Tanggal", value = currentTrip.date)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.AccessTime, label = "Jam", value = currentTrip.time)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.DirectionsCar, label = "Mobil", value = currentTrip.vehicleName)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.VpnKey, label = "Plat Nomor", value = currentTrip.plateNumber)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.AttachMoney, label = "Harga per kursi", value = "Rp ${formatPrice(currentTrip.price)}")
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.AirlineSeatReclineNormal, label = "Kursi Tersedia", value = "${currentTrip.availableSeats} dari ${currentTrip.totalSeats} kursi")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Passenger segment (Screen 7 of driver flow)
                    Text(
                        text = "Penumpang Terdaftar (${currentTrip.totalSeats - currentTrip.availableSeats}/${currentTrip.totalSeats})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NeutralDark,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )

                    if (bookings.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GrayBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada penumpang yang mendaftar.",
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        bookings.forEach { booking ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GrayBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(GreenLight, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = "Passenger",
                                                tint = GreenPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = booking.passengerName,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = NeutralDark
                                            )
                                            Text(
                                                text = "${booking.seatsBooked} Kursi • ${booking.paymentMethod}",
                                                fontSize = 11.5.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.selectTrip(currentTrip.id)
                                            onNavigateToChat()
                                        },
                                        modifier = Modifier
                                            .background(Color(0xFFE8F5E9), CircleShape)
                                            .size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Chat,
                                            contentDescription = "Chat Penumpang",
                                            tint = GreenPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Action buttons panel
                if (currentTrip.status == "Mendatang") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = onNavigateToEdit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("edit_trip_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit Perjalanan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = onNavigateToCancel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("cancel_trip_btn"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
                            border = BorderStroke(1.dp, WarningRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "Batal", tint = WarningRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Batalkan Perjalanan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}

// Screen 9: Edit Perjalanan Driver
@Composable
fun EditTripScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()
    val context = LocalContext.current

    if (trip == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenPrimary)
        }
    } else {
        val currentTrip = trip!!

        var origin by remember { mutableStateOf(currentTrip.origin) }
        var destination by remember { mutableStateOf(currentTrip.destination) }
        var date by remember { mutableStateOf(currentTrip.date) }
        var time by remember { mutableStateOf(currentTrip.time) }
        var vehicle by remember { mutableStateOf(currentTrip.vehicleName) }
        var plate by remember { mutableStateOf(currentTrip.plateNumber) }
        var seats by remember { mutableStateOf(currentTrip.totalSeats) }
        var price by remember { mutableStateOf(currentTrip.price.toString()) }

        Scaffold(
            topBar = {
                OptTopAppBar(title = "Edit Perjalanan", onNavigateBack = onNavigateBack)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFCFDFD))
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("Lokasi Rute", fontWeight = FontWeight.Bold, color = GreenSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = origin,
                                onValueChange = { origin = it },
                                label = { Text("Lokasi Asal") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Asal", tint = GreenPrimary) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = NeutralDark,
                                    unfocusedTextColor = NeutralDark,
                                    focusedBorderColor = GreenPrimary,
                                    focusedLabelColor = GreenPrimary
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = destination,
                                onValueChange = { destination = it },
                                label = { Text("Tujuan") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Tujuan", tint = WarningRed) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = NeutralDark,
                                    unfocusedTextColor = NeutralDark,
                                    focusedBorderColor = GreenPrimary,
                                    focusedLabelColor = GreenPrimary
                                ),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("Jadwal & Kendaraan", fontWeight = FontWeight.Bold, color = GreenSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = date,
                                    onValueChange = { date = it },
                                    label = { Text("Tanggal") },
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = NeutralDark,
                                        unfocusedTextColor = NeutralDark,
                                        focusedBorderColor = GreenPrimary,
                                        focusedLabelColor = GreenPrimary
                                    ),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = time,
                                    onValueChange = { time = it },
                                    label = { Text("Jam") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = NeutralDark,
                                        unfocusedTextColor = NeutralDark,
                                        focusedBorderColor = GreenPrimary,
                                        focusedLabelColor = GreenPrimary
                                    ),
                                    singleLine = true
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = vehicle,
                                    onValueChange = { vehicle = it },
                                    label = { Text("Mobil") },
                                    modifier = Modifier.weight(1.2f).padding(end = 8.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = NeutralDark,
                                        unfocusedTextColor = NeutralDark,
                                        focusedBorderColor = GreenPrimary,
                                        focusedLabelColor = GreenPrimary
                                    ),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = plate,
                                    onValueChange = { plate = it },
                                    label = { Text("Plat Nomor") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = NeutralDark,
                                        unfocusedTextColor = NeutralDark,
                                        focusedBorderColor = GreenPrimary,
                                        focusedLabelColor = GreenPrimary
                                    ),
                                    singleLine = true
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Jumlah Kursi", fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 14.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (seats > 1) seats-- },
                                        modifier = Modifier.background(Color(0xFFF0F4F2), CircleShape).size(36.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Kurang", tint = NeutralDark, modifier = Modifier.size(18.dp))
                                    }
                                    Text(text = "$seats", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 14.dp), fontSize = 16.sp)
                                    IconButton(
                                        onClick = { if (seats < 6) seats++ },
                                        modifier = Modifier.background(Color(0xFFF0F4F2), CircleShape).size(36.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = NeutralDark, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        val currentPrice = price.toDoubleOrNull() ?: 0.0
                                        if (currentPrice >= 1000.0) {
                                            price = (currentPrice - 1000.0).toInt().toString()
                                        } else {
                                            price = "0"
                                        }
                                    },
                                    modifier = Modifier
                                        .background(Color(0xFFF0F4F2), CircleShape)
                                        .size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Kurang Harga",
                                        tint = NeutralDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Harga per kursi (Rp)") },
                                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = "Harga", tint = GreenPrimary) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = NeutralDark,
                                        unfocusedTextColor = NeutralDark,
                                        focusedBorderColor = GreenPrimary,
                                        focusedLabelColor = GreenPrimary
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        val currentPrice = price.toDoubleOrNull() ?: 0.0
                                        price = (currentPrice + 1000.0).toInt().toString()
                                    },
                                    modifier = Modifier
                                        .background(Color(0xFFF0F4F2), CircleShape)
                                        .size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah Harga",
                                        tint = NeutralDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.updateTripDetails(
                            trip = currentTrip,
                            origin = origin,
                            destination = destination,
                            date = date,
                            time = time,
                            vehicleName = vehicle,
                            plateNumber = plate,
                            seats = seats,
                            price = price.toDoubleOrNull() ?: currentTrip.price
                        )
                        Toast.makeText(context, "Perjalanan berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_edit_trip_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Simpan Perubahan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// ==========================================
// 11. PERJALANAN (MY TRIPS) LIST TAB
// ==========================================
@Composable
fun PerjalananTab(
    viewModel: GoTogetherViewModel,
    onNavigateToBookingDetail: (Int) -> Unit,
    onNavigateToTripDetail: (Int) -> Unit,
    onNavigateToTawarkanPerjalanan: () -> Unit,
    onNavigateToChatDetail: ((Int) -> Unit)? = null,
    onNavigateToDriverTripDetail: (Int) -> Unit = {}
) {
    val bookings by viewModel.passengerBookings.collectAsState()
    val driverTrips by viewModel.driverTrips.collectAsState()
    val tripsOffered by viewModel.allTrips.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    
    val selectedRoleInPerjalananTab by viewModel.selectedRoleInPerjalananTab.collectAsState()
    var activeSubTab by remember { mutableStateOf(0) } // 0 = Mendatang, 1 = Berlangsung, 2 = Selesai/Batal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralLight)
    ) {
        // High fidelity role switcher tab row
        TabRow(
            selectedTabIndex = selectedRoleInPerjalananTab,
            containerColor = Color.White,
            contentColor = GreenPrimary
        ) {
            Tab(
                selected = selectedRoleInPerjalananTab == 0,
                onClick = { 
                    viewModel.selectedRoleInPerjalananTab.value = 0 
                    activeSubTab = 0
                },
                text = { Text("Sebagai Penumpang", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedRoleInPerjalananTab == 1,
                onClick = { 
                    viewModel.selectedRoleInPerjalananTab.value = 1 
                    activeSubTab = 0
                },
                text = { Text("Sebagai Driver", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Simple sub-category tabs (Mendatang, Berlangsung, Selesai)
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = Color.White,
            contentColor = GreenPrimary
        ) {
            Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }, text = { Text("Mendatang", fontWeight = FontWeight.Bold, fontSize = 13.sp) })
            Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }, text = { Text("Berlangsung", fontWeight = FontWeight.Bold, fontSize = 13.sp) })
            Tab(selected = activeSubTab == 2, onClick = { activeSubTab = 2 }, text = { Text(if (selectedRoleInPerjalananTab == 1) "Selesai / Batal" else "Selesai", fontWeight = FontWeight.Bold, fontSize = 13.sp) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedRoleInPerjalananTab == 0) {
            // ================= PASSENGER VIEW =================
            val filteredBookings = bookings.filter { booking ->
                when (activeSubTab) {
                    0 -> booking.status == "Mendatang"
                    1 -> booking.status == "Berlangsung"
                    else -> booking.status == "Selesai" || booking.status == "Dibatalkan"
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredBookings.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada perjalanan untuk kategori ini.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(filteredBookings) { booking ->
                        val correspondingTrip = tripsOffered.find { it.id == booking.tripId }
                        if (correspondingTrip != null) {
                            BookingListItem(
                                booking = booking,
                                trip = correspondingTrip,
                                onDetailClick = {
                                    viewModel.selectBooking(booking.id)
                                    viewModel.selectTrip(correspondingTrip.id)
                                    onNavigateToBookingDetail(booking.id)
                                },
                                onChatClick = {
                                    viewModel.selectBooking(booking.id)
                                    viewModel.selectTrip(correspondingTrip.id)
                                    if (onNavigateToChatDetail != null) {
                                        onNavigateToChatDetail(correspondingTrip.id)
                                    }
                                },
                                onRatingClick = {
                                    viewModel.selectBooking(booking.id)
                                    viewModel.selectTrip(correspondingTrip.id)
                                    onNavigateToBookingDetail(booking.id)
                                }
                            )
                        }
                    }
                }

                if (activeSubTab == 0) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Informasi Penting",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Sebelum waktu keberangkatan tiba, driver akan menghubungi Anda melalui fitur chat untuk menentukan titik penjemputan spesifik.",
                                    fontSize = 11.5.sp,
                                    color = NeutralDark.copy(alpha = 0.8f),
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "• Harap aktifkan notifikasi aplikasi agar Anda tidak melewatkan update status perjalanan dari driver.",
                                    fontSize = 11.5.sp,
                                    color = NeutralDark.copy(alpha = 0.8f),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // ================= DRIVER VIEW =================
            val filteredDriverTrips = driverTrips.filter { trip ->
                when (activeSubTab) {
                    0 -> trip.status == "Mendatang"
                    1 -> trip.status == "Berlangsung"
                    else -> trip.status == "Selesai" || trip.status == "Dibatalkan"
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredDriverTrips.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Anda belum menawarkan perjalanan pada kategori ini.",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )
                                if (activeSubTab == 0) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Button(
                                        onClick = onNavigateToTawarkanPerjalanan,
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Tawarkan Perjalanan Sekarang", fontSize = 12.5.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    items(filteredDriverTrips) { trip ->
                        DriverTripListItem(
                            trip = trip,
                            onClick = {
                                viewModel.selectTrip(trip.id)
                                onNavigateToDriverTripDetail(trip.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 12. PERJALANAN DETAIL (BOOKING / TRIP ACTIVE STATE) SCREEN
// ==========================================
@Composable
fun BookingDetailScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToRating: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCancel: (Int) -> Unit = {}
) {
    val booking by viewModel.activeBooking.collectAsState()
    val trip by viewModel.activeTrip.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    val screenTitle = when (booking?.status) {
        "Selesai" -> "Ringkasan Perjalanan"
        "Dibatalkan" -> "Detail Perjalanan (Batal)"
        else -> "Perjalanan Saya"
    }

    Scaffold(
        topBar = {
            OptTopAppBar(title = screenTitle, onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (booking == null || trip == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentBooking = booking!!
            val currentTrip = trip!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Status Badge Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when (currentBooking.status) {
                                "Mendatang" -> GreenLight
                                "Berlangsung" -> Color(0xFFE2F0FD)
                                "Selesai" -> GreenLight
                                else -> Color(0xFFFDE8E8)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status Perjalanan",
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when (currentBooking.status) {
                                            "Mendatang" -> GreenPrimary
                                            "Berlangsung" -> BlueChat
                                            "Selesai" -> GreenSecondary
                                            else -> WarningRed
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = currentBooking.status,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trip details
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Rute & Driver", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow(icon = Icons.Default.Person, label = "Driver", value = currentTrip.driverName)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.DirectionsCar, label = "Mobil", value = "${currentTrip.vehicleName} (${currentTrip.plateNumber})")
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Asal", value = currentTrip.origin)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.LocationOn, label = "Tujuan", value = currentTrip.destination)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.CalendarToday, label = "Tanggal", value = currentTrip.date)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.AccessTime, label = "Jam Berangkat", value = currentTrip.time)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Booking info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Informasi Pemesanan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow(icon = Icons.Default.Person, label = "Penumpang", value = currentBooking.passengerName)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.DirectionsCar, label = "Kursi Dipesan", value = "${currentBooking.seatsBooked} kursi")
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.Payment, label = "Metode Pembayaran", value = currentBooking.paymentMethod)
                            Spacer(modifier = Modifier.height(10.dp))
                            InfoRow(icon = Icons.Default.Payment, label = "Total Harga", value = "Rp ${String.format("%,.0f", currentBooking.totalPrice)}")
                        }
                    }
                }

                // Driver Chat & Cancel & Rating Action Panels
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    if (currentBooking.status == "Mendatang") {
                        Button(
                            onClick = {
                                viewModel.updateBookingStatus(currentBooking, "Berlangsung")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_trip_state_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simulasi Mulai Perjalanan", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.selectTrip(currentTrip.id)
                                    onNavigateToChat()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .padding(end = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GreenPrimary)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "Chat", tint = GreenPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chat Driver", color = GreenPrimary)
                            }

                            Button(
                                onClick = {
                                    onNavigateToCancel(currentBooking.id)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("cancel_booking_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Batalkan")
                            }
                        }
                    } else if (currentBooking.status == "Berlangsung") {
                        Button(
                            onClick = {
                                viewModel.updateBookingStatus(currentBooking, "Selesai")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("complete_trip_state_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simulasi Selesaikan Perjalanan", fontWeight = FontWeight.Bold)
                        }
                    } else if (currentBooking.status == "Selesai") {
                        if (currentBooking.ratingGiven != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = GreenLight),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Ulasan & Rating Anda",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = GreenSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating",
                                            tint = StarYellow,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${currentBooking.ratingGiven?.toInt() ?: 5} Bintang",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = NeutralDark
                                        )
                                    }
                                    if (!currentBooking.reviewGiven.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "\"${currentBooking.reviewGiven}\"",
                                            fontSize = 13.sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("close_summary_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Tutup", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = onNavigateToRating,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("give_rating_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Beri Rating & Ulasan", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                border = BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Tutup", color = Color.Gray, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 13. BERI RATING (RATING & FEEDBACK) SCREEN
// ==========================================
@Composable
fun BeriRatingScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val booking by viewModel.activeBooking.collectAsState()
    var rating by remember { mutableStateOf(5f) }
    var reviewText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Beri Rating", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (booking == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentBooking = booking!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Driver Avatar", tint = GreenPrimary, modifier = Modifier.size(48.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Bagaimana perjalanan Anda?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                    Text(text = "Beri rating untuk membantu driver meningkatkan layanannya.", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Stars Selector
                    Row {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$i Stars",
                                tint = if (i <= rating) StarYellow else Color.LightGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { rating = i.toFloat() }
                                    .padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Textarea review
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text("Tulis ulasan Anda (opsional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("review_input_text"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeutralDark,
                            unfocusedTextColor = NeutralDark,
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = GrayBorder,
                            focusedLabelColor = GreenPrimary
                        )
                    )
                }

                Button(
                    onClick = {
                        viewModel.submitRating(currentBooking, rating, reviewText)
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_rating_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Kirim Rating", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// 14. CHAT (MESSAGE LIST) TAB
// ==========================================
@Composable
fun ChatTab(
    viewModel: GoTogetherViewModel,
    onNavigateToTripDetail: (Int) -> Unit,
    onNavigateToChatDetail: (Int) -> Unit
) {
    val allTrips by viewModel.allTrips.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Obrolan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (allTrips.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Belum ada obrolan aktif.", color = Color.Gray)
                }
            }
        } else {
            items(allTrips) { trip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToChatDetail(trip.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(GreenLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Avatar", tint = GreenPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = trip.driverName, fontWeight = FontWeight.Bold, color = NeutralDark)
                            Text(
                                text = "Klik untuk membuka percakapan mengenai rute ${trip.origin} - ${trip.destination}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 15. PROFIL (USER PROFILE) TAB
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandTopAppBar(title: String, onNavigateBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = GreenPrimary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun ProfilTab(
    viewModel: GoTogetherViewModel,
    onNavigateToRiwayat: () -> Unit,
    onNavigateToPembayaran: () -> Unit,
    onNavigateToSavedAddresses: () -> Unit,
    onNavigateToTopup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToKeamanan: () -> Unit,
    onNavigateToBantuan: () -> Unit,
    onNavigateToTentang: () -> Unit,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    var isDriverMode by remember { mutableStateOf(user?.isDriver == true) }

    LaunchedEffect(user) {
        if (user != null) {
            isDriverMode = user!!.isDriver
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralLight)
    ) {
        // Top profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GreenPrimary, GreenSecondary)
                        ),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Photo",
                            tint = GreenPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user?.name ?: "Syafira Aulia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = user?.email ?: "syafiraa@gmail.com",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = user?.phone ?: "0812-3456-7890",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Akun Terverifikasi Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Akun Terverifikasi",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified Badge",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        // Penumpang / Pengemudi toggle switcher tab
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                Button(
                    onClick = {
                        isDriverMode = false
                        viewModel.switchRole(false)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("passenger_role_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isDriverMode) GreenPrimary else Color.Transparent,
                        contentColor = if (!isDriverMode) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = null
                ) {
                    Text("Penumpang", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        isDriverMode = true
                        viewModel.switchRole(true)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("driver_role_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDriverMode) GreenPrimary else Color.Transparent,
                        contentColor = if (isDriverMode) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = null
                ) {
                    Text("Pengemudi", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Profile Menu Items
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, GrayBorder, RoundedCornerShape(16.dp))
            ) {
                ProfileMenuItem(icon = Icons.Default.History, label = "Riwayat Perjalanan", onClick = onNavigateToRiwayat)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Payment, label = "Pembayaran", onClick = onNavigateToPembayaran)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Settings, label = "Pengaturan", onClick = onNavigateToSettings)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.LocationOn, label = "Alamat Tersimpan", onClick = onNavigateToSavedAddresses)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Favorite, label = "Pesan Favorit", onClick = {
                    Toast.makeText(context, "Daftar pesan favorit Anda kosong.", Toast.LENGTH_SHORT).show()
                })
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Security, label = "Keamanan", onClick = onNavigateToKeamanan)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Help, label = "Bantuan & Pusat Bantuan", onClick = onNavigateToBantuan)
                Divider(color = GrayBorder)
                ProfileMenuItem(icon = Icons.Default.Info, label = "Tentang GoTogether", onClick = onNavigateToTentang)
            }
        }

        // Logout
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("logout_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
                    border = BorderStroke(1.5.dp, WarningRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Keluar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ==========================================
// 15A. RIWAYAT PERJALANAN SCREEN
// ==========================================
@Composable
fun RiwayatPerjalananScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToBookingDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val bookings by viewModel.passengerBookings.collectAsState()
    val tripsOffered by viewModel.allTrips.collectAsState()
    var activeSubTab by remember { mutableStateOf(0) } // 0 = Semua, 1 = Selesai, 2 = Dibatalkan

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Riwayat Perjalanan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = Color.White,
                contentColor = GreenPrimary
            ) {
                Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }, text = { Text("Semua", fontWeight = FontWeight.Bold) })
                Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }, text = { Text("Selesai", fontWeight = FontWeight.Bold) })
                Tab(selected = activeSubTab == 2, onClick = { activeSubTab = 2 }, text = { Text("Dibatalkan", fontWeight = FontWeight.Bold) })
            }

            val filteredBookings = bookings.filter { booking ->
                when (activeSubTab) {
                    0 -> true
                    1 -> booking.status == "Selesai"
                    else -> booking.status == "Dibatalkan"
                }
            }

            if (filteredBookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Empty",
                            tint = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Belum ada riwayat perjalanan.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredBookings) { booking ->
                        val trip = tripsOffered.find { it.id == booking.tripId }
                        if (trip != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectBooking(booking.id)
                                        viewModel.selectTrip(trip.id)
                                        onNavigateToBookingDetail(booking.id)
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, GrayBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "${trip.date} • ${trip.time}", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (booking.status == "Selesai") GreenLight else Color(0xFFFDE8E8),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = booking.status,
                                                color = if (booking.status == "Selesai") GreenPrimary else WarningRed,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(GreenPrimary, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = trip.origin, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .width(2.dp)
                                            .height(16.dp)
                                            .background(Color.LightGray)
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(WarningRed, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = trip.destination, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = NeutralDark)
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = GrayBorder)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = "Driver",
                                                tint = GreenPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(text = "Driver: ${trip.driverName}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
                                                Text(text = trip.vehicleName, fontSize = 11.sp, color = Color.Gray)
                                            }
                                        }
                                        Text(
                                            text = "Rp ${String.format("%,.0f", booking.totalPrice)}",
                                            fontWeight = FontWeight.Bold,
                                            color = NeutralDark,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 15B. PEMBAYARAN SCREEN
// ==========================================
@Composable
fun PembayaranScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToTopup: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Pembayaran", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(GreenLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = "Wallet",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Saldo GoTogether", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                                Text(
                                    text = "Rp ${String.format("%,.0f", user?.balance ?: 250000.0)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }
                        }
                        Button(
                            onClick = onNavigateToTopup,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Top Up", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Payment methods section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Metode Pembayaran", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark)
                    Text(
                        text = "+ Tambah Metode",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GreenPrimary,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Fitur tambah metode segera hadir!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // List of Payment methods
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        // OVO
                        PaymentMethodItem(
                            label = "OVO",
                            detail = "0812 3456 7890",
                            iconBgColor = Color(0xFF4C2A86),
                            iconChar = "O",
                            isDefault = true
                        )
                        Divider(color = GrayBorder)
                        // DANA
                        PaymentMethodItem(
                            label = "DANA",
                            detail = "0812 3456 7890",
                            iconBgColor = Color(0xFF118EEA),
                            iconChar = "D",
                            isDefault = false
                        )
                        Divider(color = GrayBorder)
                        // GoPay
                        PaymentMethodItem(
                            label = "GoPay",
                            detail = "0812 3456 7890",
                            iconBgColor = Color(0xFF00AED6),
                            iconChar = "G",
                            isDefault = false
                        )
                        Divider(color = GrayBorder)
                        // ShopeePay
                        PaymentMethodItem(
                            label = "ShopeePay",
                            detail = "0812 3456 7890",
                            iconBgColor = Color(0xFFEE4D2D),
                            iconChar = "S",
                            isDefault = false
                        )
                        Divider(color = GrayBorder)
                        // Kartun Debit/Kredit
                        PaymentMethodCardItem(
                            label = "Kartu Debit/Kredit",
                            detail = "•••• 1234"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    label: String,
    detail: String,
    iconBgColor: Color,
    iconChar: String,
    isDefault: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconChar, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeutralDark)
                Text(text = detail, fontSize = 12.sp, color = Color.Gray)
            }
        }
        if (isDefault) {
            Box(
                modifier = Modifier
                    .background(GreenLight, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Utama", color = GreenPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PaymentMethodCardItem(
    label: String,
    detail: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Card",
                    tint = NeutralDark,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeutralDark)
                Text(text = detail, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ==========================================
// 16. ALAMAT TERSIMPAN (SAVED ADDRESSES) SCREEN
// ==========================================
@Composable
fun SavedAddressesScreen(
    viewModel: GoTogetherViewModel,
    onNavigateToTambahAlamat: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val addresses by viewModel.savedAddresses.collectAsState()

    Scaffold(
        topBar = {
            BrandTopAppBar(
                title = "Alamat Tersimpan",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = onNavigateToTambahAlamat) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Alamat", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (addresses.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.LocationOff, contentDescription = "Empty", tint = Color.LightGray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Belum ada alamat tersimpan.", color = Color.Gray)
                        }
                    }
                }
            } else {
                items(addresses) { addr ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GrayBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(GreenLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (addr.label.contains("Rumah", ignoreCase = true)) Icons.Default.Home else Icons.Default.LocationOn,
                                        contentDescription = addr.label,
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = addr.label, fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 15.sp)
                                        if (addr.isDefault) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(GreenLight, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(text = "Utama", fontSize = 10.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = addr.address, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
                                }
                            }
                            IconButton(onClick = { viewModel.deleteSavedAddress(addr.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus", tint = WarningRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 17. TAMBAH ALAMAT (ADD ADDRESS) SCREEN
// ==========================================
@Composable
fun TambahAlamatScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    var label by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Tambah Alamat", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = label,
                            onValueChange = { label = it },
                            placeholder = { Text("Contoh: Rumah, Kantor") },
                            label = { Text("Label Alamat") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = GrayBorder,
                                focusedLabelColor = GreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            placeholder = { Text("Contoh: Syafira Aulia") },
                            label = { Text("Nama Penerima") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = GrayBorder,
                                focusedLabelColor = GreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            placeholder = { Text("Contoh: 081234567890") },
                            label = { Text("Nomor Telepon") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = GrayBorder,
                                focusedLabelColor = GreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("Masukkan alamat lengkap") },
                            label = { Text("Alamat Lengkap") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeutralDark,
                                unfocusedTextColor = NeutralDark,
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = GrayBorder,
                                focusedLabelColor = GreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Jadikan Utama", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                                Text(text = "Alamat utama akan digunakan secara default saat memesan.", fontSize = 11.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = isDefault,
                                onCheckedChange = { isDefault = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimary, checkedTrackColor = GreenLight)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (label.isNotBlank() && address.isNotBlank()) {
                        viewModel.addSavedAddress(label, address, "Medan", isDefault)
                        Toast.makeText(context, "Alamat berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onNavigateBack()
                    } else {
                        Toast.makeText(context, "Label dan Alamat tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_address_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Simpan Alamat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 18. TOP UP SALDO SCREEN
// ==========================================
@Composable
fun TopupScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    var amount by remember { mutableStateOf("250000") }
    val context = LocalContext.current
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Selesai", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            title = {
                Text("Top Up Berhasil", fontWeight = FontWeight.Bold, color = NeutralDark, textAlign = TextAlign.Center)
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Success", tint = GreenPrimary, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Top up sebesar Rp ${String.format("%,.0f", amount.toDoubleOrNull() ?: 0.0)} berhasil ditambahkan ke saldo Anda.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Detail Saldo (Top Up)", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Top Up Saldo", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rp ${String.format("%,.0f", user?.balance ?: 250000.0)}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Saldo saat ini", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Pilih Nominal", fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    TopupChoiceButton(label = "Rp 50.000", value = "50000", selected = amount == "50000") { amount = "50000" }
                    Spacer(modifier = Modifier.width(8.dp))
                    TopupChoiceButton(label = "Rp 100.000", value = "100000", selected = amount == "100000") { amount = "100000" }
                    Spacer(modifier = Modifier.width(8.dp))
                    TopupChoiceButton(label = "Rp 150.000", value = "150000", selected = amount == "150000") { amount = "150000" }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    TopupChoiceButton(label = "Rp 200.000", value = "200000", selected = amount == "200000") { amount = "200000" }
                    Spacer(modifier = Modifier.width(8.dp))
                    TopupChoiceButton(label = "Rp 250.000", value = "250000", selected = amount == "250000") { amount = "250000" }
                    Spacer(modifier = Modifier.width(8.dp))
                    TopupChoiceButton(label = "Nominal Lain", value = "", selected = amount != "50000" && amount != "100000" && amount != "150000" && amount != "200000" && amount != "250000") { amount = "" }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Jumlah Top Up (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NeutralDark,
                        unfocusedTextColor = NeutralDark,
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayBorder,
                        focusedLabelColor = GreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Metode Pembayaran", fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF4C2A86), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "O", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "OVO", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeutralDark)
                                Text(text = "0812 3456 7890", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Text(
                            text = "Ubah",
                            color = GreenPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "Ubah metode pembayaran segera hadir!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val doubleAmount = amount.toDoubleOrNull() ?: 0.0
                    if (doubleAmount > 0) {
                        viewModel.topUpBalance(doubleAmount)
                        showSuccessDialog = true
                    } else {
                        Toast.makeText(context, "Masukkan jumlah top up yang valid.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("topup_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Top Up Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 19. PENGATURAN SCREEN
// ==========================================
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isNotificationEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Pengaturan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Akun Section
            item {
                Text(text = "Akun", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark, modifier = Modifier.padding(start = 4.dp))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        SettingsRowItem(
                            icon = Icons.Default.Person,
                            label = "Informasi Akun",
                            onClick = { Toast.makeText(context, "Informasi Akun", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Lock,
                            label = "Ubah Password",
                            onClick = { Toast.makeText(context, "Ubah Password", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Email,
                            label = "Verifikasi Email",
                            badgeText = "Terverifikasi",
                            onClick = { Toast.makeText(context, "Email Terverifikasi", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Phone,
                            label = "Ubah Nomor Telepon",
                            badgeText = "Terverifikasi",
                            onClick = { Toast.makeText(context, "Telepon Terverifikasi", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }

            // Aplikasi Section
            item {
                Text(text = "Aplikasi", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark, modifier = Modifier.padding(start = 4.dp))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifikasi", tint = GreenPrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Notifikasi", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
                            }
                            Switch(
                                checked = isNotificationEnabled,
                                onCheckedChange = { isNotificationEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimary, checkedTrackColor = GreenLight)
                            )
                        }
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Language,
                            label = "Bahasa",
                            valueText = "Bahasa Indonesia",
                            onClick = { Toast.makeText(context, "Bahasa Indonesia aktif", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Palette,
                            label = "Tema",
                            valueText = "Terang",
                            onClick = { Toast.makeText(context, "Tema Terang aktif", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Visibility,
                            label = "Privasi",
                            onClick = { Toast.makeText(context, "Privasi", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }

            // Lainnya Section
            item {
                Text(text = "Lainnya", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark, modifier = Modifier.padding(start = 4.dp))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        var cacheSize by remember { mutableStateOf("12,4 MB") }
                        SettingsRowItem(
                            icon = Icons.Default.Delete,
                            label = "Bersihkan Cache",
                            valueText = cacheSize,
                            onClick = {
                                cacheSize = "0 KB"
                                Toast.makeText(context, "Cache berhasil dibersihkan!", Toast.LENGTH_SHORT).show()
                            }
                        )
                        Divider(color = GrayBorder)
                        SettingsRowItem(
                            icon = Icons.Default.Info,
                            label = "Tentang Aplikasi",
                            valueText = "Versi 1.0.0",
                            onClick = { Toast.makeText(context, "GoTogether v1.0.0", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    label: String,
    badgeText: String? = null,
    valueText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = label, tint = GreenPrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .background(GreenLight, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = badgeText, color = GreenPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            if (valueText != null) {
                Text(text = valueText, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(end = 8.dp))
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Go", tint = Color.LightGray, modifier = Modifier.size(18.dp))
        }
    }
}

// ==========================================
// 20. KEAMANAN SCREEN
// ==========================================
@Composable
fun KeamananScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Keamanan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GrayBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            tint = GreenPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Keamanan Akun", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NeutralDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kelola keamanan akun Anda untuk pengalaman yang lebih aman.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GrayBorder)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Lock,
                        label = "Ubah Password",
                        onClick = { Toast.makeText(context, "Ubah Password", Toast.LENGTH_SHORT).show() }
                    )
                    Divider(color = GrayBorder)
                    SettingsRowItem(
                        icon = Icons.Default.Email,
                        label = "Verifikasi Email",
                        badgeText = "Terverifikasi",
                        onClick = { Toast.makeText(context, "Email terverifikasi", Toast.LENGTH_SHORT).show() }
                    )
                    Divider(color = GrayBorder)
                    SettingsRowItem(
                        icon = Icons.Default.Phone,
                        label = "Verifikasi Nomor Telepon",
                        badgeText = "Terverifikasi",
                        onClick = { Toast.makeText(context, "Nomor telepon terverifikasi", Toast.LENGTH_SHORT).show() }
                    )
                    Divider(color = GrayBorder)
                    SettingsRowItem(
                        icon = Icons.Default.Devices,
                        label = "Perangkat Terhubung",
                        valueText = "2 Perangkat",
                        onClick = { Toast.makeText(context, "Perangkat Terhubung", Toast.LENGTH_SHORT).show() }
                    )
                    Divider(color = GrayBorder)
                    SettingsRowItem(
                        icon = Icons.Default.Login,
                        label = "Aktivitas Login Terakhir",
                        onClick = { Toast.makeText(context, "Aktivitas Login Terakhir", Toast.LENGTH_SHORT).show() }
                    )
                }
            }
        }
    }
}

// ==========================================
// 21. BANTUAN SCREEN
// ==========================================
@Composable
fun BantuanScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Bantuan & Pusat Bantuan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ada yang bisa\nkami bantu?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                lineHeight = 26.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Temukan jawaban atau hubungi kami langsung.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Help Help",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(text = "Topik Bantuan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark, modifier = Modifier.padding(start = 4.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        HelpTopicRow(label = "Cara menggunakan GoTogether", onClick = { Toast.makeText(context, "Cara menggunakan GoTogether", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Pembayaran & Promo", onClick = { Toast.makeText(context, "Pembayaran & Promo", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Akun & Keamanan", onClick = { Toast.makeText(context, "Akun & Keamanan", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Perjalanan", onClick = { Toast.makeText(context, "Perjalanan", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Lainnya", onClick = { Toast.makeText(context, "Lainnya", Toast.LENGTH_SHORT).show() })
                    }
                }
            }

            item {
                Text(text = "Hubungi Kami", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NeutralDark, modifier = Modifier.padding(start = 4.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        ContactRowItem(
                            icon = Icons.Default.Chat,
                            label = "Chat dengan CS",
                            valueText = "Online 08.00 - 22.00",
                            onClick = { Toast.makeText(context, "Membuka Chat CS...", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        ContactRowItem(
                            icon = Icons.Default.Email,
                            label = "Email",
                            valueText = "support@gotogether.com",
                            onClick = { Toast.makeText(context, "Mengirim email...", Toast.LENGTH_SHORT).show() }
                        )
                        Divider(color = GrayBorder)
                        ContactRowItem(
                            icon = Icons.Default.Phone,
                            label = "Telepon",
                            valueText = "(061) 1234 5678",
                            onClick = { Toast.makeText(context, "Melakukan panggilan...", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpTopicRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Go", tint = Color.LightGray, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ContactRowItem(
    icon: ImageVector,
    label: String,
    valueText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(GreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = GreenPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeutralDark)
                Text(text = valueText, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Go", tint = Color.LightGray, modifier = Modifier.size(18.dp))
    }
}

// ==========================================
// 22. TENTANG SCREEN
// ==========================================
@Composable
fun TentangScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BrandTopAppBar(title = "Tentang GoTogether", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeutralLight)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(GreenLight, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "GoTogether Logo Logo",
                        tint = GreenPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "GoTogether", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                Text(text = "Versi 1.0.0", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "GoTogether adalah aplikasi berbagi perjalanan (ride-pooling) yang bertujuan membantu Anda menghemat biaya, bepergian dengan aman dan nyaman, serta mengurangi emisi karbon.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = NeutralDark,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column {
                        HelpTopicRow(label = "Syarat & Ketentuan", onClick = { Toast.makeText(context, "Syarat & Ketentuan", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Kebijakan Privasi", onClick = { Toast.makeText(context, "Kebijakan Privasi", Toast.LENGTH_SHORT).show() })
                        Divider(color = GrayBorder)
                        HelpTopicRow(label = "Lisensi", onClick = { Toast.makeText(context, "Lisensi", Toast.LENGTH_SHORT).show() })
                    }
                }
            }

            Text(
                text = "© 2024 GoTogether. All rights reserved.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

// ==========================================
// SUB-COMPONENTS & HELPERS
// ==========================================

@Composable
fun TripListItem(
    trip: Trip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Driver", tint = GreenPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = trip.driverName, fontWeight = FontWeight.Bold, color = NeutralDark, fontSize = 14.sp)
                        Text(text = trip.vehicleName, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Text(text = trip.time, fontWeight = FontWeight.Bold, color = GreenPrimary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Route representation
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Asal", tint = GreenPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = trip.origin, fontSize = 13.sp, color = NeutralDark)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Ke", tint = Color.Gray, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Tujuan", tint = WarningRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = trip.destination, fontSize = 13.sp, color = NeutralDark)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rp ${String.format("%,.0f", trip.price)} / kursi",
                    fontWeight = FontWeight.Bold,
                    color = BalanceGreen,
                    fontSize = 15.sp
                )
                Box(
                    modifier = Modifier
                        .background(GreenLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "${trip.availableSeats} kursi", color = GreenPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BookingListItem(
    booking: Booking,
    trip: Trip,
    onDetailClick: () -> Unit,
    onChatClick: () -> Unit,
    onRatingClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDetailClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Schedule & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Jadwal",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${trip.date} • ${trip.time}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }

                // High fidelity status badges
                val badgeColors = when (booking.status) {
                    "Mendatang" -> Pair(Color(0xFFFFF9C4), Color(0xFFF57F17))  // Light yellow / dark orange
                    "Berlangsung" -> Pair(Color(0xFFE3F2FD), Color(0xFF1976D2)) // Light blue / dark blue
                    "Selesai" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))    // Light green / dark green
                    else -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))         // Light red / dark red
                }

                Box(
                    modifier = Modifier
                        .background(badgeColors.first, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (booking.status == "Dibatalkan") "Batal" else booking.status,
                        color = badgeColors.second,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Vertical Route Timeline (Screen 5 style)
            Column(modifier = Modifier.fillMaxWidth()) {
                // Origin
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(GreenPrimary, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = trip.origin,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                }

                // Vertical line linking points
                Row {
                    Spacer(modifier = Modifier.width(7.dp))
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(18.dp)
                            .background(Color.LightGray)
                    )
                }

                // Destination
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFFFFEBEE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(WarningRed, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = trip.destination,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = GrayBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(14.dp))

            // Inner Row (2 Columns) of Details (Driver, Mobil, Plat, Harga)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Column Left
                Column(modifier = Modifier.weight(1f)) {
                    // Driver Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Driver", tint = GreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "Driver", fontSize = 10.sp, color = Color.Gray)
                            Text(text = trip.driverName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Mobil Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = "Mobil", tint = GreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "Kendaraan", fontSize = 10.sp, color = Color.Gray)
                            Text(text = trip.vehicleName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                        }
                    }
                }

                // Column Right
                Column(modifier = Modifier.weight(1f)) {
                    // Plat Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = "Plat", tint = GreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "Plat Nomor", fontSize = 10.sp, color = Color.Gray)
                            Text(text = trip.plateNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Harga Row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocalOffer, contentDescription = "Harga", tint = GreenSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = "Harga", fontSize = 10.sp, color = Color.Gray)
                            Text(text = "Rp ${String.format("%,.0f", booking.totalPrice)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons based on status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                when (booking.status) {
                    "Mendatang", "Berlangsung" -> {
                        OutlinedButton(
                            onClick = onChatClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GreenPrimary),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Chat Driver",
                                    color = GreenPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = onDetailClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Melihat Detail",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    "Selesai" -> {
                        OutlinedButton(
                            onClick = onDetailClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Lihat Detail",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Only show "Beri Rating" if no rating has been given yet (rating can be represented in booking or checked via ratingGiven state)
                        val ratingGiven = booking.ratingGiven != null
                        if (!ratingGiven) {
                            Button(
                                onClick = onRatingClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "Beri Rating",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .background(GreenLight, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sudah Dinilai", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenSecondary)
                                }
                            }
                        }
                    }

                    else -> { // Dibatalkan / others
                        Button(
                            onClick = onDetailClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Lihat Detail",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = GreenPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = label, tint = GreenPrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = NeutralDark)
        }
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Detail", tint = Color.Gray, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ChatBubble(msg: ChatMessage, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) Color(0xFFE2F5E9) else Color.White
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp),
            border = if (isCurrentUser) null else BorderStroke(1.dp, GrayBorder)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = msg.message,
                    fontSize = 15.sp,
                    color = NeutralDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                    val timeStr = sdf.format(java.util.Date(msg.timestamp))
                    Text(
                        text = timeStr,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = Color(0xFF34A853),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopupChoiceButton(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(44.dp)
            .clickable { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) GreenPrimary else Color.White,
            contentColor = if (selected) Color.White else NeutralDark
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (selected) null else BorderStroke(1.dp, GrayBorder),
        elevation = null
    ) {
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptTopAppBar(title: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = NeutralDark,
            navigationIconContentColor = NeutralDark
        )
    )
}

// Custom canvas that draws an elegant route map preview dynamically
@Composable
fun RouteMapCanvas(origin: String, destination: String) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Background color
        drawRect(color = Color(0xFFE3F2FD))

        // Grid lines (making it look like a map)
        val gridSpacing = 40f
        for (x in 0..(width / gridSpacing).toInt()) {
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(x * gridSpacing, 0f),
                end = Offset(x * gridSpacing, height),
                strokeWidth = 1f
            )
        }
        for (y in 0..(height / gridSpacing).toInt()) {
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(0f, y * gridSpacing),
                end = Offset(width, y * gridSpacing),
                strokeWidth = 1f
            )
        }

        // Elegant curved road representing the route
        val startPoint = Offset(width * 0.2f, height * 0.7f)
        val controlPoint = Offset(width * 0.5f, height * 0.1f)
        val endPoint = Offset(width * 0.8f, height * 0.3f)

        val roadPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(startPoint.x, startPoint.y)
            quadraticTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
        }

        // Draw outer thick road line
        drawPath(
            path = roadPath,
            color = Color(0xFFB0BEC5),
            style = Stroke(width = 24f)
        )

        // Draw inner white road line
        drawPath(
            path = roadPath,
            color = Color.White,
            style = Stroke(width = 16f)
        )

        // Draw dashed center lane line
        drawPath(
            path = roadPath,
            color = Color(0xFFFFCC00),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
            )
        )

        // Origin Pin
        drawCircle(
            color = GreenPrimary,
            radius = 14f,
            center = startPoint
        )
        drawCircle(
            color = Color.White,
            radius = 6f,
            center = startPoint
        )

        // Destination Pin
        drawCircle(
            color = WarningRed,
            radius = 14f,
            center = endPoint
        )
        drawCircle(
            color = Color.White,
            radius = 6f,
            center = endPoint
        )
    }
}

// ==========================================
// 22. BATALKAN PERJALANAN (DEDICATED) SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatalkanPerjalananScreen(
    bookingId: Int,
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val bookings by viewModel.passengerBookings.collectAsState()
    val booking = bookings.find { it.id == bookingId } ?: viewModel.activeBooking.collectAsState().value
    val trips by viewModel.allTrips.collectAsState()
    val trip = trips.find { it.id == booking?.tripId } ?: viewModel.activeTrip.collectAsState().value
    val context = LocalContext.current

    var selectedReason by remember { mutableStateOf("Pilih alasan") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val reasons = listOf(
        "Saya ingin merubah rute/jadwal perjalanan",
        "Driver tidak merespon chat",
        "Dapat opsi perjalanan lain yang lebih baik",
        "Rencana perjalanan saya dibatalkan",
        "Lainnya"
    )

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Batalkan Perjalanan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (booking == null || trip == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Exclamation red circle warning icon (Screen 12 style)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFFFEBEE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(Color(0xFFFFCDD2), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning Icon",
                                tint = WarningRed,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Batalkan Perjalanan?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Apakah Anda yakin ingin membatalkan perjalanan ini?",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Booking details card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GrayBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${trip.date} • ${trip.time}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFFF3E0), shape = RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Mendatang",
                                        color = Color(0xFFEF6C00),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Route Representation
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(GreenLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(modifier = Modifier.size(8.dp).background(GreenPrimary, CircleShape))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = trip.origin,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }
                            Row {
                                Spacer(modifier = Modifier.width(7.dp))
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(14.dp)
                                        .background(Color.LightGray)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color(0xFFFFEBEE), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(modifier = Modifier.size(8.dp).background(WarningRed, CircleShape))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = trip.destination,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = GrayBorder.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Driver",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = trip.driverName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NeutralDark
                                    )
                                }
                                Text(
                                    text = "Rp ${String.format("%,.0f", booking.totalPrice)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dropdown selector
                    Text(
                        text = "Alasan pembatalan (opsional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, GrayBorder, RoundedCornerShape(12.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedReason,
                                fontSize = 14.sp,
                                color = if (selectedReason == "Pilih alasan") Color.Gray else NeutralDark
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                        ) {
                            reasons.forEach { reason ->
                                DropdownMenuItem(
                                    text = { Text(reason, fontSize = 14.sp, color = NeutralDark) },
                                    onClick = {
                                        selectedReason = reason
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Bottom Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Kembali", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val reasonToSend = if (selectedReason == "Pilih alasan") "Tidak ada alasan spesifik" else selectedReason
                            viewModel.cancelBooking(booking, reasonToSend)
                            Toast.makeText(context, "Pemesanan berhasil dibatalkan.", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp)
                            .testTag("submit_cancel_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ya, Batalkan", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// Screen 10: Batalkan Perjalanan (Driver)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatalkanPerjalananDriverScreen(
    viewModel: GoTogetherViewModel,
    onNavigateBack: () -> Unit
) {
    val trip by viewModel.activeTrip.collectAsState()
    val context = LocalContext.current

    var selectedReason by remember { mutableStateOf("Pilih alasan") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val reasons = listOf(
        "Kendaraan bermasalah",
        "Sakit/halangan mendadak",
        "Perubahan rute",
        "Cuaca buruk",
        "Lainnya"
    )

    Scaffold(
        topBar = {
            OptTopAppBar(title = "Batalkan Perjalanan", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (trip == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            val currentTrip = trip!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NeutralLight)
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Exclamation red circle warning icon (Screen 10 style)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFFFFEBEE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(Color(0xFFFFCDD2), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning Icon",
                                tint = WarningRed,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Yakin ingin membatalkan perjalanan ini?",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Perjalanan yang dibatalkan tidak akan mempengaruhi rating Anda.",
                        fontSize = 12.5.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Dropdown selector
                    Text(
                        text = "Alasan pembatalan (opsional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, GrayBorder, RoundedCornerShape(12.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedReason,
                                fontSize = 14.sp,
                                color = if (selectedReason == "Pilih alasan") Color.Gray else NeutralDark
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                        ) {
                            reasons.forEach { reason ->
                                DropdownMenuItem(
                                    text = { Text(reason, fontSize = 14.sp, color = NeutralDark) },
                                    onClick = {
                                        selectedReason = reason
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Bottom Buttons (Vertically stacked matching Screen 10 of mockup)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.cancelTrip(currentTrip)
                            Toast.makeText(context, "Perjalanan berhasil dibatalkan.", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("driver_submit_cancel_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ya, Batalkan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                    ) {
                        Text("Kembali", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

