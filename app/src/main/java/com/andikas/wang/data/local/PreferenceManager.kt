package com.andikas.wang.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager(private val context: Context) {

    private val dataStore: DataStore<UserPreferencesProto> by lazy {
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer(TinkCryptoManager.getAead(context)),
            produceFile = { context.dataStoreFile("encrypted_user_preferences.pb") }
        )
    }

    val userPreferences: Flow<UserPreferencesProto> = dataStore.data

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data.map { it.isOnboardingComplete }
    val pin: Flow<String?> = dataStore.data.map { it.pin.ifEmpty { null } }
    val selectedLanguage: Flow<String> = dataStore.data.map { it.language.ifEmpty { "en" } }
    val selectedCurrency: Flow<String> = dataStore.data.map { it.currencyCode.ifEmpty { "IDR" } }
    val isFinUnlocked: Flow<Boolean> = dataStore.data.map { it.isFinUnlocked }
    val isBiometricEnabled: Flow<Boolean> = dataStore.data.map { it.isBiometricEnabled }
    val theme: Flow<String> = dataStore.data.map { it.theme.ifEmpty { "SYSTEM" } }
    val bubblePositionX: Flow<Float> = dataStore.data.map { it.bubblePositionX }
    val bubblePositionY: Flow<Float> = dataStore.data.map { it.bubblePositionY }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.updateData {
            it.toBuilder().setIsOnboardingComplete(completed).build()
        }
    }

    suspend fun savePin(pin: String?) {
        dataStore.updateData {
            it.toBuilder().setPin(pin ?: "").build()
        }
    }

    suspend fun saveLanguage(language: String) {
        dataStore.updateData {
            it.toBuilder().setLanguage(language).build()
        }
    }

    suspend fun saveCurrency(currency: String) {
        dataStore.updateData {
            it.toBuilder().setCurrencyCode(currency).build()
        }
    }

    suspend fun setFinUnlocked(unlocked: Boolean) {
        dataStore.updateData {
            it.toBuilder().setIsFinUnlocked(unlocked).build()
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.updateData {
            it.toBuilder().setIsBiometricEnabled(enabled).build()
        }
    }

    suspend fun saveTheme(theme: String) {
        dataStore.updateData {
            it.toBuilder().setTheme(theme).build()
        }
    }

    suspend fun updateBubblePosition(x: Float, y: Float) {
        dataStore.updateData {
            it.toBuilder()
                .setBubblePositionX(x)
                .setBubblePositionY(y)
                .build()
        }
    }
}
