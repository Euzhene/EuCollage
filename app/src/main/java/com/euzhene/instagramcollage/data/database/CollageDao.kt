package com.euzhene.instagramcollage.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CollageDao {
    @Query("select * from imagepath")
    fun loadPathsOfImages(): Flow<List<ImagePath>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePathsOfImages(list: List<ImagePath>)

    @Query("delete from imagepath")
    suspend fun deleteOldPathsOfImages()
}