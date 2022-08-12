package com.euzhene.instagramcollage.utils

import android.content.Context
import android.provider.MediaStore

fun getAllImagesPaths(context: Context): List<String> {
    val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
    val orderBy = MediaStore.Images.Media._ID

    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
        null, orderBy
    ) ?: return emptyList()

    val count: Int = cursor.count

    val listPath = mutableListOf<String>()

    for (i in 0 until count) {
        cursor.moveToPosition(i)
        val dataColumnIndex: Int = cursor.getColumnIndex(MediaStore.Images.Media.DATA)

        listPath.add(cursor.getString(dataColumnIndex))
    }

    cursor.close()
    return listPath
}