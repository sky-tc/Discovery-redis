package com.nepxion.discovery.console.extension.nacos.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nepxion.discovery.console.extension.nacos.adapter.RedisConfigAdapter;
import com.nepxion.discovery.console.remote.ConfigAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig
{
	
    @Bean(name = "RedisTemplate")
    public RedisTemplate<Object, Object> functionDomainRedisTemplate(JedisConnectionFactory redisConnectionFactory
    		, Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory,jackson2JsonRedisSerializer);
        return redisTemplate;
    }
    
    @Bean
    public Jackson2JsonRedisSerializer<Object> springSessionDefaultRedisSerializer()
    {
    	Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    private void initDomainRedisTemplate(RedisTemplate<Object, Object> redisTemplate, JedisConnectionFactory redisConnectionFactory,
                                         Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer)
    {
    	
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);

    }

    @Bean
    public HashOperations<Object, Object, Object> hashOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }
    

    @Bean
    public ValueOperations<Object, Object> valueOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<Object, Object> listOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<Object, Object> setOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<Object, Object> zSetOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    @Bean
    public ConfigAdapter configAdapter(){
        return new RedisConfigAdapter();
    }
}
