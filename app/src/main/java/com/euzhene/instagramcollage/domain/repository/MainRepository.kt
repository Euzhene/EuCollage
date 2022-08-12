package com.euzhene.instagramcollage.domain.repository

import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun saveData(list: List<String?>)
    fun loadData(): Flow<List<String?>>
}