package cn.net.ziqiang.teamup_backend.util.pagenation

import org.springframework.data.domain.Page
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class PagedList<M, T>(
    var count: Long = 0,
    var previous: String? = null,
    var next: String? = null,
    var results: List<T> = listOf()) {

    companion object {
        private fun getBaseUrlString(): String {
            val uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()
            return if (uri.port == -1) {
                uri.scheme + "://" + uri.host + uri.path
            } else {
                uri.scheme + "://" + uri.host + ":" + uri.port + uri.path
            }
        }
    }

    constructor(page: Int, pageSize: Int, pageData: Page<M>, func: (M) -> T) : this() {
        count = pageData.totalElements
        results = pageData.content.map(func)

        val baseUrl = getBaseUrlString()

        previous = if (page > 1)
        {
            "$baseUrl?page=${page - 1}&pageSize=$pageSize"
        } else
        {
            null
        }
        next = if (page * pageSize < count)
        {
            "$baseUrl?page=${page + 1}&pageSize=$pageSize"
        } else
        {
            null
        }
    }

}