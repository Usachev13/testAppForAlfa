package com.testWorkForAlfa.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class CacheTest {
    @Autowired
    private HelloService helloService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    void testCache() {
        String name = "test";
        String key = "hello" + Arrays.hashCode(new Object[] { name });

        // Первый вызов должен кэшировать данные
        helloService.hello(name);
        Object cachedValue = redisTemplate.opsForValue().get(key);

        assertThat(cachedValue).isNotNull();
    }
}
