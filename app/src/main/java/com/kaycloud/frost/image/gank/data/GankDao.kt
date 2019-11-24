package com.kaycloud.frost.image.gank.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by kaycloud on 2019-07-16
 */
@Dao
interface GankDao {

    @Query("SELECT * FROM gank_data")
    fun getAll(): LiveData<List<GankItemEntity>>

    @Query("SELECT * FROM gank_data LIMIT :limit OFFSET :offset")
    fun loadAllByPages(limit: Int, offset: Int): LiveData<List<GankItemEntity>>

    @Query("SELECT * FROM gank_data WHERE id = :id")
    fun getGankItem(id: String): LiveData<GankItemEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(data: List<GankItemEntity>)

    @Delete
    fun delete(data: GankItemEntity)

    @Insert
    fun insert(itemEntity: GankItemEntity)
}