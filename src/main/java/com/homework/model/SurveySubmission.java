package com.homework.model;

import java.io.Serializable;

/**
 * Immutable data submitted from the survey form.
 */
public class SurveySubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String dateOfBirth;
    private final String source;
    private final boolean cdOffers;
    private final boolean emailAnnouncements;
    private final String contactMethod;

    public SurveySubmission(
            String firstName,
            String lastName,
            String email,
            String dateOfBirth,
            String source,
            boolean cdOffers,
            boolean emailAnnouncements,
            String contactMethod) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.source = source;
        this.cdOffers = cdOffers;
        this.emailAnnouncements = emailAnnouncements;
        this.contactMethod = contactMethod;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSource() {
        return source;
    }

    public boolean isCdOffers() {
        return cdOffers;
    }

    public boolean isEmailAnnouncements() {
        return emailAnnouncements;
    }

    public String getContactMethod() {
        return contactMethod;
    }
}
