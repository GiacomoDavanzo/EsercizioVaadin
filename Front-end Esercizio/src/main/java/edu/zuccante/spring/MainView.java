package edu.zuccante.spring;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.time.LocalDate;
import java.util.Arrays;

@Route("")
@PWA(name = "Progetto sostituzioni per i professori", shortName = "Progetto Sostituzioni")

/**
 * TODO:
 *  1. Aggiungere i due pulsanti "Stampa" e "Scarica"
 *  2. Fare in modo che alla pressione del pulsante Scarica si apra una finestra di dialogo con scritto
 *      "Come si desidera scaricare il file?" e due pulsanti "Conferma" e "Annulla"
 *  3. Aggiungere a questa finestra una lista con i formati possibili per scaricare il file (.pdf, .docx)
 */

public class MainView extends VerticalLayout{

    private CustomerService service = CustomerService.getInstance();

    private Grid<Customer> gridTot = new Grid<>();

    private Grid<Customer> gridAbsent = new Grid<>();

    private TextField filterText = new TextField();

    private DatePicker operatingDate = new DatePicker();

    private CustomerForm form = new CustomerForm(this);

    public MainView() {

        gridTot.addColumn(Customer::getLastName).setHeader("Cognome");
        gridTot.addColumn(Customer::getFirstName).setHeader("Nome");
        gridAbsent.addColumn(Customer::getLastName).setHeader("Cognome");
        gridAbsent.addColumn(Customer::getFirstName).setHeader("Nome");

        filterText.setPlaceholder("Filtra per nome...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        form.setCustomer(null);

        VerticalLayout grids = new VerticalLayout(gridTot, gridAbsent);
        HorizontalLayout mainContent = new HorizontalLayout(grids, form);
        HorizontalLayout upper = new HorizontalLayout(filterText, operatingDate);

        mainContent.setSizeFull();

        gridTot.setSizeFull();
        gridAbsent.setSizeFull();
        gridAbsent.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        gridTot.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        operatingDate.setValue(LocalDate.now());
        setItaDatePicker(operatingDate);

        add(upper, mainContent);
        setSizeFull();

        gridTot.asSingleSelect().addValueChangeListener(event ->
                form.setCustomer(gridTot.asSingleSelect().getValue()));
        gridAbsent.asSingleSelect().addValueChangeListener(event ->
                form.setCustomer(gridAbsent.asSingleSelect().getValue()));

        updateList();
    }

    Grid<Customer> getGridTot() {
        return gridTot;
    }

    private void updateList() {
        gridTot.setItems(service.findAll(filterText.getValue()));
        gridAbsent.setItems(service.findAll(filterText.getValue()));
    }

    void setItaDatePicker(DatePicker datePicker){
        datePicker.setI18n(
                new DatePicker.DatePickerI18n()
                        .setClear("Annulla").setToday("Oggi")
                        .setCancel("Annulla").setFirstDayOfWeek(1)
                        .setMonthNames(Arrays.asList("Gennaio", "Febbraio",
                                "Marzo", "Aprile", "Maggio", "Giugno",
                                "Luglio", "Agosto", "Settembre", "Ottobre",
                                "Novembre", "Dicembre"))
                        .setWeekdays(Arrays.asList("Lunedì", "Martedì", "Mercoledì",
                                "Giovedì", "Venerdì", "Sabato", "Domenica"))
                        .setWeekdaysShort(Arrays.asList("Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab")));
    }
}

