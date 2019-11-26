package com.kaycloud.frost.base.loading

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 使用枚举已经没有那么大的消耗：
 * https://stackoverflow.com/questions/37833395/kotlin-annotation-intdef/37839539#37839539?newreg=4db3ffbf265f4f0d84e3b8fe5f7f7374
 */

enum class LoadStatusType(val value: Int) {
    STATUS_LOADING(1),
    STATUS_LOAD_SUCCESS(2),
    STATUS_LOAD_FAILED(3),
    STATUS_EMPTY_DATA(4)
}