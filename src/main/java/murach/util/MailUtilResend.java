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

        String apiKey = System.getenv("RESEND_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResendException("Chua cau hinh bien RESEND_API_KEY.");
        }

        String from = System.getenv("RESEND_FROM");
        if (from == null || from.isBlank()) {
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
