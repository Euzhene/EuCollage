package com.euzhene.instagramcollage.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

@Entity
data class ImagePath @Inject constructor(
    @PrimaryKey(autoGenerate = true)  val id:Int=0,
    val path:String?
)
