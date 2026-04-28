package com.devanshdroid.disasterbuddy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devanshdroid.disasterbuddy.core.utils.Constants

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        Constants.STATUS_PENDING  -> Color(0xFFFFF3CD) to Color(0xFF856404)
        Constants.STATUS_ACCEPTED -> Color(0xFFCCE5FF) to Color(0xFF004085)
        Constants.STATUS_RESOLVED -> Color(0xFFD4EDDA) to Color(0xFF155724)
        else                      -> Color(0xFFF8F9FA) to Color(0xFF6C757D)
    }
    Text(
        text = status,
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}