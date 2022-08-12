package com.euzhene.instagramcollage.data

import com.euzhene.instagramcollage.data.database.CollageDao
import com.euzhene.instagramcollage.data.database.ImagePath
import com.euzhene.instagramcollage.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dao: CollageDao,
) : MainRepository {

    override suspend fun saveData(list: List<String?>) {
        dao.deleteOldPathsOfImages()
        val mappedList = list.map { ImagePath(path = it) }

        dao.savePathsOfImages(mappedList)
    }

    override fun loadData(): Flow<List<String?>> {
        return dao.loadPathsOfImages().map { it.map { it.path } }
    }

}

