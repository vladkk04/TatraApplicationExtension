package com.flexcil.flexc.navigation.di

import com.flexcil.flexc.navigation.base.AppNavigator
import com.flexcil.flexc.core.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AppNavigatorModule {

    @Binds
    fun bindAppNavigator(impl: AppNavigator): Navigator

}
