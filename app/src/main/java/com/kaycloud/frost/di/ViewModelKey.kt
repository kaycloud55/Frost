package com.kaycloud.frost.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by kaycloud on 2019-07-17
 * @mapKey 对于那些key是枚举或泛化类的map，这个注解用来标注对应的key,也就是被标记的是value,传进来的参数是key-dagger。
 *          这里的key就是ViewModel::class
 *
 * @Target 指定可以用该注解标注的元素的可能的类型（类、函数、属性、表达式等）
 * @MustBeDocumented 指定该注解是公有API的一部分，并且应该包含在生成的API文档中显示的类或方法的签名中
 *
 * kotlin用annotation class来表示注解
 *
 * out可以简单理解为extend
 *
 */

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)