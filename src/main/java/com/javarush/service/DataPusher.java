package com.javarush.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;

import java.util.List;

public class DataPusher {
    private final RedisClient redisClient;
    private final ObjectMapper mapper;

    public DataPusher(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.mapper = new ObjectMapper();
    }

    public void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
