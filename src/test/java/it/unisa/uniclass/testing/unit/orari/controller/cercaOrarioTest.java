package it.unisa.uniclass.testing.unit.orari.controller;

import it.unisa.uniclass.orari.controller.cercaOrario;
import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.model.Giorno;
import it.unisa.uniclass.orari.model.Lezione;
import it.unisa.uniclass.orari.model.Resto;
import it.unisa.uniclass.orari.service.AnnoDidatticoService;
import it.unisa.uniclass.orari.service.CorsoLaureaService;
import it.unisa.uniclass.orari.service.LezioneService;
import it.unisa.uniclass.orari.service.RestoService;
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

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test d'unità per il controller cercaOrario.
 * Verifica le operazioni di ricerca dell'orario dei corsi.
 */
@DisplayName("Test per il controller cercaOrario")
public class cercaOrarioTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private TestableServlet servlet;
    private CorsoLaurea corsoLaurea;
    private Resto resto;
    private AnnoDidattico annoDidattico;
    private List<Lezione> lezioni;

    /**
     * Classe estesa per accedere ai metodi protected del servlet
     */
    private static class TestableServlet extends cercaOrario {
        public void callDoPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        public void callDoGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doGet(request, response);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new TestableServlet();

        // Setup dei dati di test
        corsoLaurea = mock(CorsoLaurea.class);
        when(corsoLaurea.getId()).thenReturn(1L);
        when(corsoLaurea.getNome()).thenReturn("Ingegneria Informatica");

        resto = new Resto();
        resto.setNome("Resto 0");
        resto.setCorsoLaurea(corsoLaurea);
        // Mock dell'ID del resto poiché è una field privata con @GeneratedValue
        resto = spy(resto);
        when(resto.getId()).thenReturn(1L);

        annoDidattico = new AnnoDidattico("2023-2024");
        // Mock dell'ID dell'anno didattico poiché è una field privata con @GeneratedValue
        annoDidattico = spy(annoDidattico);
        when(annoDidattico.getId()).thenReturn(1);

        // Setup delle lezioni
        lezioni = new ArrayList<>();
        Lezione lezione1 = new Lezione(1, Time.valueOf("09:00:00"), Time.valueOf("11:00:00"),
                Giorno.LUNEDI, null, null, null);
        Lezione lezione2 = new Lezione(2, Time.valueOf("11:00:00"), Time.valueOf("13:00:00"),
                Giorno.MARTEDI, null, null, null);
        lezioni.add(lezione1);
        lezioni.add(lezione2);

        // Setup del request dispatcher
        when(request.getRequestDispatcher("/OrarioSingolo.jsp")).thenReturn(requestDispatcher);
    }

    @Nested
    @DisplayName("Test del metodo doPost - Flusso Principale")
    class DoPostFlussoMainTest {

        @Test
        @DisplayName("doPost recupera corsi, resti, anni e lezioni correttamente")
        void testDoPostFlussoCompleto() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioni);
                                })) {

                            servlet.callDoPost(request, response);

                            verify(request).getParameter("corsoLaurea");
                            verify(request).getParameter("resto");
                            verify(request).getParameter("anno");
                            verify(request).setAttribute("lezioni", lezioni);
                            verify(request).setAttribute("corsoLaurea", corsoLaurea);
                            verify(request).setAttribute("resto", resto);
                            verify(request).setAttribute("anno", annoDidattico);
                            verify(request).getRequestDispatcher("/OrarioSingolo.jsp");
                            verify(requestDispatcher).forward(request, response);
                        }
                    }
                }
            }
        }

        @Test
        @DisplayName("doPost ordina correttamente le lezioni per giorno e ora")
        void testDoPostOrdnaLezioni() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            List<Lezione> lezioniNonOrdinate = new ArrayList<>();
            Lezione lez3 = new Lezione(3, Time.valueOf("14:00:00"), Time.valueOf("16:00:00"),
                    Giorno.MERCOLEDI, null, null, null);
            Lezione lez1 = new Lezione(1, Time.valueOf("09:00:00"), Time.valueOf("11:00:00"),
                    Giorno.LUNEDI, null, null, null);
            Lezione lez2 = new Lezione(2, Time.valueOf("11:00:00"), Time.valueOf("13:00:00"),
                    Giorno.LUNEDI, null, null, null);
            lezioniNonOrdinate.add(lez3);
            lezioniNonOrdinate.add(lez1);
            lezioniNonOrdinate.add(lez2);

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioniNonOrdinate);
                                })) {

                            servlet.callDoPost(request, response);

                            verify(request).setAttribute(eq("lezioni"), any(List.class));
                            verify(requestDispatcher).forward(request, response);
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo doPost - Parametri")
    class DoPostParametriTest {

        @Test
        @DisplayName("doPost con parametri validi")
        void testDoPostParametriValidi() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioni);
                                })) {

                            servlet.callDoPost(request, response);

                            verify(request, atLeast(1)).getParameter("corsoLaurea");
                            verify(request, atLeast(1)).getParameter("resto");
                            verify(request, atLeast(1)).getParameter("anno");
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo doPost - Gestione Dati Null")
    class DoPostNullDataTest {

        @Test
        @DisplayName("doPost con corso nullo")
        void testDoPostCorsoNullo() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Inesistente");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Inesistente"))
                                .thenReturn(null);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class)) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class)) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class)) {

                            assertThrows(NullPointerException.class, () -> {
                                servlet.callDoPost(request, response);
                            });
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo doPost - Lezioni Vuote")
    class DoPostLezioniVuoteTest {

        @Test
        @DisplayName("doPost con lista lezioni vuota")
        void testDoPostLezioniVuote() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(new ArrayList<>());
                                })) {

                            servlet.callDoPost(request, response);

                            verify(request).setAttribute(eq("lezioni"), any(List.class));
                            verify(requestDispatcher).forward(request, response);
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo doGet")
    class DoGetTest {

        @Test
        @DisplayName("doGet delega a doPost")
        void testDoGetDelegaADoPost() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioni);
                                })) {

                            servlet.callDoGet(request, response);

                            verify(requestDispatcher).forward(request, response);
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Test di Integrazione")
    class IntegrationTest {

        @Test
        @DisplayName("Flusso completo: richiesta GET e POST")
        void testFlussoCompletoGetPost() throws ServletException, IOException {
            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 0");
            when(request.getParameter("anno")).thenReturn("2023-2024");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 0", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2023-2024"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioni);
                                })) {

                            servlet.callDoGet(request, response);
                            servlet.callDoPost(request, response);

                            verify(requestDispatcher, times(2)).forward(request, response);
                        }
                    }
                }
            }
        }

        @Test
        @DisplayName("Flusso con multipli resti e anni")
        void testFlussoConMultipliRestieAnni() throws ServletException, IOException {
            List<Lezione> lezioniTest = new ArrayList<>();
            lezioniTest.add(new Lezione(1, Time.valueOf("09:00:00"), Time.valueOf("11:00:00"),
                    Giorno.LUNEDI, null, null, null));

            when(request.getParameter("corsoLaurea")).thenReturn("Ingegneria Informatica");
            when(request.getParameter("resto")).thenReturn("Resto 1");
            when(request.getParameter("anno")).thenReturn("2024-2025");

            try (MockedConstruction<CorsoLaureaService> mockedCorsoLaureaService = mockConstruction(CorsoLaureaService.class,
                    (mock, context) -> {
                        when(mock.trovaCorsoLaurea("Ingegneria Informatica"))
                                .thenReturn(corsoLaurea);
                    })) {
                try (MockedConstruction<RestoService> mockedRestoService = mockConstruction(RestoService.class,
                        (mock, context) -> {
                            when(mock.trovaRestoNomeCorso("Resto 1", corsoLaurea))
                                    .thenReturn(resto);
                        })) {
                    try (MockedConstruction<AnnoDidatticoService> mockedAnnoService = mockConstruction(AnnoDidatticoService.class,
                            (mock, context) -> {
                                when(mock.trovaTuttiCorsoLaureaNome(1L, "2024-2025"))
                                        .thenReturn(annoDidattico);
                            })) {
                        try (MockedConstruction<LezioneService> mockedLezioneService = mockConstruction(LezioneService.class,
                                (mock, context) -> {
                                    when(mock.trovaLezioniCorsoLaureaRestoAnno(1L, 1L, 1))
                                            .thenReturn(lezioniTest);
                                })) {

                            servlet.callDoPost(request, response);

                            verify(request).setAttribute(eq("lezioni"), any(List.class));
                            verify(requestDispatcher).forward(request, response);
                        }
                    }
                }
            }
        }
    }
}

