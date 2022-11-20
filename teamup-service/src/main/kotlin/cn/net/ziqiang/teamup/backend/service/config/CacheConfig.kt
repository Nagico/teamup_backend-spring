//package cn.net.ziqiang.teamup.backend.service.config
//
//import org.springframework.cache.CacheManager
//import org.springframework.cache.annotation.CachingConfigurerSupport
//import org.springframework.cache.interceptor.*
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.cache.RedisCacheConfiguration
//import org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig
//import org.springframework.data.redis.cache.RedisCacheManager
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.RedisSerializationContext
//import org.springframework.data.redis.serializer.StringRedisSerializer
//import javax.annotation.Resource
//
//
//@Configuration
//class CacheConfig : CachingConfigurerSupport() {
//    @Resource
//    private val factory: RedisConnectionFactory? = null
//
//    /**
//     * 自定义生成redis-key
//     *
//     * @return
//     */
//    @Bean
//    override fun keyGenerator(): KeyGenerator {
//        return KeyGenerator { o, method, objects ->
//            val sb = StringBuilder()
//            sb.append(o::class.simpleName).append(".")
//            sb.append(method.name).append(".")
//            for (obj in objects) {
//                sb.append(obj.toString())
//            }
//            sb.toString()
//        }
//    }
//
//    @Bean
//    fun redisTemplate(): RedisTemplate<String, Any> {
//        val redisTemplate = RedisTemplate<String, Any>()
//        redisTemplate.setConnectionFactory(factory!!)
//        val genericJackson2JsonRedisSerializer = GenericJackson2JsonRedisSerializer()
//        redisTemplate.keySerializer = genericJackson2JsonRedisSerializer
//        redisTemplate.valueSerializer = genericJackson2JsonRedisSerializer
//        redisTemplate.hashKeySerializer = StringRedisSerializer()
//        redisTemplate.hashValueSerializer = genericJackson2JsonRedisSerializer
//        return redisTemplate
//    }
//
//    @Bean
//    override fun cacheResolver(): CacheResolver {
//        return SimpleCacheResolver(cacheManager()!!)
//    }
//
//    @Bean
//    override fun errorHandler(): CacheErrorHandler {
//        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
//        return SimpleCacheErrorHandler()
//    }
//
//    @Bean
//    override fun cacheManager(): CacheManager? {
//        val cacheConfiguration: RedisCacheConfiguration = defaultCacheConfig()
//            .disableCachingNullValues()
//            .serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    GenericJackson2JsonRedisSerializer()
//                )
//            )
//        return RedisCacheManager.builder(factory!!).cacheDefaults(cacheConfiguration).build()
//    }
//}