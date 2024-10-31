package com.nmarsollier.nasa.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmarsollier.nasa.R

@Composable
fun HomeMenu(
) {
    TopAppBar(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.lightBlueBackground),
                        colorResource(id = R.color.blueBackground)
                    )
                )
            )
            .padding(top = 24.dp),
        elevation = 0.dp,
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = Color.Transparent,
        contentColor = colorResource(id = R.color.textWhite),
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainMenuPreview() {
    MaterialTheme {
        Column {
            HomeMenu()
        }
    }
}
