package com.advocatepedia.search.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.advocatepedia.search.room.dao.WikiRoomDao
import com.advocatepedia.search.room.entity.WikiRoomEntity


@Database(entities = [WikiRoomEntity::class], version = 1, exportSchema = false)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun wikiDao(): WikiRoomDao
}

private lateinit var INSTANCE: RoomDataBase

fun getDatabase(context: Context): RoomDataBase {

    synchronized(RoomDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                RoomDataBase::class.java,
                "database"
            ).build()
        }
    }

    return INSTANCE
}
