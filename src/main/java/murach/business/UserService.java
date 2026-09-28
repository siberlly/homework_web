package murach.business;

import jakarta.mail.MessagingException;
import murach.data.UserRepository;
import murach.util.MailUtilGmail;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String firstName, String lastName, String email) {
        String normalizedFirstName = requireValue(firstName, "First name");
        String normalizedLastName = requireValue(lastName, "Last name");
        String normalizedEmail = requireValue(email, "Email");

        User user = new User(
                normalizedFirstName,
                normalizedLastName,
                normalizedEmail
        );
        return userRepository.save(user);
    }

    public void sendWelcomeEmail(User user) throws MessagingException {
        String from = System.getenv("SMTP_USERNAME");
        String subject = "Welcome to our email list";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Thanks for joining our email list.\n"
                + "We'll send you announcements about new products "
                + "and promotions.\n\n"
                + "Have a great day!";

        MailUtilGmail.sendMail(
                user.getEmail(),
                from,
                subject,
                body,
                false
        );
    }

    private String requireValue(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }
}
