package com.flexcil.flexc.menu

import com.flexcil.flexc.core.datastore.preference.GamePreferences
import com.flexcil.flexc.core.datastore.preference.UserPreferences

data class MenuState(
    val userPreferences: UserPreferences = UserPreferences(),
    val gamePreferences: GamePreferences = GamePreferences()
)
