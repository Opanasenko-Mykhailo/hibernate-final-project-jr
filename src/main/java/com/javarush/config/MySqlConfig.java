package com.javarush.config;

import com.javarush.domain.CityEntity;
import com.javarush.domain.CountryEntity;
import com.javarush.domain.CountryLanguageEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import com.p6spy.engine.spy.P6SpyDriver;

import java.util.Properties;

public class MySqlConfig {
    private static volatile MySqlConfig instance;
    private static SessionFactory sessionFactory;

    private MySqlConfig() {
    }

    public static MySqlConfig getInstance() {
        if (instance == null) {
            synchronized (MySqlConfig.class) {
                if (instance == null) {
                    instance = new MySqlConfig();
                }
            }
        }
        return instance;
    }

    public synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = prepareRelationalDb();
        }
        return sessionFactory;
    }

    private SessionFactory prepareRelationalDb() {
        final SessionFactory sessionFactory;
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/world");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        properties.put(Environment.STATEMENT_BATCH_SIZE, "100");

        sessionFactory = new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(CityEntity.class)
                .addAnnotatedClass(CountryEntity.class)
                .addAnnotatedClass(CountryLanguageEntity.class)
                .buildSessionFactory();
        return sessionFactory;
    }
}
