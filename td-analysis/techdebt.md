# Contesto
In un corso di sostenibilità del software, la key-dimension "Tecnica" non deve di certo mancare lo studio del technical debt.
Secondo gli studi, il Technical Debt (TD) è una metafora che rappresenta il costo implicito di scelte di sviluppo software a breve termine che possono portare a problemi a lungo termine. Queste scelte possono includere l'adozione di soluzioni rapide, la mancanza di documentazione adeguata, la scarsa qualità del codice e la mancanza di test automatizzati.

Di certo ne abbiamo sentito parlare molto durante il corso, ma non ci siamo focalizzati abbastanza su una cosa fondamentale: certi progetti **nascono già *problematici***. In effetti, il technical debt non è semplicemente dato dall'uso di design pattern o pratiche di sviluppo non ottimali, bensì dalla mancata cura nella progettazione della propria base di dati, di come avvengono le chiamate, della scalabilità del sistema etc.

# Tools
Per analizzare il technical debt di un progetto software, esistono diversi tool che possono essere utilizzati per identificare e quantificare il debito tecnico presente nel codice (relativo alla progettazione). Alcuni dei tool più comuni includono:
- TODO

- **pgAdmin/Explain Analyze**: strumenti per analizzare le prestazioni e la struttura di database PostgreSQL, utili per identificare problemi di progettazione del database che possono contribuire al technical debt.

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

## Statistiche
Dopo aver eseguito i tool, possiamo raccogliere alcune statistiche interessanti riguardanti il technical debt del progetto analizzato:
=== TECHNICAL DEBT METRICHE ===
Totale classi: 80
Service classes (debt design): 40
DAO/Repository (debt data): 26
Persistence entities: 14
Package depth: 23

=== Call Graph Analysis (Python) ===
Found 40 services, 26 DAOs/Repositories
Average real DAO links per service: 1.62
Max real DAO links for a service: 2
Number of services with max links (too coupled): 26