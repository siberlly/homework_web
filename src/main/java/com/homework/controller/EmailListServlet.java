package com.homework.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.User;
import murach.business.UserService;
import murach.data.UserRepository;
import murach.util.MailException;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EmailListServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(new UserRepository());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String url = "/email/index.jsp";
        String action = request.getParameter("action");

        if (action == null) {
            action = "join";
        }

        if (action.equals("add")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String emailAddress = request.getParameter("emailAddress");

            User user;
            try {
                user = userService.register(firstName, lastName, emailAddress);
            } catch (IllegalArgumentException exception) {
                request.setAttribute("message", exception.getMessage());
                request.setAttribute(
                        "user",
                        new User(firstName, lastName, emailAddress)
                );
                getServletContext().getRequestDispatcher(url).forward(request, response);
                return;
            }

            GregorianCalendar currentDate = new GregorianCalendar();
            int currentYear = currentDate.get(Calendar.YEAR);
            request.setAttribute("currentYear", currentYear);
            request.setAttribute("user", user);

            try {
                userService.sendWelcomeEmail(user);
                request.setAttribute(
                        "emailMessage",
                        "Welcome email sent successfully."
                );
            } catch (MailException exception) {
                log("Unable to send email to " + emailAddress, exception);
                request.setAttribute(
                        "emailMessage",
                        "Your information was saved, but the email could not be sent."
                );
            }

            url = "/email/thanks.jsp";
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
