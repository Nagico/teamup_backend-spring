package cn.net.ziqiang.teamup.backend.service.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig {

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

    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, Any> {


        val redisTemplate = RedisTemplate<String, Any>().apply {
            setConnectionFactory(redisConnectionFactory)
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
}