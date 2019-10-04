package edu.zuccante.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CustomerForm extends FormLayout {

    private TextField firstName = new TextField("Nome");
    private TextField lastName = new TextField("Cognome");

    private Label label = new Label("Durata assenza:");

    private Tab tab1 = new Tab("Un giorno");
    private Div page1 = new Div();
    private Tab tab2 = new Tab("Più giorni");
    private Div page2 = new Div();
    private Tabs tabs = new Tabs(tab1, tab2);
    private Div pages = new Div(page1, page2);

    private Map<Tab, Component> tabsToPages = new HashMap<>();

    private ListBox<String> allDay = new ListBox<>();

    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();

    private Checkbox hours1 = new Checkbox("1°");
    private Checkbox hours2 = new Checkbox("2°");
    private Checkbox hours3 = new Checkbox("3°");
    private Checkbox hours4 = new Checkbox("4°");
    private Checkbox hours5 = new Checkbox("5°");
    private Checkbox hours6 = new Checkbox("6°");

    private Button cancel = new Button("Annulla");
    private Button add = new Button("+ Aggiungi");

    private HorizontalLayout fields = new HorizontalLayout(lastName, firstName);
    private HorizontalLayout hours = new HorizontalLayout(hours1, hours2, hours3, hours4, hours5, hours6);
    private HorizontalLayout buttons = new HorizontalLayout(add, cancel);
    private VerticalLayout right = new VerticalLayout(fields, label, tabs, pages, buttons);
    private HorizontalLayout datePickers = new HorizontalLayout(startDatePicker, endDatePicker);

    private Binder<Customer> binder = new Binder<>(Customer.class);

    private MainView mainView;

    CustomerForm(MainView mainView) {

        this.mainView = mainView;

        hours.setWidth("400px");

        label.setText("Durata assenza:");
        label.setHeight("2px");

        tabs.setWidth("400px");
        tabs.setFlexGrowForEnclosedTabs(1);

        page1.add(allDay, hours);

        page2.add(datePickers);
        page2.setVisible(false);

        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        Set<Component> pagesShown = Stream.of(page1)
                .collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        allDay.setItems("Tutto il giorno", "Qualche ora");
        allDay.setValue("Tutto il giorno");
        allDay.setWidth("400px");
        hours.setVisible(false);
        allDay.addValueChangeListener(event -> showOrHide(event.getValue()));

        startDatePicker.setLabel("Dal:");
        mainView.setItaDatePicker(startDatePicker);
        endDatePicker.setLabel("Al:");
        mainView.setItaDatePicker(endDatePicker);
        startDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if (selectedDate != null) {
                endDatePicker.setMin(selectedDate.plusDays(1));
                if (endDate == null) {
                    endDatePicker.setOpened(true);
                }
            } else {
                endDatePicker.setMin(null);
            }
        });

        endDatePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                startDatePicker.setMax(selectedDate.minusDays(1));
            } else {
                startDatePicker.setMax(null);
            }
        });

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addClickListener(Event -> cancel());

        add(right);

        binder.bindInstanceFields(this);
    }

    void setCustomer(Customer customer) {
        binder.setBean(customer);

        if (customer == null) {
            setVisible(true);
        } else {
            setVisible(true);
            firstName.focus();
        }
    }

    private void showOrHide(String value) {
        if (value.equals("Qualche ora")){
            hours.setVisible(true);
        } else {
            hours.setVisible(false);
        }
    }

    private void uncheckAll() {
        hours2.setValue(false);
        hours1.setValue(false);
        hours3.setValue(false);
        hours4.setValue(false);
        hours5.setValue(false);
        hours6.setValue(false);
    }

    private void cancel() {
        mainView.getGridTot().select(null);
        uncheckAll();
        firstName.setValue("");
        lastName.setValue("");
        tabs.setSelectedIndex(0);
        allDay.setValue("Tutto il giorno");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
}
