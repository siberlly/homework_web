package murach.data;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaUtil {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            createEntityManagerFactory();

    private JpaUtil() {
    }

    public static EntityManager createEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return Persistence.createEntityManagerFactory("emailPU");
        }

        DatabaseConnection connection = parsePostgresUrl(databaseUrl);
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("jakarta.persistence.jdbc.url", connection.jdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", connection.username());
        properties.put("jakarta.persistence.jdbc.password", connection.password());

        return Persistence.createEntityManagerFactory("emailPU", properties);
    }

    private static DatabaseConnection parsePostgresUrl(String databaseUrl) {
        URI uri = URI.create(databaseUrl);
        String userInfo = uri.getUserInfo();
        if (userInfo == null || !userInfo.contains(":")) {
            throw new IllegalStateException("DATABASE_URL does not contain credentials.");
        }

        String[] credentials = userInfo.split(":", 2);
        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String jdbcUrl = "jdbc:postgresql://"
                + uri.getHost()
                + ":"
                + port
                + uri.getPath();

        if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
            jdbcUrl += "?" + uri.getQuery();
        }

        return new DatabaseConnection(jdbcUrl, credentials[0], credentials[1]);
    }

    private record DatabaseConnection(
            String jdbcUrl,
            String username,
            String password) {
    }
}
