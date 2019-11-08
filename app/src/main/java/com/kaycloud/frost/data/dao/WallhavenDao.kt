package com.kaycloud.frost.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kaycloud.frost.DatabaseConstant
import com.kaycloud.frost.data.entity.WallhavenItemEntity

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 */

@Dao
interface WallhavenDao {

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN}")
    fun getAll(): LiveData<List<WallhavenItemEntity>>

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN} WHERE id IN (:ids)")
    fun loadAllByPages(ids: List<String>): LiveData<List<WallhavenItemEntity>>

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_WALLHAVEN} WHERE id = :id")
    fun getWallhavenItem(id: String): LiveData<WallhavenItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<WallhavenItemEntity>)

    @Delete
    fun delete(data: WallhavenItemEntity)

    @Insert
    fun insert(itemEntity: WallhavenItemEntity)
}