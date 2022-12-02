package cn.net.ziqiang.teamup.backend.service.business.impl

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ClassUtil
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.annotation.Business
import cn.net.ziqiang.teamup.backend.common.annotation.DocId
import cn.net.ziqiang.teamup.backend.common.annotation.EsClass
import cn.net.ziqiang.teamup.backend.common.annotation.EsField
import cn.net.ziqiang.teamup.backend.service.business.EsBusiness
import cn.net.ziqiang.teamup.backend.common.constant.type.EsDataType
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.InlineScript
import co.elastic.clients.elasticsearch._types.Script
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.elasticsearch.core.*
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation
import co.elastic.clients.elasticsearch.core.search.Highlight
import co.elastic.clients.elasticsearch.core.search.HighlightField
import co.elastic.clients.elasticsearch.core.search.Hit
import co.elastic.clients.elasticsearch.indices.Alias
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import co.elastic.clients.util.ObjectBuilder
import com.alibaba.fastjson.JSONObject
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.Assert
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Field
import java.util.*
import java.util.function.Function

@Slf4j
@Business
class EsBusinessImpl : EsBusiness, InitializingBean {
    @Value("\${es.port}")
    private val port = 0

    @Value("\${es.hostname}")
    private val hostname: String? = null

    @Value("\${es.alias}")
    private val alias = false

    private lateinit var client: ElasticsearchClient  // 同步客户端

    private lateinit var asyncClient: ElasticsearchAsyncClient  // 异步客户端

    private val indexPrefix = "index-"
    private val aliasPrefix = "alias-"

