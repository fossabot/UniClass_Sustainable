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
    private transient MessaggioService messaggioService;

    @EJB
    private transient AccademicoService accademicoService;

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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();

            String email = req.getParameter(PARAM_ACCADEMICO);
            String emailSelf = req.getParameter(PARAM_ACCADEMICO_SELF);

            Accademico accademico = accademicoService.trovaEmailUniClass(email);
            Accademico accademicoSelf = accademicoService.trovaEmailUniClass(emailSelf);

            List<Messaggio> messaggigi = messaggioService.trovaTutti();

            List<Messaggio> messaggiInviati = new ArrayList<>();
            List<Messaggio> messaggiRicevuti = new ArrayList<>();

            for (Messaggio messaggio : messaggigi) {
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

        } catch (IOException e) {
            req.getServletContext().log("Error processing chat request", e);
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred processing your request");
            } catch (IOException ioException) {
                req.getServletContext().log("Failed to send error response", ioException);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
