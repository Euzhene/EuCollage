package com.euzhene.instagramcollage.domain.usecases

import com.euzhene.instagramcollage.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadPathsOfImagesUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(): Flow<List<String?>> {
        return repository.loadData();
    }
}