package murach.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class MailUtilBrevo {
    private static final String ENDPOINT = "https://api.brevo.com/v3/smtp/email";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private MailUtilBrevo() {
    }

    public static void sendMail(
            String to,
            String subject,
            String body,
            boolean bodyIsHTML) throws MailException {

        String apiKey = MailConfig.brevoApiKey();
        if (apiKey == null) {
            throw new MailException(
                    "Chua cau hinh BREVO_API_KEY. Dat bien moi truong "
                            + "hoac them vao config.properties.");
        }

        String from = MailConfig.mailFrom();
        if (from == null) {
            throw new MailException(
                    "Chua cau hinh MAIL_FROM (sender da xac minh trong Brevo).");
        }

        String payload = buildPayload(parseSender(from), to, subject, body, bodyIsHTML);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .timeout(Duration.ofSeconds(20))
                .header("api-key", apiKey)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response;
        try {
            response = HTTP_CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
        } catch (IOException exception) {
            throw new MailException("Khong goi duoc Brevo API: " + exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new MailException("Bi ngat khi goi Brevo API.", exception);
        }

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new MailException(
                    "Brevo API tra ve HTTP " + status + ": " + response.body());
        }
    }

    private static String buildPayload(
            Sender sender,
            String to,
            String subject,
            String body,
            boolean bodyIsHTML) {

        String contentField = bodyIsHTML ? "htmlContent" : "textContent";
        return "{\"sender\":{\"name\":\"" + escape(sender.name())
                + "\",\"email\":\"" + escape(sender.email()) + "\"},"
                + "\"to\":[{\"email\":\"" + escape(to) + "\"}],"
                + "\"subject\":\"" + escape(subject) + "\","
                + "\"" + contentField + "\":\"" + escape(body) + "\"}";
    }

    private static Sender parseSender(String from) {
        int open = from.indexOf('<');
        int close = from.lastIndexOf('>');
        if (open >= 0 && close > open) {
            String name = from.substring(0, open).trim();
            String email = from.substring(open + 1, close).trim();
            if (name.isEmpty()) {
                name = email;
            }
            return new Sender(name, email);
        }
        String email = from.trim();
        return new Sender(email, email);
    }

    private static String escape(String value) {
        StringBuilder builder = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                default -> {
                    if (character < 0x20) {
                        builder.append(String.format("\\u%04x", (int) character));
                    } else {
                        builder.append(character);
                    }
                }
            }
        }
        return builder.toString();
    }

    private record Sender(String name, String email) {
    }
}
