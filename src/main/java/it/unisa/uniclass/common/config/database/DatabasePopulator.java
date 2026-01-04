package it.unisa.uniclass.common.config.database;

import it.unisa.uniclass.orari.model.*;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;

@Singleton
@Startup
@DataSourceDefinition(
        name = "java:app/jdbc/DBUniClass",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://postgres:5432/dbuniclass",
        user = "postgres",
        password = "password"
)
@LocalBean
public class DatabasePopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabasePopulator.class);

    // ✅ Costanti per valori duplicati
    private static final String CORSO_INFORMATICA = "Informatica";

    private static final String TIME_0900 = "09:00:00";
    private static final String TIME_1100 = "11:00:00";
    private static final String TIME_1300 = "13:00:00";
    private static final String TIME_1330 = "13:30:00";
    private static final String TIME_1400 = "14:00:00";
    private static final String TIME_1530 = "15:30:00";
    private static final String TIME_1730 = "17:30:00";

    @Inject //NOSONAR
    private EntityManager em;

    @PostConstruct
    public void popola() {
        LOGGER.info("AAAAAA");
        popolaDBUniversity();
        popolaDBUniClass();
        LOGGER.info("BBBBBB");
    }

    public void popolaDBUniClass() {
        // Creazione del corso di laurea
        CorsoLaurea corsoLaurea = new CorsoLaurea();
        corsoLaurea.setNome(CORSO_INFORMATICA);

        CorsoLaurea corsoLaurea2 = new CorsoLaurea();
        corsoLaurea2.setNome("Fisica");

        // Creazione dei resti e associazione con il corso di laurea
        Resto resto = new Resto();
        resto.setNome("Resto 0");
        resto.setCorsoLaurea(corsoLaurea);

        Resto resto2 = new Resto();
        resto2.setNome("Resto 1");
        resto2.setCorsoLaurea(corsoLaurea);

        Resto resto3 = new Resto();
        resto3.setNome("Resto 2");
        resto3.setCorsoLaurea(corsoLaurea);

        corsoLaurea.getResti().add(resto);
        corsoLaurea.getResti().add(resto2);
        corsoLaurea.getResti().add(resto3);

        // Creazione degli anni didattici
        AnnoDidattico anno1 = new AnnoDidattico("Anno 1");
        AnnoDidattico anno2 = new AnnoDidattico("Anno 2");
        AnnoDidattico anno3 = new AnnoDidattico("Anno 3");

        corsoLaurea.getAnniDidattici().add(anno1);
        corsoLaurea.getAnniDidattici().add(anno2);
        corsoLaurea.getAnniDidattici().add(anno3);

        anno1.getCorsiLaurea().add(corsoLaurea);
        anno2.getCorsiLaurea().add(corsoLaurea);
        anno3.getCorsiLaurea().add(corsoLaurea);

        // Creazione corsi
        Corso corso1 = new Corso();
        corso1.setNome("Ingegneria del Software");
        Corso corso2 = new Corso();
        corso2.setNome("Fondamenti di Intelligenza Artificiale");
        Corso corso3 = new Corso();
        corso3.setNome("Programmazione Distribuita");

        corso1.setCorsoLaurea(corsoLaurea);
        corso2.setCorsoLaurea(corsoLaurea);
        corso3.setCorsoLaurea(corsoLaurea);

        corsoLaurea.getCorsi().add(corso1);
        corsoLaurea.getCorsi().add(corso2);
        corsoLaurea.getCorsi().add(corso3);

        corso1.setAnnoDidattico(anno3);
        corso2.setAnnoDidattico(anno3);
        corso3.setAnnoDidattico(anno3);

        anno3.getCorsi().add(corso1);
        anno3.getCorsi().add(corso2);
        anno3.getCorsi().add(corso3);

        // Lezioni
        Lezione lezione1 = new Lezione();
        lezione1.setCorso(corso1);
        lezione1.setGiorno(Giorno.LUNEDI);
        lezione1.setResto(resto);
        lezione1.setOraInizio(Time.valueOf(TIME_1100));
        lezione1.setOraFine(Time.valueOf(TIME_1400));
        lezione1.setSemestre(1);

        Lezione lezione2 = new Lezione();
        lezione2.setCorso(corso1);
        lezione2.setGiorno(Giorno.GIOVEDI);
        lezione2.setResto(resto);
        lezione2.setOraInizio(Time.valueOf(TIME_0900));
        lezione2.setOraFine(Time.valueOf(TIME_1100));
        lezione2.setSemestre(1);

        Lezione lezione3 = new Lezione();
        lezione3.setCorso(corso1);
        lezione3.setGiorno(Giorno.VENERDI);
        lezione3.setResto(resto);
        lezione3.setOraInizio(Time.valueOf(TIME_1100));
        lezione3.setOraFine(Time.valueOf(TIME_1300));
        lezione3.setSemestre(1);

        Lezione lezione4 = new Lezione();
        lezione4.setCorso(corso2);
        lezione4.setGiorno(Giorno.MARTEDI);
        lezione4.setResto(resto);
        lezione4.setOraInizio(Time.valueOf(TIME_1330));
        lezione4.setOraFine(Time.valueOf(TIME_1530));
        lezione4.setSemestre(1);

        Lezione lezione5 = new Lezione();
        lezione5.setCorso(corso2);
        lezione5.setGiorno(Giorno.GIOVEDI);
        lezione5.setResto(resto);
        lezione5.setOraInizio(Time.valueOf(TIME_1330));
        lezione5.setOraFine(Time.valueOf(TIME_1530));
        lezione5.setSemestre(1);

        Lezione lezione6 = new Lezione();
        lezione6.setCorso(corso3);
        lezione6.setGiorno(Giorno.LUNEDI);
        lezione6.setResto(resto);
        lezione6.setOraInizio(Time.valueOf("14:30:00")); // non segnalato da SonarQube
        lezione6.setOraFine(Time.valueOf(TIME_1730));
        lezione6.setSemestre(1);

        // Creazione delle aule
        Aula aulaF1 = new Aula();
        aulaF1.setNome("F1");
        aulaF1.setEdificio("F");

        Aula aulaF2 = new Aula();
        aulaF2.setNome("F2");
        aulaF2.setEdificio("F");

        Aula aulaF3 = new Aula();
        aulaF3.setNome("F3");
        aulaF3.setEdificio("F");

        Aula aulaP1 = new Aula();
        aulaP1.setNome("P1");
        aulaP1.setEdificio("P");

        Aula aulaP2 = new Aula();
        aulaP2.setNome("P2");
        aulaP2.setEdificio("P");

        // Associazione lezioni alle aule
        lezione1.setAula(aulaF1);
        lezione2.setAula(aulaF2);
        lezione3.setAula(aulaF3);
        lezione4.setAula(aulaP1);
        lezione5.setAula(aulaP2);
        lezione6.setAula(aulaF1);

        // Persistenza
        em.persist(anno1);
        em.persist(anno2);
        em.persist(anno3);
        em.persist(corsoLaurea);
        em.persist(corsoLaurea2);
        em.persist(resto);
        em.persist(resto2);
        em.persist(resto3);
        em.persist(corso1);
        em.persist(corso2);
        em.persist(corso3);
        em.persist(aulaF1);
        em.persist(aulaF2);
        em.persist(aulaF3);
        em.persist(aulaP1);
        em.persist(aulaP2);
        em.persist(lezione1);
        em.persist(lezione2);
        em.persist(lezione3);
        em.persist(lezione4);
        em.persist(lezione5);
        em.persist(lezione6);
        em.flush();
        em.clear();
    }

    public void popolaDBUniversity() {
        // Metodo lasciato vuoto intenzionalmente per futuri sviluppi
    }
}
