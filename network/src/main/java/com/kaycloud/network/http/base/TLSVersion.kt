package com.kaycloud.network.http.base

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 * TLS协商时可供选择的TLS/SSL版本
 */
enum class TLSVersion(val javaName: String) {
    TLS_1_3("TLSv1.3"), // 2016.
    TLS_1_2("TLSv1.2"), // 2008.
    TLS_1_1("TLSv1.1"), // 2006.
    TLS_1_0("TLSv1"), // 1999.
    SSL_3_0("SSLv3"); // 1996.

    companion object {
        @JvmStatic
        fun forJavaName(javaName: String): TLSVersion {
            return when (javaName) {
                "TLSv1.3" -> TLS_1_3
                "TLSv1.2" -> TLS_1_2
                "TLSv1.1" -> TLS_1_1
                "TLSv1" -> TLS_1_0
                "SSLv3" -> SSL_3_0
                else -> throw IllegalArgumentException("Unexpected TLS version: $javaName")
            }
        }
    }
}