package cn.net.ziqiang.teamup.backend.service.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import javax.annotation.Resource
import org.springframework.cache.CacheManager
import org.springframework.cache.interceptor.*
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.serializer.RedisSerializationContext


@Configuration
class RedisConfig : CachingConfigurerSupport() {
    @Resource
    private lateinit var factory: RedisConnectionFactory

    companion object {
        val objectMapper: ObjectMapper
            get() {
                return ObjectMapper().apply {
                    registerModule(KotlinModule.Builder().build())

                    setVisibility(
                        /* forMethod = */ PropertyAccessor.ALL,
                        /* visibility = */ JsonAutoDetect.Visibility.ANY
                    )

                    configure(
                        /* f = */ DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        /* state = */ false
                    )

                    activateDefaultTyping(
                        /* ptv = */ LaissezFaireSubTypeValidator.instance,
                        /* applicability = */ ObjectMapper.DefaultTyping.EVERYTHING,
                    )
                }
            }
    }

    /**
     * 自定义生成redis-key
     *
     * @return
     */
    @Bean
    override fun keyGenerator(): KeyGenerator {
        return KeyGenerator { o, method, objects ->
            val sb = StringBuilder()
            sb.append(o::class.simpleName).append(".")
            sb.append(method.name).append(".")
            for (obj in objects) {
                sb.append(obj.toString())
            }
            sb.toString()
        }
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {


        val redisTemplate = RedisTemplate<String, Any>().apply {
            setConnectionFactory(factory)
            val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
            val stringSerializer = StringRedisSerializer()

            keySerializer = stringSerializer
            hashKeySerializer = stringSerializer
            valueSerializer = jsonSerializer
            hashValueSerializer = jsonSerializer
        }
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    @Bean
    override fun cacheResolver(): CacheResolver {
        return SimpleCacheResolver(cacheManager()!!)
    }

    @Bean
    override fun errorHandler(): CacheErrorHandler {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return SimpleCacheErrorHandler()
    }

    @Bean
    override fun cacheManager(): CacheManager? {
        val cacheConfiguration: RedisCacheConfiguration = defaultCacheConfig()
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build()
    }
}