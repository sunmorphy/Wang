package com.andikas.wang.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.wang.data.local.PreferenceManager
import com.andikas.wang.data.local.dao.BudgetCategoryDao
import com.andikas.wang.data.local.dao.BudgetDao
import com.andikas.wang.data.local.dao.CategoryDao
import com.andikas.wang.data.local.dao.WalletDao
import com.andikas.wang.data.local.entities.BudgetCategoryEntity
import com.andikas.wang.data.local.entities.BudgetEntity
import com.andikas.wang.data.local.entities.CategoryEntity
import com.andikas.wang.data.local.entities.WalletEntity
import com.andikas.wang.domain.model.vo.CategoryOption
import com.andikas.wang.domain.model.vo.CurrencyType
import com.andikas.wang.domain.model.vo.WalletType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SetupViewModel(
    private val walletDao: WalletDao,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val budgetCategoryDao: BudgetCategoryDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _isCurrencySetupDone = MutableStateFlow(false)
    val isCurrencySetupDone: StateFlow<Boolean> = _isCurrencySetupDone.asStateFlow()

    private val _isWalletSetupDone = MutableStateFlow(false)
    val isWalletSetupDone: StateFlow<Boolean> = _isWalletSetupDone.asStateFlow()

    private val _isBudgetSetupDone = MutableStateFlow(false)
    val isBudgetSetupDone: StateFlow<Boolean> = _isBudgetSetupDone.asStateFlow()

    private val _selectedCurrency = MutableStateFlow(CurrencyType.IDR)
    val selectedCurrency: StateFlow<CurrencyType> = _selectedCurrency.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryOption>>(emptyList())
    val categories: StateFlow<List<CategoryOption>> = _categories.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceManager.userPreferences.collect { prefs ->
                if (prefs.currencyCode.isNotEmpty()) {
                    _isCurrencySetupDone.value = true
                }
                val type =
                    CurrencyType.entries.find { it.name == prefs.currencyCode } ?: CurrencyType.IDR
                _selectedCurrency.value = type
            }
        }
        viewModelScope.launch {
            walletDao.getAllWallets().collect { wallets ->
                _isWalletSetupDone.value = wallets.isNotEmpty()
            }
        }
        viewModelScope.launch {
            budgetDao.getAllBudgets().collect { budgets ->
                _isBudgetSetupDone.value = budgets.isNotEmpty()
            }
        }
        viewModelScope.launch {
            categoryDao.getAllCategories().collect { entities ->
                val options = entities.map {
                    CategoryOption(
                        id = it.id.toString(),
                        label = it.name,
                        icon = CategoryOption.getCategoryIcon(it.name),
                        isDefault = it.isDefault
                    )
                }.toMutableList()
                options.add(
                    CategoryOption(
                        id = "0",
                        label = "Other",
                        isDefault = true
                    )
                )
                _categories.value = options
            }
        }
    }

    fun saveCurrency(currency: CurrencyType) {
        viewModelScope.launch {
            preferenceManager.saveCurrency(currency.name)
            _selectedCurrency.value = currency
            _isCurrencySetupDone.value = true
        }
    }

    fun saveWallet(
        name: String,
        type: WalletType,
        balance: Double,
        defaultName: String = "Main Wallet"
    ) {
        viewModelScope.launch {
            val wallet = WalletEntity(
                name = name.ifBlank { defaultName },
                type = type.name
            )
            walletDao.insertWallet(wallet)
            _isWalletSetupDone.value = true
        }
    }

    fun saveBudget(amount: Double, categoryId: String? = null, newCategoryName: String? = null) {
        viewModelScope.launch {
            var targetCategoryId = categoryId?.toIntOrNull()

            if (!newCategoryName.isNullOrBlank()) {
                val trimmedName = newCategoryName.trim()
                val existing = categoryDao.getCategoryByName(trimmedName)
                if (existing != null) {
                    targetCategoryId = existing.id
                } else {
                    val newCategory = CategoryEntity(
                        name = trimmedName,
                        isDefault = false
                    )
                    val insertedCatId = categoryDao.insertCategory(newCategory)
                    if (insertedCatId > 0) {
                        targetCategoryId = insertedCatId.toInt()
                    }
                }
            }

            val monthYear = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
            val budget = BudgetEntity(
                monthYear = monthYear,
                totalBudget = amount.toFloat()
            )
            val budgetId = budgetDao.insertBudget(budget)

            if (targetCategoryId != null && targetCategoryId > 0 && budgetId > 0) {
                val budgetCategory = BudgetCategoryEntity(
                    budgetId = budgetId.toInt(),
                    categoryId = targetCategoryId,
                    allocatedAmount = amount.toFloat()
                )
                budgetCategoryDao.insertBudgetCategory(budgetCategory)
            }

            _isCurrencySetupDone.value = true
            _isBudgetSetupDone.value = true
        }
    }

    fun completeSetup(onCompleted: () -> Unit) {
        viewModelScope.launch {
            preferenceManager.setOnboardingCompleted(true)
            onCompleted()
        }
    }
}
