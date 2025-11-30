package com.svault.citymoments

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseModule {
    @Provides
    @Singleton
    fun provideMomentDatabase(application: Application): MomentDatabase =
        MomentDatabase.initDatabase(application)

    @Provides
    @Singleton
    fun provideMomentDao(momentDatabase: MomentDatabase): MomentDao {
        return momentDatabase.momentDao()
    }

    @Provides
    @Singleton
    fun provideMomentRepository(dao: MomentDao): MomentRepository {
        return MomentRepository(dao)
    }
}