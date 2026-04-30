package com.example.qrcodeapp.core.di

import androidx.room.Room
import com.example.qrcodeapp.data.local.database.AppDatabase
import com.example.qrcodeapp.data.mapper.BarCodeGetter
import com.example.qrcodeapp.data.mapper.QrCodeListProvider
import com.example.qrcodeapp.data.mapper.QrTemplateProcessor
import com.example.qrcodeapp.data.repository.BarCodeGenerateRepositoryImpl
import com.example.qrcodeapp.data.repository.BarCodeRepositoryImpl
import com.example.qrcodeapp.data.repository.CameraRepositoryImpl
import com.example.qrcodeapp.data.repository.HistoryCreateRepositoryImpl
import com.example.qrcodeapp.data.repository.HistoryRepositoryImpl
import com.example.qrcodeapp.data.repository.TemplateRepositoryImpl
import com.example.qrcodeapp.domain.formatter.ContactFormatter
import com.example.qrcodeapp.domain.formatter.WifiFormatter
import com.example.qrcodeapp.domain.repository.BarCodeRepository
import com.example.qrcodeapp.domain.repository.CameraRepository
import com.example.qrcodeapp.domain.repository.GenerateBarCodeRepository
import com.example.qrcodeapp.domain.repository.HistoryCreateRepository
import com.example.qrcodeapp.domain.repository.HistoryRepository
import com.example.qrcodeapp.domain.repository.TemplateRepository
import com.example.qrcodeapp.domain.usecase.ApplyTemplateUseCase
import com.example.qrcodeapp.domain.usecase.BarCodeShowUseCase
import com.example.qrcodeapp.domain.usecase.CameraUseCase
import com.example.qrcodeapp.domain.usecase.DeleteHistoryUseCase
import com.example.qrcodeapp.domain.usecase.FavouriteUseCase
import com.example.qrcodeapp.domain.usecase.FetchHistoryCreateUseCase
import com.example.qrcodeapp.domain.usecase.FetchHistoryUseCase
import com.example.qrcodeapp.domain.usecase.GenerateBarCodeUseCase
import com.example.qrcodeapp.domain.usecase.PrepareQrDataUseCase
import com.example.qrcodeapp.domain.usecase.SaveHistoryCreateUseCase
import com.example.qrcodeapp.domain.usecase.SaveHistoryUseCase
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        QrEditorViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(), get(), get(),
            get(),
            get()
        )
    }

    single<TemplateRepository> { TemplateRepositoryImpl(get()) }
    factory { ApplyTemplateUseCase(get()) }
    single { QrTemplateProcessor() }



    factory { BarCodeShowUseCase(get()) }
    single<BarCodeRepository> { BarCodeRepositoryImpl(get()) }
    single { BarCodeGetter() }


    factory { PrepareQrDataUseCase(get()) }
    factory { ContactFormatter() }
    factory { WifiFormatter() }

    single {
        listOf(
            WifiFormatter(),
            ContactFormatter()
        )
    }


    single<GenerateBarCodeRepository> { BarCodeGenerateRepositoryImpl() }

    single<CameraRepository> { CameraRepositoryImpl(get()) }

    factory { CameraUseCase(get()) }
    factory { GenerateBarCodeUseCase(get()) }


    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "qrcode_database"
        ).build()
    }

    // ✅ DAO — from database object
    single {
        get<AppDatabase>().historyDao()
    }

    // ✅ Repository — inject dao into impl
    single<HistoryRepository> {
        HistoryRepositoryImpl(dao = get())
    }
    // ✅ UseCases
    factory { SaveHistoryUseCase(repository = get()) }
    factory { FetchHistoryUseCase(repository = get()) }
    factory { DeleteHistoryUseCase(repository = get()) }
    factory { FavouriteUseCase(repository = get()) }

    // ✅ Repository — inject dao into impl
    single<HistoryCreateRepository> {
        HistoryCreateRepositoryImpl(dao = get())
    }
    factory { SaveHistoryCreateUseCase(repository = get()) }
    factory { FetchHistoryCreateUseCase(repository = get()) }


    single { QrCodeListProvider() }


}