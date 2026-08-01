# Product Backlog

| Priorità | Item                                       | Descrizione                                                                                                        | Stima Iniziale | Note         |
|:--------:|:-------------------------------------------|:-------------------------------------------------------------------------------------------------------------------|:--------------:|:-------------|
|  **1**   | Setup Infrastruttura e CI/CD               | Configurazione della repository GitHub e creazione della pipeline di CI (GitHub Actions)                           |       5        | Sprint-Ready |
|  **2**   | Ingegneria Requisiti e Architettura        | Stesura del dominio, design architetturale (UML) e setup dei file Markdown per il report finale.                   |       13       | Can be split |
|  **3**   | Modellazione Plancia Personale             | Modellazione tramite ADT (Algebraic Data Types) della plancia di gioco                                             |       13       | Can be split |
|  **4**   | Modellazione Dischi Terreno                | Implementazione dischi terreno presenti nel gioco                                                                  |       8        | Sprint-ready |
|  **5**   | Validazione Piazzamento                    | Sviluppo della logica pura (funzionale) per verificare la correttezza del posizionamento dei dischi sulla plancia. |       13       | Can be split |
|  **6**   | Creazione Sacchetto Token                  | Implementazione del sacchetto di gioco per estrarre token da mettere nell'offerta centrale di gioco                |       8        | Sprint-ready |
|  **7**   | Gestione Offerta di Gioco                  | Logica di estrazione randomica dal sacchetto e popolazione/aggiornamento dell'offerta comune dei dischi.           |       13       | Can be split |
|  **8**   | Game Loop e Turni                          | Orchestrazione dell'alternanza dei turni e aggiornamento dello stato immutabile del gioco.                         |       21       | Can be split |
|  **9**   | Creazione e Pattern Matching Carte Animale | Algoritmo di esplorazione della plancia per individuare le conformazioni di habitat richieste dalle carte.         |       21       | Can be split |
|  **10**  | Motore di Punteggio                        | Sviluppo di funzioni per il calcolo dei punti vittoria a fine partita (habitat completati e animali).              |       13       | Can be split |
|  **11**  | Interfaccia Utente (GUI)                   | Sviluppo dell'interfaccia grafica e collegamento reattivo allo stato del gioco.                                    |       34       | Can be split |
|  **12**  | Carte Spirito della Natura                 | Sviluppo carte spirito della natura                                                                                |       8        | Optional     |