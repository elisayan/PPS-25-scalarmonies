# Processo di Sviluppo
Il team ha adottato un processo di sviluppo ispirato alla metodologia Scrum, con sprint settimanali e backlog aggiornato a ogni iterazione.
Lo sviluppo del codice segue l'approccio Test-Driven Development (TDD), seguendo il ciclo Red-Green Refactor su incrementi per le logiche core del dominio.

## Organizzazione del Team
Il gruppo è composto da 3 persone, suddivisi nei seguenti ruoli:
- Elisa Yan - Product Owner & Sviluppatrice
- Filippo Ferretti - Scrum Master & Sviluppatore
- Oluwatobi Daniel Ariyo - Sviluppatore

## Sprint Planning
Il progetto sarà organizzato in sprint della durata di una settimana. 
Durante ogni sprint il team svolgerà brevi meeting giornalieri, sia da remoto sia in presenza, per monitorare l’avanzamento delle attività e coordinare il lavoro. 
Al termine di ogni sprint verrà effettuato un meeting conclusivo per valutare i risultati raggiunti e definire lo sprint backlog della settimana successiva.

## Task Management
Per la gestione e il tracciamento delle attività il team utilizzerà GitHub Projects, organizzato in una board con le seguenti colonne:
- **Product Backlog Items**: contiene le funzionalità individuate durante la pianificazione, non ancora scomposte in task operativi.
- **Tasks**: contiene i task derivati dai backlog item, suddivisi e assegnati ai membri del team durante lo sprint planning.
- **In Progress**: raccoglie i task attualmente in lavorazione da parte di uno o più membri del team.
- **Done**: raccoglie i task completati e verificati, pronti per essere considerati parte dell'incremento di sprint.

## Definition of Done
Un task si considera completo quando soddisfa tutti i seguenti punti:
- Codice integrato nel main branch tramite pull request
- Code coverage minima del 70% per il model, 60% per le altre componenti
- Documentazione Scaladoc presente dove necessaria
- Per la view: ogni componente deve essere collegato al model e al controller e che non siano presenti componenti non funzionanti

## Documentazione
La documentazione sarà realizzata in formato Markdown, raccolta nella cartella docs/ del repository e pubblicata tramite GitHub Pages.

## Versionamento / Git Workflow
Il team adotta un workflow basato su tre tipologie di branch:
- main: contiene le release
- develop: rappresenta la linea principale di sviluppo, in cui vengono integrate le feature completate
- feature/*: un branch dedicato per ogni funzionalità
- docs/*: un branch dedicato per ogni sezione della documentazione

Ogni pull request deve essere revisionata e approvata da un membro del team diverso da chi ha effettuato il push, per garantire un controllo incrociato sulla qualità del codice.

## CI/CD Pipeline e Automazione della Build
Per automatizzare la verifica del codice e la qualità del software è stato utilizzato **GitHub Actions** integrato con lo strumento di build **SBT (Scala Build Tool)**:
- **Continuous Integration (CI):** A ogni *push* o apertura di *Pull Request* verso i branch `main` e `develop`, un workflow automatizzato compila il progetto, esegue l'intera suite di test automatizzati (**ScalaTest / JUnit**) e verifica l'assenza di regressioni.
- **Controllo di Qualità (Linting & Formatting):** Nel ciclo di build sono integrati **scalafmt** per la verifica della formattazione standard del codice Scala 3 e **scalafix** per l'analisi statica e la prevenzione dei code smell (es. rimozione di import inutilizzati e sintassi sconsigliate).
- **Continuous Delivery (Documentation & Release):** La documentazione Markdown in `docs/` viene costantemente pubblicata tramite GitHub Pages, mentre all'atto della release il sistema produce i pacchetti JAR eseguibili dell'applicazione.