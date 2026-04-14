package com.tutushubham.studypartner.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.tutushubham.studypartner.core.ui.R

private val interProvider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

private fun inter(weight: FontWeight) =
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = interProvider,
        weight = weight,
    )

private val InterFontFamily =
    FontFamily(
        inter(FontWeight.Normal),
        inter(FontWeight.Medium),
        inter(FontWeight.SemiBold),
        inter(FontWeight.Bold),
    )

private val base = Typography()

val FocusTypography =
    Typography(
        displayLarge = base.displayLarge.copy(fontFamily = InterFontFamily),
        headlineSmall = base.headlineSmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
        titleMedium = base.titleMedium.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold),
        bodyLarge = base.bodyLarge.copy(fontFamily = InterFontFamily, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium = base.bodyMedium.copy(fontFamily = InterFontFamily),
        bodySmall = base.bodySmall.copy(fontFamily = InterFontFamily),
        labelLarge = base.labelLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
        labelMedium = base.labelMedium.copy(fontFamily = InterFontFamily),
    )
