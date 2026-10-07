package murach.util;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

public final class MailUtilResend {
    private static final String DEFAULT_FROM =
            "Email Servlet <onboarding@resend.dev>";

    private MailUtilResend() {
    }

    public static void sendMail(
            String to,
            String subject,
            String body,
            boolean bodyIsHTML) throws ResendException {

        String apiKey = MailConfig.resendApiKey();
        if (apiKey == null) {
            throw new ResendException(
                    "Chua cau hinh RESEND_API_KEY. Dat bien moi truong "
                            + "hoac them vao config.properties.");
        }

        String from = MailConfig.resendFrom();
        if (from == null) {
            from = DEFAULT_FROM;
        }

        CreateEmailOptions.Builder emailBuilder = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject);

        if (bodyIsHTML) {
            emailBuilder.html(body);
        } else {
            emailBuilder.text(body);
        }

        Resend resend = new Resend(apiKey);
        resend.emails().send(emailBuilder.build());
    }
}
