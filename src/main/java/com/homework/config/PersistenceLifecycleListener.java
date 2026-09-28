package com.homework.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import murach.data.JpaUtil;

public class PersistenceLifecycleListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        JpaUtil.getEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JpaUtil.close();
    }
}
