package it.unisa.uniclass.testing.unit.orari.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisa.uniclass.orari.controller.getAnno;
import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.service.AnnoDidatticoService;
import it.unisa.uniclass.orari.service.CorsoLaureaService;
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
 * Test d'unità per il controller getAnno.
 * Verifica le operazioni di recupero degli anni didattici in formato JSON.
 */
@DisplayName("Test per il controller getAnno")
public class getAnnoTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private TestableServlet servlet;
    private CorsoLaurea corsoLaurea;
    private List<AnnoDidattico> anni;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    /**
     * Classe estesa per accedere ai metodi protected del servlet
     */
    private static class TestableServlet extends getAnno {
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

        // Setup degli AnniDidattici
        anni = new ArrayList<>();
        AnnoDidattico anno1 = mock(AnnoDidattico.class);
        when(anno1.getId()).thenReturn(1);
        when(anno1.getAnno()).thenReturn("2023-2024");

        AnnoDidattico anno2 = mock(AnnoDidattico.class);
        when(anno2.getId()).thenReturn(2);
        when(anno2.getAnno()).thenReturn("2024-2025");

        anni.add(anno1);
        anni.add(anno2);

        // Setup della risposta
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Nested
    @DisplayName("Test doGet - Flusso Principale")
    class DoGetFlussoMainTest {

        @Test
        @DisplayName("doGet recupera anni didattici e li restituisce in JSON")
        void testDoGetFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
                        })) {

                    servlet.callDoGet(request, response);

                    // Verifica che sia stata restituita una risposta JSON valida
                    String output = stringWriter.toString();
                    assertNotNull(output);
                    assertTrue(output.contains("2023-2024"));
                    assertTrue(output.contains("2024-2025"));

                    // Verifica il type di contenuto
                    verify(response).setContentType("application/json");
                    verify(response).setCharacterEncoding("UTF-8");
                }
            }
        }

        @Test
        @DisplayName("doGet con lista vuota di anni")
        void testDoGetAnniVuoti() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
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
        @DisplayName("doGet con multipli anni didattici")
        void testDoGetMultipliAnni() throws ServletException, IOException {
            List<AnnoDidattico> moltiAnni = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                AnnoDidattico anno = mock(AnnoDidattico.class);
                when(anno.getId()).thenReturn(i);
                when(anno.getAnno()).thenReturn((2020 + i) + "-" + (2021 + i));
                moltiAnni.add(anno);
            }

            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(moltiAnni);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString();
                    assertNotNull(output);
                    // Verifica che contiene tutti gli anni
                    for (AnnoDidattico anno : moltiAnni) {
                        assertTrue(output.contains(anno.getAnno()));
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
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString().trim();

                    // Verifica che il JSON sia un array valido
                    assertTrue(output.startsWith("["));
                    assertTrue(output.endsWith("]"));

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
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString().trim();
                    JSONArray jsonArray = new JSONArray(output);

                    JSONObject firstAnno = jsonArray.getJSONObject(0);
                    assertEquals(1, firstAnno.getInt("id"));
                    assertEquals("2023-2024", firstAnno.getString("nome"));

                    JSONObject secondAnno = jsonArray.getJSONObject(1);
                    assertEquals(2, secondAnno.getInt("id"));
                    assertEquals("2024-2025", secondAnno.getString("nome"));
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
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
                        })) {

                    servlet.callDoPost(request, response);

                    String output = stringWriter.toString();
                    assertNotNull(output);
                    assertTrue(output.contains("2023-2024"));

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
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
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
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(1L))
                                    .thenReturn(anni);
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

            List<AnnoDidattico> anniCorso2 = new ArrayList<>();
            AnnoDidattico anno2023 = mock(AnnoDidattico.class);
            when(anno2023.getId()).thenReturn(3);
            when(anno2023.getAnno()).thenReturn("2023-2024");
            anniCorso2.add(anno2023);

            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Gestionale");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Gestionale"))
                                .thenReturn(corsoLaurea2);
                    })) {
                try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                        (mock, context) -> {
                            when(mock.trovaTuttiCorsoLaurea(2L))
                                    .thenReturn(anniCorso2);
                        })) {

                    servlet.callDoGet(request, response);

                    String output = stringWriter.toString();
                    assertTrue(output.contains("2023-2024"));
                    verify(response, atLeastOnce()).setContentType("application/json");
                }
            }
        }
    }
}

