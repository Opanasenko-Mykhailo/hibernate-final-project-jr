package com.javarush.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityDAO;
import com.javarush.domain.CityEntity;
import com.javarush.domain.CountryLanguageEntity;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;

public class DataTester {
    private final RedisClient redisClient;
    private final SessionFactory sessionFactory;
    private final ObjectMapper mapper;
    private final CityDAO cityDAO;


    public DataTester(RedisClient redisClient, SessionFactory sessionFactory) {
        this.redisClient = redisClient;
        this.sessionFactory = sessionFactory;
        this.mapper = new ObjectMapper();
        this.cityDAO = new CityDAO(sessionFactory);
    }

    public void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            for (Integer id : ids) {
                CityEntity city = cityDAO.getById(id);
                Set<CountryLanguageEntity> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }
}
