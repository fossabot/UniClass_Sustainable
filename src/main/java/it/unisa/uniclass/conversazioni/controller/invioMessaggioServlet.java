package it.unisa.uniclass.conversazioni.controller;

import it.unisa.uniclass.conversazioni.model.Messaggio;
import it.unisa.uniclass.conversazioni.model.Topic;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "invioMessaggio", value = "/invioMessaggioServlet")
public class invioMessaggioServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(invioMessaggioServlet.class);

    @EJB
    //@ spec_public
    //@ nullable
    private transient MessaggioService messaggioService;

    @EJB
    //@ spec_public
    //@ nullable
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

    /**
     * Gestisce le richieste GET per inviare un messaggio o un avviso.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();

            String emailSession = (String) session.getAttribute("utenteEmail");
            String emailDest = request.getParameter("email");
            String messaggio = request.getParameter("testo");
            String topic = request.getParameter("topic");

            LOGGER.info("Topic ricevuto: {}", topic);

            Accademico accademicoSelf = accademicoService.trovaEmailUniClass(emailSession);
            Accademico accademicoDest = accademicoService.trovaEmailUniClass(emailDest);

            Topic top = new Topic();
            if (topic != null) {
                top.setNome(topic);
                top.setCorsoLaurea(accademicoSelf.getCorsoLaurea());
            }

            Messaggio messaggio1 = new Messaggio();
            messaggio1.setAutore(accademicoSelf);
            messaggio1.setDestinatario(accademicoDest);
            messaggio1.setBody(messaggio);
            messaggio1.setDateTime(LocalDateTime.now());

            if (topic != null) {
                messaggio1.setTopic(top);
            }

            Messaggio test = messaggioService.aggiungiMessaggio(messaggio1);
            Long messageId = test.getId();

            LOGGER.info("Messaggio ID: {} - {}", messageId, test);

            List<Messaggio> messaggi = messaggioService.trovaTutti();
            LOGGER.info("Messaggi trovati: {}", messaggi);

            request.setAttribute("messaggi", messaggi);
            request.setAttribute("accademici",
                    messaggioService.trovaMessaggeriDiUnAccademico(accademicoSelf.getMatricola()));

            response.sendRedirect("Conversazioni");

        } catch (IOException e) {
            request.getServletContext().log("Error processing message sending request", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred processing your request");
            } catch (IOException ioException) {
                request.getServletContext().log("Failed to send error response", ioException);
            }
        }
    }

    /**
     * Gestisce le richieste POST delegando al metodo doGet.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
