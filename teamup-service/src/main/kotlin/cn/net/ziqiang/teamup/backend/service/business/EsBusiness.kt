package cn.net.ziqiang.teamup.backend.service.business

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.util.ObjectBuilder
import java.util.function.Function

interface EsBusiness {

    /**
     * 查询全部索引
     *
     * @return
     */
    fun getAllIndexes(): List<String?>

    /**
     * 查询全部别名
     *
     * @return
     */
    fun getAllAliases(): List<String?>

    /**
     * 创建索引与别名
     * 如果索引或者别名已经存在,那么就不会在创建了
     * @return 是否成功
     */
    fun <T> createIndexSettingsMappings(tClass: Class<T>): Boolean

    /**
     * 查询全部数据
     *
     * @param T 数据类型
     * @param tClass 类型
     * @return
     */
    fun <T> queryAll(tClass: Class<T>): List<T?>

    /**
     * @param o 数据源
     * @param async 是否异步 true异步 ,false同步
     * @return 如果是异步那么永远返回null
     */
    fun <T> addData(o: T, async: Boolean): String?

    /**
     * 查询指定文档id数据是否存在
     *
     * @param T 类型
     * @param tClass 类型
     * @param id 文档id
     * @return
     */
    fun <T> checkDocId(id: String?, tClass: Class<T>): Boolean

    /**
     * 批量添加
     *
     * @param T 类型
     * @param list 数据源
     * @param async 是否异步
     */
    fun <T> addDataList(list: List<T>, async: Boolean)

    /**
     * 根据DocID获取对象
     *
     * @param T 类型
     * @param DocId 文档ID
     * @param clazz 类型
     * @return
     */
    fun <T> getByDocId(DocId: String?, clazz: Class<T>): T

    /**
     * 根据doc id更新对象
     *
     * @param T 类型
     * @param docId doc id
     * @param o 更新内容，只更新非空字段
     * @param async 是否异步
     */
    fun <T> updateByDocId(docId: String, o: T, async: Boolean)

    /**
     * 根据doc id删除对象
     *
     * @param T 类型
     * @param docId doc id
     * @param clazz 类型
     */
    fun <T> deleteByDocId(docId: String?, clazz: Class<T>)

    /**
     * 根据 查询条件 更新对象
     *
     * @param T 类型
     * @param query 查询条件
     * @param o  更新内容，只更新非空字段
     * @param async 是否异步
     */
    fun <T> updateByQuery(query: Query?, o: T, async: Boolean)

    /**
     * 根据 查询条件 删除对象
     *
     * @param T 类型
     * @param query 查询条件
     * @param clazz 类型
     */
    fun <T> deleteByQuery(query: Query?, clazz: Class<T>)

    /**
     * 复杂查询
     *
     * @param T
     * @param query 查询条件
     * @param clazz 类型
     * @return
     */
    fun <T> complexQuery(query: Query?, clazz: Class<T>): List<T?>

    /**
     * 聚合查询
     * 不需要显示数据 ,只要聚合结果
     *
     * @param T 类型
     * @param query 查询条件
     * @param fn 聚合函数
     * @param clazz 类型
     * @return
     */
    fun <T> complexQueryAggregations(
        query: Query?, fn: Function<Aggregation.Builder?, ObjectBuilder<Aggregation?>?>?, clazz: Class<T>,
    ): Aggregate?

    /**
     * 高亮查询
     *
     * @param T 类型
     * @param query 查询条件
     * @param clazz 类型
     * @param fields 高亮字段
     * @return
     */
    fun <T> complexQueryHighlight(query: Query?, clazz: Class<T>, vararg fields: String?): List<Map<String, Any?>>
}