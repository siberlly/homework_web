package murach.util;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public final class MailUtilGmail {
    private MailUtilGmail() {
    }

    public static void sendMail(
            String to,
            String from,
            String subject,
            String body,
            boolean bodyIsHTML) throws MessagingException {

        String username = System.getenv("SMTP_USERNAME");
        String password = System.getenv("SMTP_PASSWORD");

        if (username == null || username.isBlank()) {
            throw new MessagingException("Chua cau hinh bien SMTP_USERNAME.");
        }
        if (password == null || password.isBlank()) {
            throw new MessagingException("Chua cau hinh bien SMTP_PASSWORD.");
        }
        if (from == null || from.isBlank()) {
            throw new MessagingException("Dia chi nguoi gui khong hop le.");
        }

        // 1. Tao mail session cho Gmail SMTP qua SSL.
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.port", "465");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(properties);
        session.setDebug(true);

        // 2. Tao noi dung email.
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject, StandardCharsets.UTF_8.name());

        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body, StandardCharsets.UTF_8.name());
        }

        // 3. Dat dia chi gui va nhan.
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4. Dang nhap Gmail va gui email.
        Transport transport = session.getTransport();
        try {
            transport.connect(username, password);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}
