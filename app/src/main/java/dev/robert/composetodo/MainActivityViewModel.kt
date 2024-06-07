package dev.robert.composetodo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.design_system.domain.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        themeRepository: ThemeRepository,
    ) : ViewModel() {
        val currentTheme = themeRepository.themeValue
    }
