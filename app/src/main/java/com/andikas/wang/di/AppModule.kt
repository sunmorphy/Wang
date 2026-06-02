package com.andikas.wang.di

import com.andikas.wang.BuildConfig
import com.andikas.wang.data.local.DatabaseModule
import com.andikas.wang.data.local.PreferenceManager
import com.andikas.wang.data.local.WangDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.andikas.wang.engine.stats.FinancialStatisticsEngine
import com.andikas.wang.engine.anomaly.AnomalyDetector

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
    single { get<WangDatabase>().recommendationDao() }

    single { FinancialStatisticsEngine(get()) }
    single { AnomalyDetector(get()) }

//    viewModel { PinViewModel(get()) }
//    viewModel { SetupViewModel(get()) }
//    viewModel { DashboardViewModel(get()) }
//    viewModel { WalletViewModel(get()) }
//    viewModel { TransactionViewModel(get()) }
}
