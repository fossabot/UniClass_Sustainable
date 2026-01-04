package it.unisa.uniclass.utenti.controller;

import it.unisa.uniclass.common.security.CredentialSecurity;
import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.model.PersonaleTA;
import it.unisa.uniclass.utenti.model.Utente;
import it.unisa.uniclass.utenti.service.AccademicoService;
import it.unisa.uniclass.utenti.service.PersonaleTAService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/Login")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_ERROR = "/Login.jsp?action=error";

    private AccademicoService accademicoService;
    private PersonaleTAService personaleTAService;

    public void setAccademicoService(AccademicoService accademicoService) {
        this.accademicoService = accademicoService;
    }

    public void setPersonaleTAService(PersonaleTAService personaleTAService) {
        this.personaleTAService = personaleTAService;
    }

    protected AccademicoService getAccademicoService() {
        return new AccademicoService();
    }

    protected PersonaleTAService getPersonaleTAService() {
        return new PersonaleTAService();
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            initializeServices();

            final String email = request.getParameter("email");
            final String password = CredentialSecurity.hashPassword(request.getParameter("password"));

            final Accademico user1 = accademicoService.trovaEmailPassUniclass(email, password);
            final PersonaleTA user2 = personaleTAService.trovaEmailPass(email, password);

            final Utente user = resolveUser(user1, user2, request, response);
            if (user == null) {
                return; // redirect già gestito
            }

            final HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser", user);
            response.sendRedirect(request.getContextPath() + "/Home");

        } catch (final IOException e) {
            request.getServletContext().log("Error processing login request", e);
            try {
                response.sendRedirect(request.getContextPath() + LOGIN_ERROR);
            } catch (final IOException ioException) {
                request.getServletContext().log("Failed to redirect after error", ioException);
            }
        }
    }

    // ---------------------------------------------------------
    //                  METODI PRIVATI
    // ---------------------------------------------------------

    private void initializeServices() {
        if (accademicoService == null) {
            accademicoService = new AccademicoService();
        }
        if (personaleTAService == null) {
            personaleTAService = new PersonaleTAService();
        }
    }

    private Utente resolveUser(final Accademico user1, final PersonaleTA user2,
                               final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        if (user1 == null && user2 == null) {
            response.sendRedirect(request.getContextPath() + LOGIN_ERROR);
            return null;
        }

        if (user1 != null && user2 == null) {
            if (user1.isAttivato()) {
                return user1;
            }

            if (user1.getPassword() == null) {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=notactivated");
                return null;
            }

            // Caso mancante: non attivato + password NON null
            response.sendRedirect(request.getContextPath() + LOGIN_ERROR);
            return null;
        }

        if (user1 == null) {
            return user2;
        }

        return null;
    }

}
