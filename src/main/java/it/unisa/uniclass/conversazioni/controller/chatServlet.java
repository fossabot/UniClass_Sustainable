package it.unisa.uniclass.conversazioni.controller;

import it.unisa.uniclass.conversazioni.model.Messaggio;
import it.unisa.uniclass.conversazioni.service.MessaggioService;
import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.service.AccademicoService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "chatServlet", value = "/chatServlet")
public class chatServlet extends HttpServlet {

    private static final String PARAM_ACCADEMICO = "accademico";
    private static final String PARAM_ACCADEMICO_SELF = "accademicoSelf";

    private static final String ATTR_ACCADEMICO = "accademico";
    private static final String ATTR_ACCADEMICO_SELF = "accademicoSelf";
    private static final String ATTR_MESSAGGI = "messaggigi";
    private static final String ATTR_MESSAGGI_INVIATI = "messaggiInviati";
    private static final String ATTR_MESSAGGI_RICEVUTI = "messaggiRicevuti";

    @EJB
    private MessaggioService messaggioService;

    @EJB
    private AccademicoService accademicoService;

    /**
     * Setter per iniettare il MessaggioService (utile per i test).
     */
    public void setMessaggioService(MessaggioService messaggioService) {
        this.messaggioService = messaggioService;
    }

    /**
     * Setter per iniettare l'AccademicoService (utile per i test).
     */
    public void setAccademicoService(AccademicoService accademicoService) {
        this.accademicoService = accademicoService;
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) {
        try {
            final HttpSession session = req.getSession();

            final String email = req.getParameter(PARAM_ACCADEMICO);
            final String emailSelf = req.getParameter(PARAM_ACCADEMICO_SELF);

            final Accademico accademico = accademicoService.trovaEmailUniClass(email);
            final Accademico accademicoSelf = accademicoService.trovaEmailUniClass(emailSelf);

            final List<Messaggio> messaggigi = messaggioService.trovaTutti();

            final List<Messaggio> messaggiInviati = new ArrayList<>();
            final List<Messaggio> messaggiRicevuti = new ArrayList<>();

            for (final Messaggio messaggio : messaggigi) {
                if (messaggio.getDestinatario().getMatricola().equals(accademicoSelf.getMatricola())) {
                    messaggiRicevuti.add(messaggio);
                }
                if (messaggio.getAutore().getMatricola().equals(accademicoSelf.getMatricola())) {
                    messaggiInviati.add(messaggio);
                }
            }

            req.setAttribute(ATTR_MESSAGGI, messaggigi);
            session.setAttribute(ATTR_MESSAGGI, messaggigi);

            req.setAttribute(ATTR_MESSAGGI_INVIATI, messaggiInviati);
            req.setAttribute(ATTR_MESSAGGI_RICEVUTI, messaggiRicevuti);

            req.setAttribute(ATTR_ACCADEMICO, accademico);
            session.setAttribute(ATTR_ACCADEMICO, accademico);

            req.setAttribute(ATTR_ACCADEMICO_SELF, accademicoSelf);
            session.setAttribute(ATTR_ACCADEMICO_SELF, accademicoSelf);

            resp.sendRedirect(req.getContextPath() + "/chat.jsp");

        } catch (final IOException e) {
            req.getServletContext().log("Error processing chat request", e);
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred processing your request");
            } catch (final IOException ioException) {
                req.getServletContext().log("Failed to send error response", ioException);
            }
        }
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
