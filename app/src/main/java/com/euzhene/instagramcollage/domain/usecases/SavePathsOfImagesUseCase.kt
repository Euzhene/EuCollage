package com.euzhene.instagramcollage.domain.usecases

import com.euzhene.instagramcollage.domain.repository.MainRepository
import javax.inject.Inject

class SavePathsOfImagesUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(list: List<String?>) {
        return repository.saveData(list)
    }
}