package com.nmarsollier.nasa.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmarsollier.nasa.res.AppColors
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.ui.utils.ExtendedDateValueSamples
import com.nmarsollier.nasa.ui.utils.KoinPreview

@Composable
@ExperimentalFoundationApi
fun DateItemView(date: ExtendedDateValue, reduce: (MainAction) -> Unit) {
    val image = remember(date) {
        when {
            date.isLoading -> Icons.Default.Refresh
            !date.needsLoad -> Icons.Default.Check
            else -> null
        }
    }
    val text = remember(date) {
        when {
            date.needsLoad -> "Start downloading"
            else -> "${date.caches}/${date.count}"
        }
    }
    val color = remember(date) {
        when {
            date.isLoading -> AppColors.TextColorYellow
            !date.needsLoad -> AppColors.TextColorGreen
            else -> AppColors.TextColorLightGray
        }
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = (AppColors.BlueCardBackground),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
            .fillMaxWidth()
            .combinedClickable(onClick = {})
            .clickable { reduce(MainAction.GoImages(date)) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                .background(AppColors.BlueCardBackground)
        ) {
            Column {
                Text(
                    text = date.formattedDayString,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextWhite
                )
                Text(
                    text = date.formattedDateString,
                    fontSize = 14.sp,
                    color = AppColors.TextColorLightGray
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                image?.let {
                    Icon(
                        it,
                        contentDescription = "",
                        modifier = Modifier.size(16.dp),
                        tint = color
                    )
                }

                Text(
                    text = text, fontSize = 14.sp, color = color
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "",
                    tint = AppColors.TextColorLightGray
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun DateItemViewPreview() {
    KoinPreview {
        Column {
            DateItemView(
                ExtendedDateValueSamples.partialLoadedExtendedDateValueSample
            ) {}
            DateItemView(
                ExtendedDateValueSamples.fullyLoadedExtendedDateValueSample
            ) {}
            DateItemView(
                ExtendedDateValueSamples.unloadedLoadedExtendedDateValueSample
            ) {}
        }
    }
}
