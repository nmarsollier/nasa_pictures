package com.nmarsollier.nasa.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmarsollier.nasa.common.res.AppColors

@Composable
fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BlueBackground)
            .padding(bottom = 120.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(120.dp),
            color = AppColors.Primary,
            strokeWidth = 12.dp
        )
    }
}

@Preview
@Composable
fun LoadingViewPreview() {
    LoadingView()
}

