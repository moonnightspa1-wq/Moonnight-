package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BlushPinkAccent
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.LightGrayText
import com.example.ui.theme.MutedText
import com.example.ui.theme.PalePinkText
import com.example.ui.theme.PlumBorder
import com.example.ui.theme.PlumCard
import com.example.ui.theme.PlumSurface
import com.example.ui.theme.RosePinkPrimary
import com.example.ui.theme.SoftPinkContainer
import com.example.ui.theme.SoftWhite
import com.example.ui.theme.UrgentCoral
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.WineDark
import kotlinx.coroutines.delay

private enum class AuthStep {
    PHONE_INPUT,
    OTP_VERIFY
}

@Composable
fun AuthScreen(
    onAuthenticated: (name: String, phone: String, otp: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(AuthStep.PHONE_INPUT) }
    var name by remember { mutableStateOf("") }
    var phoneDigits by remember { mutableStateOf("") }
    var otpDigits by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var countdown by remember { mutableIntStateOf(30) }
    var isTimerActive by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isTimerActive, countdown) {
        if (isTimerActive && countdown > 0) {
            delay(1000)
            countdown -= 1
        } else if (countdown == 0) {
            isTimerActive = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WineDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Brand Logo & Header
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(PlumSurface)
                    .border(2.dp, GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.moonnight_icon),
                    contentDescription = "Moonnight Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "MOONNIGHT",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = ChampagneGold
                )
            )

            Text(
                text = "LUXURY IN-HOME SPA & WELLNESS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.6.sp,
                    color = BlushPinkAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Main Auth Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_card"),
                colors = CardDefaults.cardColors(containerColor = PlumCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, PlumBorder)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (step == AuthStep.PHONE_INPUT) {
                        // STEP 1: Phone & Name Input
                        Text(
                            text = "Guest Verification",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SoftWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Enter your mobile number to receive an instant 6-digit secure verification code.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightGrayText,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Full Name Input
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Your Name",
                                style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("Enter your name", color = MutedText, fontSize = 13.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RosePinkPrimary,
                                    unfocusedBorderColor = PlumBorder,
                                    focusedTextColor = SoftWhite,
                                    unfocusedTextColor = SoftWhite,
                                    focusedContainerColor = PlumSurface,
                                    unfocusedContainerColor = PlumSurface
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Phone Number Input with +91 Badge
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Mobile Number *",
                                style = MaterialTheme.typography.labelMedium.copy(color = LightGrayText)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = SoftPinkContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, RosePinkPrimary.copy(alpha = 0.5f)),
                                    modifier = Modifier.height(56.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(horizontal = 14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🇮🇳 +91",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ChampagneGold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedTextField(
                                    value = phoneDigits,
                                    onValueChange = {
                                        if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                                            phoneDigits = it
                                            errorMessage = null
                                        }
                                    },
                                    placeholder = { Text("10-digit number", color = MutedText, fontSize = 13.sp) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("auth_phone_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RosePinkPrimary,
                                        unfocusedBorderColor = PlumBorder,
                                        focusedTextColor = SoftWhite,
                                        unfocusedTextColor = SoftWhite,
                                        focusedContainerColor = PlumSurface,
                                        unfocusedContainerColor = PlumSurface
                                    ),
                                    singleLine = true
                                )
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage!!,
                                color = UrgentCoral,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = {
                                if (phoneDigits.length != 10) {
                                    errorMessage = "Please enter a valid 10-digit mobile number."
                                } else {
                                    errorMessage = null
                                    step = AuthStep.OTP_VERIFY
                                    countdown = 30
                                    isTimerActive = true
                                    otpDigits = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RosePinkPrimary,
                                contentColor = SoftWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("send_otp_btn")
                        ) {
                            Text(
                                text = "Get 6-Digit Verification Code",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }

                    } else {
                        // STEP 2: 6-Digit OTP Verification Screen
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    step = AuthStep.PHONE_INPUT
                                    errorMessage = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = ChampagneGold
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verification Code",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWhite
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Enter the 6-digit verification code sent to +91 $phoneDigits",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LightGrayText,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 6-Digit Visual Pin Boxes
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_boxes_row"),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0 until 6) {
                                val digitChar = otpDigits.getOrNull(i)?.toString() ?: ""
                                val isFocused = otpDigits.length == i

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    color = PlumSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        if (isFocused) 1.8.dp else 1.dp,
                                        if (isFocused) GoldAccent else if (digitChar.isNotEmpty()) RosePinkPrimary else PlumBorder
                                    )
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = digitChar,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ChampagneGold,
                                                fontSize = 20.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Hidden/Overlay Numeric Input Field for 6 Digits
                        OutlinedTextField(
                            value = otpDigits,
                            onValueChange = {
                                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                    otpDigits = it
                                    errorMessage = null
                                    if (it.length == 6) {
                                        keyboardController?.hide()
                                    }
                                }
                            },
                            placeholder = { Text("Tap here to type 6 digits", color = MutedText, fontSize = 12.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RosePinkPrimary,
                                unfocusedBorderColor = PlumBorder,
                                focusedTextColor = SoftWhite,
                                unfocusedTextColor = SoftWhite,
                                focusedContainerColor = PlumSurface,
                                unfocusedContainerColor = PlumSurface
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Test Demo Helper
                        Surface(
                            color = SoftPinkContainer,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.7.dp, BlushPinkAccent.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { otpDigits = "123456" }
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = PalePinkText, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Demo Code: 123456 (Tap to Auto-fill)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = PalePinkText,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage!!,
                                color = UrgentCoral,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (otpDigits.length != 6) {
                                    errorMessage = "Please enter all 6 digits of the verification code."
                                } else {
                                    val success = onAuthenticated(
                                        name.ifBlank { "Guest User" },
                                        "+91 $phoneDigits",
                                        otpDigits
                                    )
                                    if (!success) {
                                        errorMessage = "Invalid verification code. Please check and try again."
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RosePinkPrimary,
                                contentColor = SoftWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("verify_otp_btn")
                        ) {
                            Text(
                                text = "Verify & Access Moonnight",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isTimerActive) {
                                Text(
                                    text = "Resend Code in ${countdown}s",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MutedText)
                                )
                            } else {
                                TextButton(
                                    onClick = {
                                        countdown = 30
                                        isTimerActive = true
                                        errorMessage = null
                                    }
                                ) {
                                    Text(
                                        text = "Resend Code",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = ChampagneGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "100% Encrypted & Privacy Protected",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LightGrayText,
                                fontSize = 10.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
