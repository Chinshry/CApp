package com.chinshry.base.db

import android.content.Context
import com.chinshry.base.db.dao.BadgeDao

/**
 * Created by chinshry on 2022/03/21.
 */
object DatabaseManager {
    private var appDatabase: AppDatabase? = null

    fun getBadgeDao(context: Context): BadgeDao {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInstance(context)
        }
        return appDatabase!!.badgeDao()
    }

}