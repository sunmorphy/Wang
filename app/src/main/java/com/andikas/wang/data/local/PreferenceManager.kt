package com.andikas.wang.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {

    companion object {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val PIN_KEY = stringPreferencesKey("pin")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val CURRENCY_KEY = stringPreferencesKey("currency")
        val IS_FIN_UNLOCKED = booleanPreferencesKey("is_fin_unlocked")
    }

    val isOnboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { it[IS_ONBOARDING_COMPLETED] ?: false }
    val pin: Flow<String?> = context.dataStore.data.map { it[PIN_KEY] }
    val selectedLanguage: Flow<String> = context.dataStore.data.map { it[LANGUAGE_KEY] ?: "en" }
    val selectedCurrency: Flow<String> = context.dataStore.data.map { it[CURRENCY_KEY] ?: "IDR" }
    val isFinUnlocked: Flow<Boolean> =
        context.dataStore.data.map { it[IS_FIN_UNLOCKED] ?: false }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[IS_ONBOARDING_COMPLETED] = completed }
    }

    suspend fun savePin(pin: String) {
        context.dataStore.edit { it[PIN_KEY] = pin }
    }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }

    suspend fun saveCurrency(currency: String) {
        context.dataStore.edit { it[CURRENCY_KEY] = currency }
    }

    suspend fun setFinUnlocked(unlocked: Boolean) {
        context.dataStore.edit { it[IS_FIN_UNLOCKED] = unlocked }
    }
}

