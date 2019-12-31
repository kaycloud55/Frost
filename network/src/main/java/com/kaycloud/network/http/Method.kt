package com.kaycloud.network.http

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 * HTTP请求方法定义，表示对资源的动作
 *
 * GET：获取资源，可以理解为读取或者下载数据；
 * HEAD：获取资源的元信息；
 * POST：向资源提交数据，相当于写入或上传数据；set的语义
 * PUT：类似 POST； update的语义
 * DELETE：删除资源；
 * CONNECT：建立特殊的连接隧道；
 * OPTIONS：列出可对资源实行的方法；
 * TRACE：追踪请求 - 响应的传输路径。
 */
enum class Method {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    OPTIONS,
    TRACE,
    CONNECT
}