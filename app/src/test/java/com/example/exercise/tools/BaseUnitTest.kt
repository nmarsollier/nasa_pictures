package com.example.exercise.tools

import android.content.Context
import com.example.exercise.models.api.koinApiModule
import com.example.exercise.models.database.config.LocalDatabase
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.ImageEntityDao
import com.example.exercise.models.extendedDate.FrescoUtils
import com.example.exercise.models.modelsModule
import com.example.exercise.ui.koinViewModelModule
import com.google.gson.GsonBuilder
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
abstract class BaseUnitTest : KoinTest {
    /**
     * http mocking server.
     *
     * In the tests we can mock different endpoints to different answers.
     * Refer to TestHttpServer docs in the file.
     */
    val testHttpServer: TestHttpServer = TestHttpServer()

    lateinit var frescoUtilsMock: FrescoUtils
    lateinit var imagesDao: ImageEntityDao
    lateinit var datesDao: DatesEntityDao

    init {
        Dispatchers.setMain(Dispatchers.Default)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    /**
     * Koin Modules used in the tests, Most modules are the same as the app, but some
     * libraries needs to be mocked for testing purposes.
     */
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            testModule,
            koinApiModule,
            modelsModule,
            koinViewModelModule
        )
    }

    /**
     * Mocked dependencies for testing purposes.
     */
    private val testModule = module {
        single {
            Retrofit.Builder()
                .baseUrl("http://localhost/")
                .client(buildOkHttpClientMock(testHttpServer))
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .build()
        }

        single<Context> {
            mockk(relaxed = true)
        }

        single { Mutex() }

        single<FrescoUtils> {
            mockFresco().also {
                frescoUtilsMock = it
            }
        }

        single<LocalDatabase> {
            mockDataBase().also {
                imagesDao = it.imageDao()
                datesDao = it.datesDao()
            }
        }
    }
}
