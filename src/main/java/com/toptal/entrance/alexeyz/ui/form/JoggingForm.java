package com.toptal.entrance.alexeyz.ui.form;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JoggingForm extends AbstractForm<Jog> {

    private final DateField date = new MDateField("Date");
    private final TextField distance = new MTextField("Distance");
    private final TextField time = new MTextField("Time");

    public JoggingForm(Jog jog) {
        setSizeUndefined();
        setEntity(jog);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        date,
                        distance,
                        time
                        //new MHorizontalLayout(timeH, timeM, timeS)
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

}
