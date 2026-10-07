package murach.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class MailConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties FILE_PROPERTIES = loadFileProperties();

    private MailConfig() {
    }

    public static String resendApiKey() {
        return resolve("RESEND_API_KEY");
    }

    public static String resendFrom() {
        return resolve("RESEND_FROM");
    }

    private static String resolve(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key);
        }
        if (isBlank(value)) {
            value = FILE_PROPERTIES.getProperty(key);
        }
        return isBlank(value) ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static Properties loadFileProperties() {
        Properties properties = new Properties();
        Path path = Path.of(CONFIG_FILE);
        if (!Files.isRegularFile(path)) {
            return properties;
        }
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException exception) {
            System.err.println("Unable to read " + CONFIG_FILE + ": " + exception.getMessage());
        }
        return properties;
    }
}
