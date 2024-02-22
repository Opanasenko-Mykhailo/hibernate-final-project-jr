package com.javarush.dao;

import com.javarush.domain.CountryEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CountryDAO {private final SessionFactory sessionFactory;

    public CountryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<CountryEntity> getAll() {
        Query<CountryEntity> query = sessionFactory.getCurrentSession().createQuery("select c from CountryEntity c", CountryEntity.class);
        return query.list();
    }
}