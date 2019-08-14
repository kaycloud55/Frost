package com.kaycloud.frost.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.data.GankItem
import com.kaycloud.frost.data.GankRepository
import com.kaycloud.frost.vo.Resource
import java.util.logging.Logger

/**
 * Created by kaycloud on 2019-07-17
 * 可见性修饰符 internal 意味着该成员只在相同模块内可见。更具体地说， 一个模块是编译在一起的一套 Kotlin 文件：
 * 一个 IntelliJ IDEA 模块；
 * 一个 Maven 项目；
 * 一个 Gradle 源集（例外是 test 源集可以访问 main 的 internal 声明）；
 * 一次 <kotlinc> Ant 任务执行所编译的一套文件。
 *
 * 也就是说，如果定义了这个权限控制，即时当前module作为另一个module的依赖，这个类在宿主module中也是不可用的
 */
class GankViewModel internal constructor(context: Context) : ViewModel() {

    private val gankRepository = GankRepository.getInstance(AppDataBase.getInstance(context).gankDao())

    private var welfare: LiveData<Resource<List<GankItem>>> = MutableLiveData()

    fun getWelfare(): LiveData<Resource<List<GankItem>>> {
        return welfare
    }

    fun loadWelfare(page: Int) {
        welfare = gankRepository.getGankData(page)
    }


}