package com.kaycloud.frost.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kaycloud.frost.data.entity.GankItemEntity

/**
 * Created by kaycloud on 2019-07-16
 */
@Dao
interface GankDao {

    @Query("SELECT * FROM gank_data")
    fun getAll(): LiveData<List<GankItemEntity>>

    @Query("SELECT * FROM gank_data WHERE id IN (:ids)")
    fun loadAllByPages(ids: List<String>): LiveData<List<GankItemEntity>>

    @Query("SELECT * FROM gank_data WHERE id = :id")
    fun getGankItem(id: String): LiveData<GankItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<GankItemEntity>)

    @Delete
    fun delete(data: GankItemEntity)

    @Insert
    fun insert(itemEntity: GankItemEntity)
}