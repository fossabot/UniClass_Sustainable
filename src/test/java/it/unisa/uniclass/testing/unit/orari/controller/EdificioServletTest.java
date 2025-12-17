package it.unisa.uniclass.testing.unit.orari.controller;

import it.unisa.uniclass.orari.controller.EdificioServlet;
import it.unisa.uniclass.orari.model.Aula;
import it.unisa.uniclass.orari.service.AulaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test d'unità per il controller EdificioServlet.
 * Verifica le operazioni di ricerca delle aule per edificio con maximum branch coverage.
 */
@DisplayName("Test per il controller EdificioServlet")
public class EdificioServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private TestableServlet servlet;
    private List<Aula> aule;

    /**
     * Classe estesa per accedere ai metodi protected del servlet
     */
    private static class TestableServlet extends EdificioServlet {
        public void callDoGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doGet(request, response);
        }

        public void callDoPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new TestableServlet();

        // Setup delle aule
        aule = new ArrayList<>();
        Aula aula1 = new Aula();
        aula1.setNome("Aula 101");
        aula1.setEdificio("Edificio A");

        Aula aula2 = new Aula();
        aula2.setNome("Aula 102");
        aula2.setEdificio("Edificio A");

        aule.add(aula1);
        aule.add(aula2);

        // Setup del request dispatcher
        when(request.getRequestDispatcher("/edificio.jsp")).thenReturn(requestDispatcher);
    }

    @Nested
    @DisplayName("Test doGet - Flusso Principale (Branch Coverage)")
    class DoGetFlussoMainTest {

        @Test
        @DisplayName("doGet: recupera e visualizza aule per edificio")
        void testDoGetFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio A");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio A"))
                                .thenReturn(aule);
                    })) {

                servlet.callDoGet(request, response);

                verify(request).getParameter("ed");
                verify(request).setAttribute("aule", aule);
                verify(request).setAttribute("ed", "Edificio A");
                verify(request).getRequestDispatcher("/edificio.jsp");
                verify(requestDispatcher).forward(request, response);
            }
        }

        @Test
        @DisplayName("doGet: gestisce lista vuota di aule")
        void testDoGetAuleVuote() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio Vuoto");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio Vuoto"))
                                .thenReturn(new ArrayList<>());
                    })) {

                servlet.callDoGet(request, response);

                verify(request).setAttribute(eq("aule"), any(List.class));
                verify(request).setAttribute("ed", "Edificio Vuoto");
                verify(requestDispatcher).forward(request, response);
            }
        }

        @Test
        @DisplayName("doGet: gestisce multipli edifici con aule diverse")
        void testDoGetMultipliEdifici() throws ServletException, IOException {
            List<Aula> auleDifferenti = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Aula aula = new Aula();
                aula.setNome("Aula " + (200 + i));
                aula.setEdificio("Edificio B");
                auleDifferenti.add(aula);
            }

            when(request.getParameter("ed")).thenReturn("Edificio B");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio B"))
                                .thenReturn(auleDifferenti);
                    })) {

                servlet.callDoGet(request, response);

                verify(request).setAttribute("aule", auleDifferenti);
                verify(request).setAttribute("ed", "Edificio B");
                verify(requestDispatcher).forward(request, response);
            }
        }
    }

    @Nested
    @DisplayName("Test doGet - Eccezioni e Branch Coverage")
    class DoGetExceptionTest {

        @Test
        @DisplayName("doGet: gestisce parametro null")
        void testDoGetParametroNull() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn(null);

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio(null))
                                .thenReturn(new ArrayList<>());
                    })) {

                servlet.callDoGet(request, response);

                verify(request).setAttribute("ed", null);
                verify(request).setAttribute(eq("aule"), any(List.class));
                verify(requestDispatcher).forward(request, response);
            }
        }

        @Test
        @DisplayName("doGet: gestisce parametri diversi")
        void testDoGetParametriDiversi() throws ServletException, IOException {
            String[] edifici = {"Edificio A", "Edificio B", "Edificio C"};

            for (String edificio : edifici) {
                when(request.getParameter("ed")).thenReturn(edificio);

                try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                        (mock, context) -> {
                            when(mock.trovaAuleEdificio(edificio))
                                    .thenReturn(new ArrayList<>());
                        })) {

                    servlet.callDoGet(request, response);

                    verify(request).setAttribute("ed", edificio);
                }
            }
        }
    }

    @Nested
    @DisplayName("Test doPost - Comportamento (Branch Coverage)")
    class DoPostTest {

        @Test
        @DisplayName("doPost: delega completamente a doGet")
        void testDoPostDelegaADoGet() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio A");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio A"))
                                .thenReturn(aule);
                    })) {

                servlet.callDoPost(request, response);

                verify(request).getParameter("ed");
                verify(request).setAttribute("aule", aule);
                verify(requestDispatcher).forward(request, response);
            }
        }

        @Test
        @DisplayName("doPost: esegue esattamente il comportamento di doGet")
        void testDoPostComportamentoIdentico() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio X");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio X"))
                                .thenReturn(aule);
                    })) {

                servlet.callDoPost(request, response);

                verify(request).setAttribute("ed", "Edificio X");
                verify(request).setAttribute("aule", aule);
                verify(requestDispatcher).forward(request, response);
            }
        }
    }

    @Nested
    @DisplayName("Test Integrazione - Maximum Branch Coverage")
    class IntegrationTestMaxCoverage {

        @Test
        @DisplayName("Integrazione: flusso completo GET -> POST -> forward")
        void testIntegrazioneFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio A");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio A"))
                                .thenReturn(aule);
                    })) {

                // Testa sia GET che POST
                servlet.callDoGet(request, response);
                servlet.callDoPost(request, response);

                // Forward deve essere chiamato 2 volte
                verify(requestDispatcher, times(2)).forward(request, response);
                // Attributi devono essere settati per entrambe le richieste
                verify(request, atLeastOnce()).setAttribute("aule", aule);
                verify(request, atLeastOnce()).setAttribute("ed", "Edificio A");
            }
        }

        @Test
        @DisplayName("Integrazione: GET e POST con parametri diversi")
        void testIntegrazioneParametriDiversi() throws ServletException, IOException {
            when(request.getParameter("ed")).thenReturn("Edificio X");

            try (MockedConstruction<AulaService> mockedAulaService = mockConstruction(AulaService.class,
                    (mock, context) -> {
                        when(mock.trovaAuleEdificio("Edificio X"))
                                .thenReturn(aule);
                    })) {

                servlet.callDoGet(request, response);
                servlet.callDoPost(request, response);

                verify(request, atLeastOnce()).setAttribute("ed", "Edificio X");
                verify(requestDispatcher, times(2)).forward(request, response);
            }
        }
    }
}

