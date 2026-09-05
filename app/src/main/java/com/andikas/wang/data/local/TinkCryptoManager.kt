package com.andikas.wang.data.local

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

object TinkCryptoManager {
    private const val KEYSET_NAME = "wang_datastore_keyset"
    private const val PREF_FILE_NAME = "wang_tink_keyset_prefs"
    private const val MASTER_KEY_URI = "android-keystore://wang_master_key"

    fun getAead(context: Context): Aead {
        AeadConfig.register()
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context.applicationContext, KEYSET_NAME, PREF_FILE_NAME)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }
}
