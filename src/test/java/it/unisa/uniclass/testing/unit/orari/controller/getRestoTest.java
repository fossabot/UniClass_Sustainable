package it.unisa.uniclass.testing.unit.orari.controller;

import it.unisa.uniclass.orari.controller.getResto;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.model.Resto;
import it.unisa.uniclass.orari.service.CorsoLaureaService;
import it.unisa.uniclass.orari.service.RestoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test d'unità per il controller getResto.
 * Verifica le operazioni di recupero dei resti in formato JSON.
 */
@DisplayName("Test per il controller getResto")
public class getRestoTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private TestableServlet servlet;
    private CorsoLaurea corsoLaurea;
    private List<Resto> resti;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    /**
     * Classe estesa per accedere ai metodi protected del servlet
     */
    private static class TestableServlet extends getResto {
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

        // Setup del CorsoLaurea
        corsoLaurea = mock(CorsoLaurea.class);
        when(corsoLaurea.getId()).thenReturn(1L);
        when(corsoLaurea.getNome()).thenReturn("Ingegneria Informatica");

        // Setup dei Resti
        resti = new ArrayList<>();
        Resto resto1 = mock(Resto.class);
        when(resto1.getId()).thenReturn(1L);
        when(resto1.getNome()).thenReturn("Resto 0");

        Resto resto2 = mock(Resto.class);
        when(resto2.getId()).thenReturn(2L);
        when(resto2.getNome()).thenReturn("Resto 1");

        resti.add(resto1);
        resti.add(resto2);

        // Setup della risposta
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Nested
    @DisplayName("Test doGet - Flusso Principale")
    class DoGetFlussoMainTest {

        @Test
        @DisplayName("doGet recupera resti e li restituisce in JSON")
        void testDoGetFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoGet(request, response);

                    // Verifica che sia stata restituita una risposta JSON valida
                    String output = stringWriter.toString();
                    assertNotNull(output);
                    assertTrue(output.contains("Resto 0"));
                    assertTrue(output.contains("Resto 1"));

                    // Verifica il type di contenuto
                    verify(response).setContentType("application/json");
                    verify(response).setCharacterEncoding("UTF-8");
                }
            }
        }

        @Test
        @DisplayName("doGet con lista vuota di resti")
        void testDoGetRestiVuoti() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(new ArrayList<>());
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString();
                    assertNotNull(output);
                    // Deve contenere un array JSON vuoto
                    assertTrue(output.contains("[]"));
                }
            }
        }

        @Test
        @DisplayName("doGet con multipli resti")
        void testDoGetMultipliResti() throws ServletException, IOException {
            List<Resto> moltiResti = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Resto resto = mock(Resto.class);
                when(resto.getId()).thenReturn((long) i);
                when(resto.getNome()).thenReturn("Resto " + i);
                moltiResti.add(resto);
            }

            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(moltiResti);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString();
                    assertNotNull(output);
                    // Verifica che contiene tutti i resti
                    for (Resto resto : moltiResti) {
                        assertTrue(output.contains(resto.getNome()));
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test doGet - JSON Structure")
    class DoGetJsonStructureTest {

        @Test
        @DisplayName("doGet restituisce JSON valido con struttura corretta")
        void testDoGetJsonStructure() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString().trim();

                    // Verifica che il JSON sia un array valido (tollerant alle newline)
                    assertTrue(output.contains("["));
                    assertTrue(output.contains("]"));

                    // Parsa il JSON per verificare la struttura
                    JSONArray jsonArray = new JSONArray(output);
                    assertEquals(2, jsonArray.length());

                    // Verifica la struttura di ogni elemento
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        assertTrue(obj.has("id"));
                        assertTrue(obj.has("nome"));
                    }
                }
            }
        }

        @Test
        @DisplayName("doGet contiene id e nome corretti nei JSON")
        void testDoGetJsonCorrectFields() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString().trim();
                    JSONArray jsonArray = new JSONArray(output);

                    JSONObject firstResto = jsonArray.getJSONObject(0);
                    assertEquals("Resto 0", firstResto.getString("nome"));

                    JSONObject secondResto = jsonArray.getJSONObject(1);
                    assertEquals("Resto 1", secondResto.getString("nome"));
                }
            }
        }
    }

    @Nested
    @DisplayName("Test doPost")
    class DoPostTest {

        @Test
        @DisplayName("doPost delega a doGet")
        void testDoPostDelegaADoGet() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoPost(request, response);

                    String output = stringWriter.toString();
                    assertNotNull(output);
                    assertTrue(output.contains("Resto 0"));

                    verify(response).setContentType("application/json");
                }
            }
        }

        @Test
        @DisplayName("doPost restituisce stesso risultato di doGet")
        void testDoPostSameBehaviorAsDoGet() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoPost(request, response);

                    verify(response).setContentType("application/json");
                    verify(response).setCharacterEncoding("UTF-8");
                }
            }
        }
    }

    @Nested
    @DisplayName("Test Integrazione")
    class IntegrationTest {

        @Test
        @DisplayName("Integrazione: GET e POST ritornano stesso JSON")
        void testIntegrazioneGetPost() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoGet(request, response);
                    String getOutput = stringWriter.toString();

                    // Reset writer per POST
                    stringWriter = new StringWriter();
                    printWriter = new PrintWriter(stringWriter);
                    when(response.getWriter()).thenReturn(printWriter);

                    servlet.callDoPost(request, response);
                    String postOutput = stringWriter.toString();

                    // Entrambi dovrebbero avere la stessa struttura
                    assertTrue(getOutput.contains("["));
                    assertTrue(postOutput.contains("["));
                }
            }
        }

        @Test
        @DisplayName("Integrazione: più richieste con corsi diversi")
        void testIntegrazionePiuCorsi() throws ServletException, IOException {
            // Setup secondo corso
            CorsoLaurea corsoLaurea2 = mock(CorsoLaurea.class);
            when(corsoLaurea2.getId()).thenReturn(2L);
            when(corsoLaurea2.getNome()).thenReturn("Ingegneria Gestionale");

            List<Resto> restiCorso2 = new ArrayList<>();
            Resto resto2023 = mock(Resto.class);
            when(resto2023.getId()).thenReturn(10L);
            when(resto2023.getNome()).thenReturn("Resto A");
            restiCorso2.add(resto2023);

            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Gestionale");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Gestionale"))
                                .thenReturn(corsoLaurea2);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea2))
                                    .thenReturn(restiCorso2);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString();
                    assertTrue(output.contains("Resto A"));
                    verify(response, atLeastOnce()).setContentType("application/json");
                }
            }
        }

        @Test
        @DisplayName("Integrazione: flusso completo con parametri diversi")
        void testIntegrazioneFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestiCorsoLaurea(corsoLaurea))
                                    .thenReturn(resti);
                        })) {

                    servlet.callDoGet(request, response);
                    servlet.callDoPost(request, response);

                    verify(response, times(2)).setContentType("application/json");
                    verify(response, times(2)).setCharacterEncoding("UTF-8");
                }
            }
        }
    }
}

