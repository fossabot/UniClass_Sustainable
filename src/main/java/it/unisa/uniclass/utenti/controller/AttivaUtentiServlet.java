package it.unisa.uniclass.utenti.controller;

import it.unisa.uniclass.common.security.CredentialSecurity;
import it.unisa.uniclass.common.security.PasswordGenerator;
import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.model.Tipo;
import it.unisa.uniclass.utenti.service.AccademicoService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AttivaUtentiServlet", value = "/AttivaUtentiServlet")
public class AttivaUtentiServlet extends HttpServlet {

    private transient AccademicoService accademicoService;

    // Setter per test
    public void setAccademicoService(AccademicoService accademicoService) {
        this.accademicoService = accademicoService;
    }

    public AttivaUtentiServlet() {
        accademicoService = new AccademicoService();
    }

    public AttivaUtentiServlet(AccademicoService acc) {
        accademicoService = acc;
    }

    // Metodo pubblico per i test
    public void doPostPublic(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String param = req.getParameter("param");

            if ("add".equals(param)) {
                handleAdd(req, resp);
            } else if ("remove".equals(param)) {
                handleRemove(req, resp);
            }

        } catch (IOException e) {
            req.getServletContext().log("Error processing user activation request", e);
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred processing your request");
            } catch (IOException ioException) {
                req.getServletContext().log("Failed to send error response", ioException);
            }
        }
    }

    // -----------------------------
    //        METODI PRIVATI
    // -----------------------------

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String matricola = req.getParameter("matricola");
        String tiporeq = req.getParameter("tipo");

        Accademico accEmail = accademicoService.trovaEmailUniClass(email);
        Accademico accMatricola = accademicoService.trovaAccademicoUniClass(matricola);

        if (!isSameAccademico(accEmail, accMatricola)) {
            redirectError(resp, req);
            return;
        }

        Tipo tipo = parseTipo(tiporeq);
        if (tipo == null || !accEmail.getTipo().equals(tipo)) {
            redirectError(resp, req);
            return;
        }

        activateAccademico(accEmail, req);
        resp.sendRedirect(req.getContextPath() + "/PersonaleTA/AttivaUtenti.jsp");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email-remove");

        Accademico accademico = accademicoService.trovaEmailUniClass(email);
        if (accademico != null) {
            accademicoService.cambiaAttivazione(accademico, false);
        }

        resp.sendRedirect(req.getContextPath() + "/PersonaleTA/AttivaUtenti.jsp");
    }

    private boolean isSameAccademico(Accademico a1, Accademico a2) {
        return a1 != null && a2 != null &&
                a1.getEmail().equals(a2.getEmail()) &&
                a1.getMatricola().equals(a2.getMatricola());
    }

    private Tipo parseTipo(String tiporeq) {
        switch (tiporeq) {
            case "Studente": return Tipo.Studente;
            case "Docente": return Tipo.Docente;
            case "Coordinatore": return Tipo.Coordinatore;
            default: return null;
        }
    }

    private void activateAccademico(Accademico accademico, HttpServletRequest req) {
        String password = PasswordGenerator.generatePassword(8);

        accademico.setAttivato(true);
        accademico.setPassword(CredentialSecurity.hashPassword(password));

        accademicoService.aggiungiAccademico(accademico);

        // Log sicuro (niente System.out)
        req.getServletContext().log("Password generata per l'attivato: " + password);
    }

    private void redirectError(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/PersonaleTA/AttivaUtenti.jsp?action=error");
    }
}
