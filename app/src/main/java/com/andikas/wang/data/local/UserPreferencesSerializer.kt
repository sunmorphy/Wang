package com.andikas.wang.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

class UserPreferencesSerializer(
    private val aead: Aead
) : Serializer<UserPreferencesProto> {

    override val defaultValue: UserPreferencesProto
        get() = UserPreferencesProto.getDefaultInstance().toBuilder()
            .setCurrencyCode("IDR")
            .setLanguage("id")
            .setTheme("SYSTEM")
            .build()

    override suspend fun readFrom(input: InputStream): UserPreferencesProto {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) {
            return defaultValue
        }

        return try {
            val decryptedBytes = aead.decrypt(encryptedBytes, null)
            UserPreferencesProto.parseFrom(decryptedBytes)
        } catch (e: Exception) {
            when (e) {
                is InvalidProtocolBufferException, is IllegalArgumentException -> throw CorruptionException("Cannot read proto preferences", e)
                else -> throw e
            }
        }
    }

    override suspend fun writeTo(t: UserPreferencesProto, output: OutputStream) {
        val plaintextBytes = t.toByteArray()
        val encryptedBytes = aead.encrypt(plaintextBytes, null)
        output.write(encryptedBytes)
    }
}
