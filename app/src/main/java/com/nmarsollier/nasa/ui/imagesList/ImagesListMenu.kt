package com.nmarsollier.nasa.ui.imagesList

import android.app.Activity
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.navigation.AppNavActions
import com.nmarsollier.nasa.common.utils.Samples
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import org.koin.compose.koinInject

@Composable
fun ImagesListMenu(
    date: ExtendedDateValue,
    navActions: AppNavActions = koinInject(),
    onClose: () -> Unit,
) {
    val context = LocalContext.current as? Activity

    TopAppBar(modifier = Modifier
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
        backgroundColor = Color.Transparent,
        contentColor = colorResource(id = R.color.textWhite),
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.formattedDayString,
                    fontSize = 21.sp,
                    color = colorResource(id = R.color.textWhite)
                )
                Text(
                    text = date.formattedDateString,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textWhite)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onClose()
                navActions.goUp()
            }) {
                Icon(painterResource(id = R.drawable.ic_back), "")
            }
        })
}

@Preview(showSystemUi = true)
@Composable
fun ImagesMenuPreview() {
    MaterialTheme {
        Column {
            ImagesListMenu(date = ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample) {}
        }
    }
}
