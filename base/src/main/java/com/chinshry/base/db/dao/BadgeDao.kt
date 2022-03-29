package com.chinshry.base.db.dao

import androidx.room.*
import com.chinshry.base.db.BadgeDataModel

/**
 * Created by chinshry on 2022/03/21.
 */
@Dao
interface BadgeDao {

    @Query("SELECT * FROM Badge where id=:id and type=:type")
    fun get(id: String, type: String): BadgeDataModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: BadgeDataModel): Long

    @Delete
    fun delete(data: BadgeDataModel): Int
}
