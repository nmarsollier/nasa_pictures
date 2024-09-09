package com.example.exercise.tools

import com.example.exercise.common.utils.toDateTimeString
import com.example.exercise.models.database.config.LocalDatabase
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.ImageEntity
import com.example.exercise.models.database.image.ImageEntityDao
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

fun mockDataBase(): LocalDatabase {
    val imageDao = mockk<ImageEntityDao> {
        val mockImages = mutableMapOf<String, ImageEntity>()

        coEvery { findByDate(any()) } answers {
            val date = firstArg<String>()

            mockImages.filter { it.value.day == date }.map { it.value }.sortedBy { it.date }
        }

        coEvery { insert(any()) } answers {
            val image = firstArg<ImageEntity>()
            mockImages[image.identifier] = image
        }
    }

    val datesDao = mockk<DatesEntityDao> {
        val mockDates = mutableMapOf<String, DatesEntity>()

        coEvery { findAll(any(), any()) } answers {
            mockDates.map { it.value }.sortedByDescending { it.date }
        }

        coEvery { findLast() } answers {
            mockDates.map { it.value }.maxByOrNull { it.date }
        }

        coEvery { insert(any()) } answers {
            val date = firstArg<DatesEntity>()
            mockDates[date.date] = date

            LocalDateTime.now().plusDays(1).toDateTimeString.let {
                mockDates[it] = DatesEntity(it)
            }
        }
    }

    return mockk<LocalDatabase> {
        every { imageDao() } returns imageDao
        every { datesDao() } returns datesDao
    }
}
