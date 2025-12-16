package com.bolashak.wildberries.presentation.favorites

import androidx.lifecycle.ViewModel
import com.bolashak.wildberries.domain.manager.FavoritesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesManager: FavoritesManager
) : ViewModel() {
    val favorites = favoritesManager.favorites
}
