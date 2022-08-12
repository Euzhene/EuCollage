package com.euzhene.instagramcollage.hilt

import android.content.Context
import com.euzhene.instagramcollage.data.database.CollageDao
import com.euzhene.instagramcollage.data.database.CollageRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DataModule {

    @Provides
    fun provideCollageDao(@ApplicationContext context: Context): CollageDao {
        return CollageRoom.getInstance(context).dao()
    }


}