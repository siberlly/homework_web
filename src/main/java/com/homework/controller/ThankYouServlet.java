package com.homework.controller;

import com.homework.model.SurveySubmission;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/thankyou")
public class ThankYouServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object sessionValue = session == null ? null : session.getAttribute("surveySubmission");

        if (!(sessionValue instanceof SurveySubmission submission)) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
            return;
        }

        request.setAttribute("submission", submission);
        request.getRequestDispatcher("/WEB-INF/thankyou.jsp").forward(request, response);
    }
}
