package com.toptal.entrance.alexeyz.ui.form;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;
import java.util.Locale;

/**
 * Add/edit Jog
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class JoggingForm extends AbstractForm<Jog> {

    private final DateField date = new MDateField("Date");
    private final TextField distance = new MTextField("Distance, km")
            .withInputPrompt("Distance in kilometers");
    private final TextField time = new TextField("Time, minutes");


    public JoggingForm(Jog jog) {

        setSizeUndefined();
        setEntity(jog);

        distance.setConverter(new StringToFloatConverter() {
            @Override
            protected Number convertToNumber(String value, Class<? extends Number> targetType, Locale locale) throws ConversionException {
                try {
                    return Float.valueOf(value);
                } catch (Exception e) { return 0; }
            }
            @Override
            public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return String.format("%.1f", value);
            }
        });

        time.setConverter(new StringToLongConverter() {
            @Override
            protected Number convertToNumber(String value, Class<? extends Number> targetType, Locale locale) throws ConversionException {
                try {
                    String[] data = value.split(":");
                    int mins = Integer.parseInt(data[0]);
                    int secs = 0;
                    if (data.length > 1)
                        secs = Integer.parseInt(data[1]);

                    return (mins*60 + secs) * 1000;

                } catch (Exception e) { return 0; }

            }

            @Override
            public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                long mins = value / 60_000;
                long secs = (value - mins*60_000) / 1000;

                return String.format("%02d:%02d", mins, secs);
            }
        });

        date.setRangeEnd(new Date());
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        date,
                        distance,
                        time
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

}
