package com.andikas.wang.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WalletType { BANK, EWALLET, CASH }

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: WalletType,
    val balance: Double = 0.0
)
