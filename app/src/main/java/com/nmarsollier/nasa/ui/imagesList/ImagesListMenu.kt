package com.nmarsollier.nasa.ui.imagesList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmarsollier.nasa.common.navigation.AppNavActions
import com.nmarsollier.nasa.common.res.AppColors
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.ui.utils.ExtendedDateValueSamples
import org.koin.compose.koinInject

@Composable
fun ImagesListMenu(
    date: ExtendedDateValue,
    navActions: AppNavActions = koinInject(),
    onClose: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColors.LightBlueBackground,
                        AppColors.BlueBackground
                    )
                )
            )
            .padding(top = 24.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        contentColor = AppColors.TextWhite,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.formattedDayString,
                    fontSize = 21.sp,
                    color = AppColors.TextWhite
                )
                Text(
                    text = date.formattedDateString,
                    fontSize = 14.sp,
                    color = AppColors.TextWhite
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onClose()
                navActions.goUp()
            }) {
                Icon(rememberVectorPainter(Icons.Default.ChevronLeft), "")
            }
        })
}

@Preview
@Composable
fun ImagesMenuPreview() {
    MaterialTheme {
        Column {
            ImagesListMenu(date = ExtendedDateValueSamples.fullyLoadedExtendedDateValueSample) {}
        }
    }
}
