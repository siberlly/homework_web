package com.homework.controller;

import java.io.IOException;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.data.JpaUtil;

public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        EntityManager entityManager = null;
        try {
            entityManager = JpaUtil.createEntityManager();
            entityManager.createNativeQuery("select 1", Integer.class)
                    .getSingleResult();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print("OK");
        } catch (RuntimeException exception) {
            log("Database health check failed", exception);
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().print("Database unavailable");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
