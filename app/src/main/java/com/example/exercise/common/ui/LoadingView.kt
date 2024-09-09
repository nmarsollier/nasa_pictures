package com.example.exercise.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercise.R

@Composable
fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blueBackground))
            .padding(bottom = 120.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(120.dp),
            color = colorResource(id = R.color.colorPrimary),
            strokeWidth = 12.dp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingViewPreview() {
    LoadingView()
}

