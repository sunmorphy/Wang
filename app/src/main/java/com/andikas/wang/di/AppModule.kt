package com.andikas.wang.di

import com.andikas.wang.BuildConfig
import com.andikas.wang.data.local.DatabaseModule
import com.andikas.wang.data.local.PreferenceManager
import com.andikas.wang.data.local.WangDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { PreferenceManager(androidContext()) }

    single {
        // Note: In a real app, passphrase should be handled securely.
        // For now, we use a hardcoded one as per the initial setup.
        val passphrase = BuildConfig.PASSPHRASE.toByteArray()
        DatabaseModule.getDatabase(androidContext(), passphrase)
    }

    single { get<WangDatabase>().walletDao() }
    single { get<WangDatabase>().transactionDao() }
    single { get<WangDatabase>().budgetDao() }
    single { get<WangDatabase>().goalDao() }

//    viewModel { PinViewModel(get()) }
//    viewModel { SetupViewModel(get()) }
//    viewModel { DashboardViewModel(get()) }
//    viewModel { WalletViewModel(get()) }
//    viewModel { TransactionViewModel(get()) }
}
