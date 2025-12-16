package com.bolashak.wildberries.presentation.profile

import androidx.lifecycle.ViewModel
import com.bolashak.wildberries.domain.manager.PurchasesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val purchasesManager: PurchasesManager
) : ViewModel() {
    val purchases = purchasesManager.purchases
}
