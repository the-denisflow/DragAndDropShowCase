package com.example.myapplication.di

import com.example.myapplication.data.repository.DragListenerImpl
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.shared.utils.AppLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DragModule {
    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger  = AppLogger()

    @Provides
    @Singleton
    fun provideDragListener(logger: AppLogger): DragListener  = DragListenerImpl(logger)

}