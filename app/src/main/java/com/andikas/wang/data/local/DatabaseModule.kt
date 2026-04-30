package com.andikas.wang.data.local

import android.content.Context
import androidx.room.Room
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

object DatabaseModule {
    private var INSTANCE: WangDatabase? = null

    fun getDatabase(context: Context, passphrase: ByteArray): WangDatabase {
        val existingInstance = INSTANCE
        if (existingInstance != null) {
            return existingInstance
        }

        return synchronized(this) {
            // Initialize SQLCipher libraries
            System.loadLibrary("sqlcipher")
            val factory = SupportOpenHelperFactory(passphrase)
            val instance = Room.databaseBuilder(
                context.applicationContext,
                WangDatabase::class.java,
                "wang.db"
            ).openHelperFactory(factory)
                .fallbackToDestructiveMigration(false)
                .build()
            INSTANCE = instance
            instance
        }
    }

    fun clearInstance() {
        INSTANCE = null
    }
}