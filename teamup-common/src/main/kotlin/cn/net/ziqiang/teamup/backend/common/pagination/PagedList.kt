package cn.net.ziqiang.teamup.backend.common.pagination

import org.springframework.data.domain.Page
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 * 分页列表
 *
 * @param M 数据库实体
 * @param T VO
 * @property count 总数
 * @property previous 上一页
 * @property next 下一页
 * @property results 结果列表
 */
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

    constructor(pageData: Page<M>, func: (M) -> T = { m -> m as T }) : this() {
        count = pageData.totalElements
        results = pageData.content.map(func)
        val page = pageData.number
        val pageSize = pageData.size

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
