package com.kaycloud.frost.module.image.gank.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.network.Resource

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
class GankViewModel internal constructor(application: Application) : AndroidViewModel(application) {

    private val gankRepository =
        GankRepository.getInstance(AppDataBase.getInstance(application).gankDao())

    private val _page: MutableLiveData<Int> = MutableLiveData()

    val page: LiveData<Int>
        get() = _page

    /**
     * 这里观察了`_page`的值，发生变化就去调用getGankData
     */
    private var welfare: LiveData<Resource<List<GankItemEntity>>> =
        Transformations.switchMap(_page) { input -> gankRepository.getGankData(input) }

    fun setPage(newValue: Int) {
        if (this._page.value == newValue) {
            return
        }
        _page.value = newValue
    }

    fun getWelfare() = welfare

    fun nextPage() {
        if (welfare.value == null) {
            return
        }
        if (welfare.hasObservers()) {
            _page.value = _page.value?.plus(1)
        }
    }
}