    override fun afterPropertiesSet() {
        val restClient = RestClient.builder(HttpHost(hostname, port)).build()
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())
        // es 客户端
        client = ElasticsearchClient(transport)
        asyncClient = ElasticsearchAsyncClient(transport)
    }

    override fun getAllIndexes(): List<String?> {
        val indices = client.cat().indices().valueBody() ?: throw ApiException(ResultType.ThirdServiceError, "ES异常")
        return indices.map { it.index() }
    }

    override fun getAllAliases(): List<String?> {
        val aliasesRecords = client.cat().aliases().valueBody() ?: throw ApiException(ResultType.ThirdServiceError, "ES异常")
        return aliasesRecords.map { it.alias() }
    }

    override fun <T> createIndexSettingsMappings(tClass: Class<T>): Boolean {
        val annotation = tClass.getAnnotation(EsClass::class.java)
        val index = getClassIndex(tClass)
        val alias = getClassAlias(tClass)
        val indexes = getAllIndexes()
        val aliases = getAllAliases()
        if (indexes.contains(index) || aliases.contains(alias)) {
            return false
        }
        val shards = annotation.shards
        val replicas = annotation.replicas
        val stringBuilder = StringBuilder("{")
        stringBuilder.append(
            "\"settings\": {\n"
                    + "    \"number_of_shards\": "
                    + shards
                    + ",\n"
                    + "    \"number_of_replicas\": "
                    + replicas
                    + "\n"
                    + "  },"
        )
        stringBuilder.append("\"mappings\": {\n" + "    \"properties\": ")
        val jsonObject = JSONObject()
        for (declaredField: Field in tClass.declaredFields) {
            declaredField.isAccessible = true
            val jsonObject1 = JSONObject()
            val docId = declaredField.getAnnotation(DocId::class.java)
            if (docId != null) {
                jsonObject1["type"] = EsDataType.LONG.type
                jsonObject[declaredField.name] = jsonObject1
                continue
            }
            val annotation1 = declaredField.getAnnotation(EsField::class.java)
            if (annotation1 != null) {
                val name = if ((annotation1.name.isBlank())) declaredField.name else annotation1.name
                val type = annotation1.type
                val analyzer = annotation1.analyzer
                val searchAnalyzer = annotation1.searchAnalyzer
                jsonObject1["type"] = type.type
                if ("" != analyzer) {
                    jsonObject1["analyzer"] = analyzer
                }
                if ("" != searchAnalyzer) {
                    jsonObject1["search_analyzer"] = searchAnalyzer
                }
                jsonObject[name] = jsonObject1
            }
        }
        Assert.isTrue(jsonObject.size > 0, "请添加es相关注解")
        stringBuilder.append(jsonObject)
        stringBuilder.append("}}")
        val queryJson: Reader = StringReader(stringBuilder.toString())
        val req = CreateIndexRequest.of { b: CreateIndexRequest.Builder ->
            b.index(index)
                .aliases(
                    alias,
                    Alias.of { a: Alias.Builder ->
                        a.isWriteIndex(
                            true
                        )
                    }
                )
                .withJson(queryJson)
        }
        return client.indices().create(req).acknowledged()
    }

    override fun <T> queryAll(tClass: Class<T>): List<T?> {
        val list: MutableList<T?> = ArrayList()
        val index = getClassAliasOrIndex(tClass)
        val search = client.search({ s: SearchRequest.Builder ->
            s.index(index).query { q: Query.Builder ->
                q.matchAll { m: MatchAllQuery.Builder? -> m }
            }
        }, tClass)
        for (hit: Hit<T> in search.hits().hits()) {
            list.add(hit.source())
        }
        return list
    }

    override fun <T> addData(o: T, async: Boolean): String? {
        var id: Any? = null
        for (declaredField: Field in o!!.javaClass.declaredFields) {
            declaredField.isAccessible = true
            val annotation = declaredField.getAnnotation(DocId::class.java)
            if (annotation != null) {
                try {
                    id = declaredField[o]
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
        var response: IndexResponse? = null
        try {
            val indexReqBuilder = IndexRequest.Builder<T>()
            indexReqBuilder.index(getClassAlias(o.javaClass))
            indexReqBuilder.id(id.toString())
            indexReqBuilder.document(o)
            if (async) {
                asyncClient.index(indexReqBuilder.build())
                return null
            }
            response = client.index(indexReqBuilder.build())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        assert(response != null)
        return response!!.id()
    }

    override fun <T> checkDocId(id: String?, tClass: Class<T>): Boolean {
        return client.exists { s: ExistsRequest.Builder ->
            s.index(
                getClassAlias(tClass)
            ).id(id)
        }.value()
    }

    override fun <T> addDataList(list: List<T>, async: Boolean) {
        val br = BulkRequest.Builder()
        for (o: T in list) {
            var id: Any? = null
            for (declaredField: Field in o!!.javaClass.declaredFields) {
                declaredField.isAccessible = true
                val annotation = declaredField.getAnnotation(DocId::class.java)
                if (annotation != null) {
                    try {
                        id = declaredField[o]
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            if (id == null) {
                id = UUID.randomUUID().mostSignificantBits
            }
            val finalId: Any = id
            br.operations { op: BulkOperation.Builder ->
                op.index { idx: IndexOperation.Builder<Any?> ->
                    idx.index(
                        getClassAlias(o.javaClass)
                    ).id(finalId.toString()).document(o)
                }
            }
        }
        if (async) {
            asyncClient.bulk(br.build())
            return
        }
        val result = client.bulk(br.build()) ?: throw ApiException(ResultType.ThirdServiceError, "ES异常")
        if (result.errors()) {
            logger.error("Bulk had errors")
            for (item: BulkResponseItem in result.items()) {
                if (item.error() != null) {
                    logger.error(item.error()!!.reason())
                }
            }
        }
    }

    override fun <T> getByDocId(DocId: String?, clazz: Class<T>): T {
        val response = client.get({ g: GetRequest.Builder ->
            g.index(
                getClassAlias(clazz)
            ).id(DocId)
        }, clazz)
        return response!!.source()!!
    }

    override fun <T> updateByDocId(docId: String, o: T, async: Boolean) {
        if (o == null) throw NullPointerException("doc is null")
        //先查询出来
        val docId1: Any = getByDocId(docId, o.javaClass)
        //进行对象拷贝
        BeanUtil.copyProperties(o, docId1)
        val index = getClassAliasOrIndex(o.javaClass)
        if (async) {
            asyncClient.update(UpdateRequest.of{u: UpdateRequest.Builder<Any, Any> ->
                u.index(index).id(docId).doc(docId1)
            }, o.javaClass)
            return
        }
        client.update(UpdateRequest.of { d: UpdateRequest.Builder<Any, Any> ->
            d.index(
                index
            ).id(docId).doc(o)
        }, o.javaClass)
    }

    override fun <T> deleteByDocId(docId: String?, clazz: Class<T>) {
        val index = getClassAliasOrIndex(clazz)
        val de = DeleteRequest.of { d: DeleteRequest.Builder ->
            d.index(
                index
            ).id(docId)
        }
        client.delete(de)
    }

    override fun <T> updateByQuery(query: Query?, o: T, async: Boolean) {
        o ?: throw NullPointerException("doc is null")
        val aClass: Class<*> = o.javaClass
        val index = getClassAliasOrIndex(aClass)
        //获取全部字段和字段的名称以及需要修改的值
        val stringBuilder = StringBuilder()
        for (declaredField: Field in aClass.declaredFields) {
            declaredField.isAccessible = true
            val o1 = declaredField.get(o) ?: continue
            declaredField.isAccessible = true
            val field = declaredField.getAnnotation(EsField::class.java)
            var name = field.name
            name = if (("" == name)) declaredField.name else name
            val str = "ctx._source['$name'] = '$o1';"
            stringBuilder.append(str)
        }
        val of = UpdateByQueryRequest.of { u: UpdateByQueryRequest.Builder ->
            u.index(index).query(query).script { s: Script.Builder ->
                s.inline { i: InlineScript.Builder ->
                    i.source(
                        stringBuilder.toString()
                    )
                }
            }
        }
        if (async) {
            asyncClient.updateByQuery(of)
            return
        }
        client.updateByQuery(of)
    }

    override fun <T> deleteByQuery(query: Query?, clazz: Class<T>) {
        val index = getClassAliasOrIndex(clazz)
        val de = DeleteByQueryRequest.of { d: DeleteByQueryRequest.Builder ->
            d.index(
                index
            ).query(query)
        }
        client.deleteByQuery(de)
    }

    override fun <T> complexQuery(query: Query?, clazz: Class<T>): List<T?> {
        val list: MutableList<T?> = ArrayList()
        val response = client.search({ s: SearchRequest.Builder ->
            s.index(
                getClassAlias(clazz)
            ).query(query)
        }, clazz)
        val hits = response.hits().hits()
        for (hit: Hit<T> in hits) {
            list.add(hit.source())
        }
        return list
    }

    override fun <T> complexQueryAggregations(
        query: Query?,
        fn: Function<Aggregation.Builder?, ObjectBuilder<Aggregation?>?>?,
        clazz: Class<T>,
    ): Aggregate? {
        val response = client.search(
            { s: SearchRequest.Builder ->
                s.index(getClassAlias(clazz))
                    .size(0) // 不需要显示数据 ,只想要聚合结果
                    .query(query)
                    .aggregations("aggregations", fn)
            },
            clazz
        )
        return response.aggregations()["aggregations"]
    }

    override fun <T> complexQueryHighlight(
        query: Query?,
        clazz: Class<T>,
        vararg fields: String?,
    ): List<Map<String, Any?>> {
        val list: MutableList<Map<String, Any?>> = ArrayList()
        val index = getClassAliasOrIndex(clazz)
        val of = Highlight.of { h: Highlight.Builder ->
            for (field: String? in fields) {
                h.fields(
                    field
                ) { h1: HighlightField.Builder ->
                    h1.preTags(
                        "<font color='red'>"
                    ).postTags("</font>")
                }
            }
            h
        }
        val response = client.search(
            { s: SearchRequest.Builder ->
                s
                    .index(index)
                    .query(query)
                    .highlight(of)
            }, clazz
        )
        for (hit: Hit<T> in response.hits().hits()) {
            val map: MutableMap<String, Any?> = HashMap()
            map["source"] = hit.source()
            map["highlight"] = hit.highlight()
            list.add(map)
        }
        return list
    }
    // region Utils
    /**
     * 使用索引还是使用别名
     * @param clazz
     * @param <T>
     * @return
    </T> */
    private fun <T> getClassAliasOrIndex(clazz: Class<T>): String {
        return if (alias) {
            getClassAlias(clazz)
        } else getClassIndex(clazz)
    }

    /**
     * 获取类的索引名称(没有指定默认类名首字母小写,  前缀+索引)
     *
     * @param T
     * @param clazz
     * @return
     */
    private fun <T> getClassIndex(clazz: Class<T>): String {
        val annotation = clazz.getAnnotation(EsClass::class.java)
        var index = annotation.index
        index =
            if (("" == index)) Objects.requireNonNull(ClassUtil.getClassName(clazz, false))
                .lowercase(Locale.getDefault()) else index.lowercase(
                Locale.getDefault()
            )
        return indexPrefix + index
    }

    /**
     * 获取类的别名名称(没有指定默认类名首字母小写,  前缀+别名)
     *
     * @param T
     * @param clazz
     * @return
     */
    private fun <T> getClassAlias(clazz: Class<T>): String {
        val annotation = clazz.getAnnotation(EsClass::class.java)
        var alias = annotation.alias
        alias =
            if (("" == alias)) Objects.requireNonNull(ClassUtil.getClassName(clazz, false))
                .lowercase(Locale.getDefault()) else alias.lowercase(
                Locale.getDefault()
            )
        return aliasPrefix + alias
    }
    // endregion
}