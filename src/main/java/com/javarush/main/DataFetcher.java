package com.javarush.main;

import com.javarush.dao.CityDAO;
import com.javarush.domain.CityEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class DataFetcher {
    private final SessionFactory sessionFactory;
    private final CityDAO cityDAO;

    public DataFetcher(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.cityDAO = new CityDAO(sessionFactory);
    }

    public List<CityEntity> fetchCities() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<CityEntity> allCities = new ArrayList<>();
            session.beginTransaction();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }
}
