package com.kaycloud.frost.module.toplist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kaycloud.frost.DatabaseConstant
import com.kaycloud.frost.module.toplist.ui.toplist.TopListType
import com.kaycloud.frost.module.toplist.ui.toplist.TopListItem

/**
 * Created by jiangyunkai on 2020/4/19
 */
@Dao
interface TopListDao {

    @Query("SELECT * FROM ${DatabaseConstant.TABLE_TOP_LIST}")
    fun loadAllTypes(): LiveData<List<TopListType>>

    @Query("SELECT * FROM toplistitem WHERE typeId = :id  ORDER BY CreateTime limit :size offset (:page * :size)")
    fun loadItemsByType(id: String, size: Int, page: Int): LiveData<List<TopListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTypes(data: List<TopListType>)

    @Delete
    fun deleteItem(data: TopListItem)

    @Delete
    fun deleteCategory(data: TopListType)

    @Insert
    fun insertAllItems(data: List<TopListItem>)
}