# EsercizioVaadin
Repo che contiene i materiali necessari a svolgere l'esercizio su Vaadin

Link utile:
[Vaadin Components](https://vaadin.com/components)

## Consegne
1. Aggiungere i due pulsanti "Stampa" e "Scarica"
2. Fare in modo che alla pressione del pulsante Scarica si apra una finestra di dialogo con scritto
  "Come si desidera scaricare il file?" e due pulsanti "Conferma" e "Annulla"
3. Aggiungere a questa finestra una lista con i formati possibili per scaricare il file (.pdf, .docx)

## Tips and Tricks
* Attenzione, nella inizializzazione degli oggetti ad importare le classi di Vaadin e non quelle di Java
* I tre componenti da utilizzare sono:
  * [Button](https://vaadin.com/components/vaadin-button)
  * [Dialog](https://vaadin.com/components/vaadin-dialog)
  * [ListBox](https://vaadin.com/components/vaadin-list-box)
* Per il componente Button il metodo per rilevare il click .addClickListener(...) è cambiato, e sulle pagine di Vaadin è riportato quello più recente, quello che useremo noi è .addClickListener(event -> {comandi});
