package com.andikas.wang.di

import com.andikas.wang.BuildConfig
import com.andikas.wang.data.local.DatabaseModule
import com.andikas.wang.data.local.PreferenceManager
import com.andikas.wang.data.local.WangDatabase
import com.andikas.wang.ui.onboarding.OnboardingViewModel
import com.andikas.wang.ui.setup.SetupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { PreferenceManager(androidContext()) }

    single {
        val passphrase = BuildConfig.PASSPHRASE.toByteArray()
        DatabaseModule.getDatabase(androidContext(), passphrase)
    }

    single { get<WangDatabase>().walletDao() }
    single { get<WangDatabase>().transactionDao() }
    single { get<WangDatabase>().budgetDao() }
    single { get<WangDatabase>().goalDao() }
    single { get<WangDatabase>().categoryDao() }
    single { get<WangDatabase>().budgetCategoryDao() }

    factory { OnboardingViewModel(get()) }
    factory { SetupViewModel(get(), get(), get(), get(), get()) }
}
