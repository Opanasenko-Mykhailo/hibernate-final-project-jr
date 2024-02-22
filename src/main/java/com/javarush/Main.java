package com.javarush;

import com.javarush.config.MySqlConfig;
import com.javarush.config.RedisConfig;
import com.javarush.domain.CityEntity;
import com.javarush.service.DataFetcher;
import com.javarush.service.DataPusher;
import com.javarush.service.DataTester;
import com.javarush.service.DataTransformer;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = MySqlConfig.getInstance().getSessionFactory();
        RedisClient redisClient = RedisConfig.getInstance().getRedisClient();

        DataFetcher dataFetcher = new DataFetcher(sessionFactory);
        DataTransformer dataTransformer = new DataTransformer();
        DataPusher dataPusher = new DataPusher(redisClient);
        DataTester dataTester = new DataTester(redisClient, sessionFactory);

        List<CityEntity> allCities = dataFetcher.fetchCities();
        List<CityCountry> preparedData = dataTransformer.transformData(allCities);
        dataPusher.pushToRedis(preparedData);

        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        dataTester.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        dataTester.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        sessionFactory.getCurrentSession().close();
        redisClient.shutdown();
    }
}
