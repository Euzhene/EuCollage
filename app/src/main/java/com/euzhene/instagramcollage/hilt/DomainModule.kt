package com.euzhene.instagramcollage.hilt

import com.euzhene.instagramcollage.data.MainRepositoryImpl
import com.euzhene.instagramcollage.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindCollageRepository(impl: MainRepositoryImpl): MainRepository
}