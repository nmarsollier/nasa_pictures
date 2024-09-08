package com.example.exercise.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exercise.R
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.common.ui.KoinPreview
import com.example.exercise.ui.utils.Samples

@Composable
@ExperimentalFoundationApi
fun DateItemView(date: ExtendedDateValue, reduce: (MainAction) -> Unit) {
    Card(shape = RoundedCornerShape(10.dp),
        backgroundColor = (colorResource(id = R.color.blueCardBackground)),
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
                .background(colorResource(id = R.color.blueCardBackground))
        ) {
            Column {
                Text(
                    text = date.formattedDayString,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textWhite)
                )
                Text(
                    text = date.formattedDateString,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textColorLightGray)
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(imageResource(date)), "", modifier = Modifier.size(16.dp)
                )
                Text(
                    text = text(date), fontSize = 14.sp, color = colorResource(id = textColor(date))
                )
                Image(
                    painterResource(R.drawable.ic_next), ""
                )
            }
        }
    }
}

fun imageResource(date: ExtendedDateValue): Int {
    return when {
        date.isLoading -> R.drawable.ic_reload
        !date.needsLoad -> R.drawable.ic_clock
        else -> R.drawable.ic_empty
    }
}

fun textColor(date: ExtendedDateValue): Int {
    return when {
        date.isLoading -> R.color.textColorYellow
        !date.needsLoad -> R.color.textColorGreen
        else -> R.color.textColorLightGray
    }
}

fun text(date: ExtendedDateValue): String {
    return when {
        date.needsLoad -> "Start downloading"
        else -> "${date.caches}/${date.count}"
    }
}


@ExperimentalFoundationApi
@Preview(showSystemUi = true)
@Composable
fun DateItemViewPreview() {
    KoinPreview {
        Column {
            DateItemView(
                ExtendedDateValue.Samples.partialLoadedExtendedDateValueSample,
                {}
            )
            DateItemView(
                ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample,
                {}
            )
            DateItemView(
                ExtendedDateValue.Samples.unloadedLoadedExtendedDateValueSample,
                {}
            )
        }
    }
}
