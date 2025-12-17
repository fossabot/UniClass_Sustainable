package it.unisa.uniclass.common;

import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.service.CorsoLaureaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/Home")
public class IndexServlet extends HttpServlet {



    private void listJNDI(Context ctx, String name) throws Exception {
        NamingEnumeration<NameClassPair> list = ctx.list(name);
        while (list.hasMore()) {
            NameClassPair nc = list.next();
            String fullName = name + (name.endsWith("/") ? "" : "/") + nc.getName();
            System.out.println("JNDI Name: " + fullName);
            try {
                // recursive call to list subcontexts
                listJNDI(ctx, fullName);
            } catch (Exception e) {
                // ignore if not a context
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Context ctx = new InitialContext();
            System.out.println("---- Listing JNDI java:global ----");
            listJNDI(ctx, "java:global");
            System.out.println("---- End JNDI listing ----");
        } catch (Exception e) {
            request.getServletContext().log("Error listing JNDI resources", e);
        }

        try {
            CorsoLaureaService corsoLaureaService = new CorsoLaureaService();
            List<CorsoLaurea> corsi = corsoLaureaService.trovaTutti();
            System.out.println(corsi);
            request.setAttribute("corsi", corsi);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.getServletContext().log("Error retrieving courses", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred processing your request");
            } catch (IOException ioException) {
                request.getServletContext().log("Failed to send error response", ioException);
            }
        }
    }
}
