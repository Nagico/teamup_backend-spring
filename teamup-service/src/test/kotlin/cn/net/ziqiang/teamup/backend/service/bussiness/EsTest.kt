package cn.net.ziqiang.teamup.backend.service.bussiness


import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.service.business.EsBusiness
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation
import co.elastic.clients.elasticsearch._types.aggregations.AverageAggregation
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation
import co.elastic.clients.elasticsearch._types.query_dsl.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.IOException

@Slf4j
class EsTest {
    @Autowired
    private lateinit var esClient: EsBusiness
    
    @Test
    fun createIndexSettingsMappings() {
        esClient.createIndexSettingsMappings(UserDoc::class.java)
    }

    @Test
    fun aliases() {
        logger.info(esClient.getAllAliases().toString())
    }

    @Test
    fun indexes() {
        logger.info(esClient.getAllIndexes().toString())
    }

    @Test
    @Throws(Exception::class)
    fun selectDocIdExists() {
        if (esClient.checkDocId("1", UserDoc::class.java)) {
            logger.info("product exists")
        }
    }

    @Test
    fun queryAll() {
        val queries: List<UserDoc?> = esClient.queryAll(UserDoc::class.java)
        logger.info(queries.toString())
    }

    @Test
    @Throws(IOException::class)
    fun addData() {
        val userDoc = UserDoc(
            id = 22,
            name = "xxxxa",
            age = 22,
            dec = "xxxxxxxx",
            price = 22.1,
            sku = "aaa1"
        )
        val s = esClient.addData<Any>(userDoc, true)
        logger.info(s)
    }

    @Test
    @Throws(IOException::class)
    fun addDatas() {
        val userDoc1 = UserDoc(
            id = 23L,
            name = "hu",
            age = 22,
            dec = "游泳",
            price = 22.1,
            sku = "aaa1"
        )
        val userDoc2 = UserDoc(
            id = 24L,
            name = "an",
            age = 22,
            dec = "",
            price = 22.2,
            sku = "vvvvvvv"
        )
        val list = mutableListOf(userDoc1, userDoc2)
        esClient.addDataList(list, true)
    }

    @get:Test
    val docId: Unit
        get() {
            val docId: UserDoc = esClient.getByDocId("24", UserDoc::class.java)
            logger.info(docId.toString())
        }

    @Test
    fun complexQuery_MatchAll() {
        val query = Query.of { q: Query.Builder ->
            q.matchAll { m: MatchAllQuery.Builder? -> m }
        }
        val userDocs = esClient.complexQuery(query, UserDoc::class.java)
        logger.info(userDocs.toString())
    }

    @Test
    fun complexQuery_MatchAll_Alias() {
        val query = Query.of { q: Query.Builder ->
            q.matchAll { m: MatchAllQuery.Builder? -> m }
        }
        val userDocs = esClient.complexQuery(query, UserDoc::class.java)
        logger.info(userDocs.toString())
    }

    @Test
    fun complexQuery_MatchQuery() {
        val query = Query.of { q: Query.Builder ->
            q.match { m: MatchQuery.Builder ->
                m.field(
                    "name"
                ).query("xxxxa")
            }
        }
        val userDocs: List<UserDoc?> = esClient.complexQuery(query, UserDoc::class.java)
        logger.info(userDocs.toString())
    }

    @Test
    fun complexQuery_query_bool_must() {
        val age = Query.of { q: Query.Builder ->
            q.match { m: MatchQuery.Builder ->
                m.field(
                    "age"
                ).query(22)
            }
        }
        val price = Query.of { q: Query.Builder ->
            q.match { m: MatchQuery.Builder ->
                m.field(
                    "price"
                ).query(22.1)
            }
        }
        val bool = Query.of { q: Query.Builder ->
            q.bool { b: BoolQuery.Builder ->
                b.must(
                    age
                ).must(price)
            }
        }
        val userDocs: List<UserDoc?> = esClient.complexQuery(bool, UserDoc::class.java)
        logger.info(userDocs.toString())
    }

    @Test
    fun complexQueryHighlight() {
        val dec = Query.of { q: Query.Builder ->
            q.matchPhrase { m: MatchPhraseQuery.Builder ->
                m.field(
                    "dec"
                ).query("匹配")
            }
        }
        val maps = esClient.complexQueryHighlight(
            dec,
            UserDoc::class.java, "dec"
        )
        logger.info(maps.toString())
    }

    @Test
    fun complexQuery_query_complexQueryAggregations() {
        val query = Query.of { q: Query.Builder ->
            q.matchAll { m: MatchAllQuery.Builder? -> m }
        }
        val age = esClient.complexQueryAggregations(
            query,
            { a: Aggregation.Builder? ->
                a!!.terms { t: TermsAggregation.Builder ->
                    t.field(
                        "age"
                    )
                }
            },
            UserDoc::class.java
        )
        for (longTermsBucket in age!!.lterms().buckets().array()) {
            logger.info("key:" + longTermsBucket.key() + ":共多少:" + longTermsBucket.docCount())
        }
        val name = esClient.complexQueryAggregations(
            query,
            { a: Aggregation.Builder? ->
                a!!.terms { t: TermsAggregation.Builder ->
                    t.field(
                        "name"
                    )
                }
            },
            UserDoc::class.java
        )
        for (stringTermsBucket in name!!.sterms().buckets().array()) {
            logger.info("key:" + stringTermsBucket.key() + ":共多少:" + stringTermsBucket.docCount())
        }
        val price = esClient.complexQueryAggregations(
            query,
            { a: Aggregation.Builder? ->
                a!!.avg { t: AverageAggregation.Builder ->
                    t.field(
                        "price"
                    )
                }
            },
            UserDoc::class.java
        )
        logger.info(price!!.avg().value().toString())
    }

    @Test
    fun delDocId() {
        esClient.deleteByDocId("23", UserDoc::class.java)
    }

    @Test
    fun delQuery() {
        val price = Query.of { q: Query.Builder ->
            q.match { m: MatchQuery.Builder ->
                m.field(
                    "price"
                ).query("0.0")
            }
        }
        esClient.deleteByQuery(price, UserDoc::class.java)
    }

    @Test
    fun upDocId() {
        val userDoc = UserDoc(
            age = 21,
            dec = "嘻嘻嘻嘻嘻嘻擦擦擦",
            price = 28.2,
            sku = "mmmmm"
        )
        esClient.updateByDocId<Any>("241", userDoc, true)
    }

    @Test
    fun upQuery() {
        val userDoc = UserDoc(
            dec = "123123123",
        )
        val name = Query.of { q: Query.Builder ->
            q.matchPhrase { m: MatchPhraseQuery.Builder ->
                m.field(
                    "age"
                ).query("33")
            }
        }
        esClient.updateByQuery<Any>(name, userDoc, true)
    }
}

