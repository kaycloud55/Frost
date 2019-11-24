package com.kaycloud.frost.image.wallhaven.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kaycloud.frost.DatabaseConstant

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 */

@Dao
interface WallhavenDao {

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN}")
    fun getAll(): LiveData<List<WallhavenItemEntity>>

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN} LIMIT :limit OFFSET :offset")
    fun loadAllByPages(limit: Int, offset: Int): LiveData<List<WallhavenItemEntity>>

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN} WHERE id = :id")
    fun getWallhavenItem(id: String): LiveData<WallhavenItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<WallhavenItemEntity>)

    @Delete
    fun delete(data: WallhavenItemEntity)

    @Insert
    fun insert(itemEntity: WallhavenItemEntity)
}