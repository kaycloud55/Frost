package com.kaycloud.frost.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by kaycloud on 2019-07-16
 */
@Dao
interface GankDao {

    @Query("SELECT * FROM gank_data")
    fun getAll(): LiveData<List<GankItem>>

    @Query("SELECT * FROM gank_data WHERE id IN (:ids)")
    fun loadAllByPages(ids: List<String>): LiveData<List<GankItem>>

    @Query("SELECT * FROM gank_data WHERE id = :id")
    fun getGankItem(id: String): LiveData<GankItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<GankItem>)

    @Delete
    fun delete(data: GankItem)

    @Insert
    fun insert(item: GankItem)
}