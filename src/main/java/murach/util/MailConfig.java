package murach.util;

public final class MailConfig {
    private MailConfig() {
    }

    public static String brevoApiKey() {
        return resolve("BREVO_API_KEY");
    }

    public static String mailFrom() {
        return resolve("MAIL_FROM");
    }

    private static String resolve(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key);
        }
        return isBlank(value) ? null : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
