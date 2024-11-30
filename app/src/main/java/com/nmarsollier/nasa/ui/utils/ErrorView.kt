package com.nmarsollier.nasa.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmarsollier.nasa.res.AppColors
import com.nmarsollier.nasa.res.AppStrings

@Composable
fun ErrorView(onClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            }
            .background(AppColors.BlueBackground)) {

        Image(
            rememberVectorPainter(Icons.Filled.Info),
            "",
            colorFilter = ColorFilter.tint(AppColors.Error),
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = AppStrings.errorLoadingData,
            color = AppColors.TextWhite,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = AppStrings.retry,
            color = AppColors.TextColorLightGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    ErrorView {

    }
}

