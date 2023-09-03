package com.example.exercise.tools

import com.example.exercise.models.database.config.Database
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.image.ImageEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject

fun mocksDataBase() {
    mockkObject(Database)
    coEvery { Database.getRoomDatabase() } returns mockk {
        every { imageDao() } returns mockk {
            val mockImages = mutableMapOf<String, ImageEntity>()

            every { findById(any()) } answers {
                mockImages[firstArg()]
            }

            every { findByDate(any()) } answers {
                val date = firstArg<String>()

                mockImages.filter { it.value.day == date }.map { it.value }.sortedBy { it.date }
            }

            every { insert(any()) } answers {
                val image = firstArg<ImageEntity>()
                mockImages[image.identifier] = image
            }
        }

        every { datesDao() } returns mockk {
            val mockDates = mutableMapOf<String, DatesEntity>()

            every { findAll() } answers {
                mockDates.map { it.value }.sortedByDescending { it.date }
            }

            every { insert(any()) } answers {
                val date = firstArg<DatesEntity>()
                mockDates[date.date] = date
            }
        }
    }
}

