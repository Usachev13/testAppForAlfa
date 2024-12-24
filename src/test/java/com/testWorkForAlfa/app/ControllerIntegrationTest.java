package com.testWorkForAlfa.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {
    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);
    @Container
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeAll
    static void setup() {
        String host = redis.getHost();
        int port = redis.getMappedPort(6379);

        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }
    @Test
    void shouldCacheAndSaveToMongoDB() throws Exception {
        //вызов контроллера
        String param = "test";
        mockMvc.perform(get("/hello").param("name", param))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello " + param));
        mockMvc.perform(get("/hello").param("name", param))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello " + param));


        Object[] methodArgs = new Object[]{ param };
        int arrayHash = java.util.Arrays.hashCode(methodArgs);
        String cashKey = "hello" + arrayHash;

        //Проверяю Redis
        Object cachedValue = redisTemplate.opsForValue().get(cashKey);
        assertThat(cachedValue).isNotNull();
        //Проверяю MongoDb
        long mongoCount = mongoTemplate.getCollection("requestLogs").countDocuments();
        assertThat(mongoCount).isGreaterThan(0);

    }
}

