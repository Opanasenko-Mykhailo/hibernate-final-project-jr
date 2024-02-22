package com.javarush.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConfig {
    private static volatile RedisConfig instance;
    private static RedisClient redisClient;

    private RedisConfig() {
    }

    public static RedisConfig getInstance() {
        if (instance == null) {
            synchronized (RedisConfig.class) {
                if (instance == null) {
                    instance = new RedisConfig();
                }
            }
        }
        return instance;
    }

    public synchronized RedisClient getRedisClient() {
        if (redisClient == null) {
            redisClient = prepareRedisClient();
        }
        return redisClient;
    }

    private RedisClient prepareRedisClient() {
        RedisClient client = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            System.out.println("\nConnected to Redis\n");
        }
        return client;
    }
}
