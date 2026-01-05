# Contesto
In un corso di sostenibilità del software, la key-dimension "Tecnica" non deve di certo mancare lo studio del technical debt.
Secondo gli studi, il Technical Debt (TD) è una metafora che rappresenta il costo implicito di scelte di sviluppo software a breve termine che possono portare a problemi a lungo termine. Queste scelte possono includere l'adozione di soluzioni rapide, la mancanza di documentazione adeguata, la scarsa qualità del codice e la mancanza di test automatizzati.

Di certo ne abbiamo sentito parlare molto durante il corso, ma non ci siamo focalizzati abbastanza su una cosa fondamentale: certi progetti **nascono già *problematici***. In effetti, il technical debt non è semplicemente dato dall'uso di design pattern o pratiche di sviluppo non ottimali, bensì dalla mancata cura nella progettazione della propria base di dati, di come avvengono le chiamate, della scalabilità del sistema etc.

# Tools
Per analizzare il technical debt di un progetto software, esistono diversi tool che possono essere utilizzati per identificare e quantificare il debito tecnico presente nel codice (relativo alla progettazione). Alcuni dei tool più comuni includono:
- **FIND** - funzione per l'analisi statica del livello architetturale quantitativo e qualitativo del software
- **Call Graph Analysis** - script Python per analizzare le dipendenze tra i vari componenti del software, in particolare tra i servizi e i DAO/Repository. In caso di elevato accoppiamento tra i due tipi di componenti del software, si considera presente il technical debt
- **Query Logs Analysis** - una semplice analisi delle query log e del tempo richiesto per l'esecuzione di ognuna di esse, (dopo una determinata soglia, e.g. 25ms), determina la complessità della base di dati su cui si poggia la web application.
- **Normalization??**

## Installazione
È consigliato entrare nella cartella di ognuno e fare mvn clean package per installare i tool.

### Static Dependency Analysis
1. `mvn clean package -DskipTests` # per avere il target con il war all'interno
2. `mkdir -p td-analysis/{input,output,classes}` # creare le cartelle necessarie
3. `cp target/*.war td-analysis/input/app.war` # copiare il war nell'input
4. Estrazione classi 
```batch
mkdir classes/
cd input/
jar -xf app.war WEB-INF/classes/
cp -r WEB-INF/classes/* ../../classes/
cd ../
```
5. jar cf app-classes.jar -C classes/ . # creare il jar delle classi 
6. Scrittura Report
```batch

echo "=== BASELINE METRICS ==="
cat > td-report.txt << EOF
=== TECHNICAL DEBT METRICS ===
EOF

echo "Totale classi: $(find classes/ -name '*.class' | wc -l)" >> td-report.txt
echo "Service classes: $(find classes/ -path '*service*' -name '*.class' | wc -l)" >> td-report.txt
echo "DAO/Repository: $(find classes/ -path '*dao*' -o -path '*repository*' -name '*.class' | wc -l)" >> td-report.txt
echo "Entities: $(find classes/ -name '*Entity.class' -o -path '*model*' -name '*.class' | wc -l)" >> td-report.txt
echo "Package depth: $(find classes/ -type d | grep 'it/unisa' | wc -l)" >> td-report.txt
```

### Python Script per Call Graph Analysis
1. Assicurarsi di avere Python 3 installato.
2. Installare le dipendenze necessarie:
```bash
pip install networkx matplotlib
```
3. Eseguire lo script Python per generare il call graph:
```bash
python3 service-dao-graph.py
```
4. Il grafico verrà salvato come `service_dao_graph.png`.

### Query Logs Analysis
1. Aggiungere logging in persistence.xml per la visione di tutti i dettagli relativi al DB (EL FINE).
2. Aggiungere i logging anche in logging.properties per forzare la visione della duration (solo persistence non sbaglia)

## Statistiche
Dopo aver eseguito i tool, possiamo raccogliere alcune statistiche interessanti riguardanti il technical debt del progetto analizzato:
**=== TECHNICAL DEBT METRICHE ===**

Totale classi: 80
Service classes (debt design): 40
DAO/Repository (debt data): 26
Persistence entities: 14
Package depth: 23

**=== Call Graph Analysis (Python) ===**

Found 40 services, 26 DAOs/Repositories
Average real DAO links per service: 1.62
Max real DAO links for a service: 2
Number of services with max links (too coupled): 26
È presente un file output .png che rappresenta il grafo delle chiamate tra services e DAOs/Repositories. Esso rileva **alto accoppiamento**, poca manutenibilità e, di conseguenza, alto tech debt.

**=== Query Logs Analysis ===**

completeProfileExecution è un file che contiene tutte le query eseguite con il relativo tempo di esecuzione, creazione oggetti java a partire dagli statement, profiling, etc.
Nonostante il technical debt presente a livello di design, l'analisi delle query log mostra che la maggior parte delle query viene eseguita in **tempi** **ottimali**, specialmente nelle query core. **Le uniche eccezioni sono presenti nello start-up del progetto**, dove alcune query di inizializzazione richiedono più tempo del previsto (la prima query richiede **18ms**, mentre la create dei messaggi è **20ms**, con i successivi statements che si attestano intorno ai 2-3ms). Questo suggerisce che, nonostante il debito tecnico a livello di progettazione, le **performance del database sono comunque accettabili** per la maggior parte delle operazioni quotidiane.
L'ottimalità è garantita (in parte) anche grazie alla correttezza della codifica delle query parametriche in Java, le entità ben modellate e l'ottimizzazione delle relazioni tra le tabelle del database e le chiamate del service layer.

**Analisi Manuale della Normalizzazione**

Lo schema sarebbe 3NF, escludendo la dipendenza transitiva topic -> corso -> corsolaurea per definire la relazione tra topic e corsolaurea.
Tuttavia, la presenza di campi ridondanti come nomeCorso e nomeCorsoLaurea nelle tabelle Topic e Corso può indicare una violazione della 3NF, suggerendo che lo schema potrebbe essere considerato in 2NF. Questa ridondanza può portare a problemi di consistenza dei dati e aumentare il rischio di anomalie durante le operazioni di aggiornamento, inserimento o eliminazione.
In conclusione, lo schema del database presenta alcune caratteristiche della 3NF, ma la presenza di campi ridondanti suggerisce che potrebbe essere più accuratamente classificato come in 2NF.

# Conclusioni
L'analisi del technical debt di un progetto software è fondamentale per garantire la sostenibilità a lungo termine del sistema. Utilizzando tool come FIND, Call Graph Analysis e Query Logs Analysis e studiando la propria base di dati, è possibile identificare aree problematiche e adottare strategie per ridurre il debito tecnico, migliorando così la qualità del software e la soddisfazione degli utenti finali.

Il progetto in questione è definito da un debito tecnico presente a livello di design architetturale, con un alto accoppiamento tra i servizi e i DAO/Repository, mentre le performance della base di dati risultano essere ottimali nella maggior parte delle operazioni quotidiane. Tuttavia, è importante affrontare il debito tecnico identificato per migliorare la manutenibilità e la scalabilità del sistema nel lungo termine.