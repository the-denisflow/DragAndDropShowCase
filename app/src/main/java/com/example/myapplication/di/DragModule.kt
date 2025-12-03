package com.example.myapplication.di

import com.example.myapplication.data.repository.DragHelperImpl
import com.example.myapplication.data.repository.DragListenerImpl
import com.example.myapplication.data.repository.TilesRepositoryImpl
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.domain.repository.TilesRepository
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
    fun provideTilesRepository(logger: AppLogger): TilesRepository  = TilesRepositoryImpl(logger)

    @Provides
    @Singleton
    fun provideDragHelper( repository: TilesRepository): DragHelper  =
        DragHelperImpl(repository)


    @Provides
    @Singleton
    fun provideDragListener(logger: AppLogger, dragHelper: DragHelper): DragListener  = DragListenerImpl(logger,dragHelper)


}