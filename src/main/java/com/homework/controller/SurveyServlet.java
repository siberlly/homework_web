package com.homework.controller;

import com.homework.model.SurveySubmission;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/survey")
public class SurveyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        SurveySubmission submission = new SurveySubmission(
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("email"),
                request.getParameter("dateOfBirth"),
                request.getParameter("source"),
                "yes".equals(request.getParameter("cdOffers")),
                "yes".equals(request.getParameter("emailAnnouncements")),
                request.getParameter("contactMethod")
        );

        HttpSession session = request.getSession();
        session.setAttribute("surveySubmission", submission);

        String thankYouUrl = request.getContextPath() + "/thankyou";
        response.sendRedirect(response.encodeRedirectURL(thankYouUrl));
    }
}
