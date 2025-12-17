package it.unisa.uniclass.utenti.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;


// Mappatura della servlet
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/Home");
        } catch (IOException e) {
            request.getServletContext().log("Error processing logout request", e);
            try {
                response.sendRedirect(request.getContextPath() + "/Home");
            } catch (IOException ioException) {
                request.getServletContext().log("Failed to redirect after logout error", ioException);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }
}

