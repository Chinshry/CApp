package com.chinshry.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chinshry.base.db.dao.BadgeDao
import java.io.File

/**
 * Created by chinshry on 2022/03/21.
 */
@Database(
    entities = [BadgeDataModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun badgeDao(): BadgeDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    val dbPath = context.filesDir.absolutePath + File.separator + "../app_db"
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, dbPath
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance!!
        }
    }
}