package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.ui.form.JoggingForm;
import com.toptal.entrance.alexeyz.util.Utils;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author alexey.zakharchenko@gmail.com
 */
class AdminJoggingTab extends MVerticalLayout {
    private MTable<Jog> joggingTable = new MTable<>(Jog.class)
            .withProperties("userId", "date", "distance", "time", "averageSpeed")
            .withColumnHeaders("UserId", "Date", "Distance, km", "Time", "Average Speed, km/h")
            .setSortableProperties("date", "userId")
            .withFullWidth();

    private Button editButton = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button deleteButton = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this entry?", this::remove);

    private DateField fromDateField = new MDateField("From").withIcon(FontAwesome.ARROW_LEFT);
    private DateField toDateField = new MDateField("To").withIcon(FontAwesome.ARROW_RIGHT);;

    private final MainView view;

    AdminJoggingTab(MainView view) {
        this.view = view;
    }

    AdminJoggingTab init() {
        addComponent(new MHorizontalLayout(editButton, deleteButton, fromDateField, toDateField));
        addComponent(joggingTable);
        expand(joggingTable);

        setConverters();

        fromDateField.addValueChangeListener(this::datesChanged);
        toDateField.addValueChangeListener(this::datesChanged);

        joggingTable.addMValueChangeListener(e -> adjustActionButtonState());

        reloadData();

        return this;
    }

    private void setConverters() {
        joggingTable.setConverter("date", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });

        joggingTable.setConverter("time", new StringToLongConverter() {
            @Override
            public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.formatHMS(value);
            }
        });

        joggingTable.setConverter("averageSpeed", new StringToFloatConverter() {
            @Override
            public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.format(value);
            }
        });
   }

    protected void adjustActionButtonState() {
        boolean hasSelection = joggingTable.getValue() != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    void reloadData() {
        Date fromDate = fromDateField.getValue();
        if (fromDate == null)
            fromDate = new Date(0);

        Date toDate = toDateField.getValue();
        if (toDate == null)
            toDate = new Date();

        joggingTable.setBeans(view.ui.joggingRepository.findAllWithParameters(fromDate, toDate));

        adjustActionButtonState();
    }

    private void datesChanged(Property.ValueChangeEvent vce) {
        reloadData();
    }

    private void edit(Button.ClickEvent e) {
        edit(joggingTable.getValue());
    }

    private void remove(Button.ClickEvent e) {
        if (joggingTable.getValue() != null) {
            view.ui.joggingRepository.delete(joggingTable.getValue().getId());
            joggingTable.setValue(null);
            view.onJogChange();
        }
    }

    private void edit(Jog entry) {
        if (entry == null)
            return;

        JoggingForm form = new JoggingForm(entry);
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);

        form.openInModalPopup();
    }

    private void saveEntry(Jog entry) {
        view.ui.joggingRepository.save(entry);
        view.onJogChange();
        view.ui.closeWindow();
    }

    private void resetEntry(Jog entry) {
        view.onJogChange();
        view.ui.closeWindow();
    }

}