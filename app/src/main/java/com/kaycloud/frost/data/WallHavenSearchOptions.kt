package com.kaycloud.frost.data

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */
data class WallHavenSearchOptions(
    val sorting: DisplayType,
    val searchText: String? = null,
    val categories: String? = null
) {


    fun toQueryMap(): Map<String, String> {
        val map = mapOf(
            "q" to searchText,
            "sorting" to sorting.value,
            "categories" to categories
        )

        return map.filterValues { it != null } as Map<String, String>
    }
}

enum class DisplayType(val value: String) {
    TOPLIST("toplist"),
    RANDOM("random"),
    LATEST("data_added")
}