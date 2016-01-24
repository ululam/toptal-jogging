package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.domain.Week;
import com.toptal.entrance.alexeyz.util.UserUtil;
import com.toptal.entrance.alexeyz.util.Utils;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Weekly reports tab
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class WeekTab extends MVerticalLayout {
    private MTable<Week> weeksTable = new MTable<>(Week.class)
            .withProperties("week", "start", "end", "runs", "distance", "time", "averageSpeed")
            .withColumnHeaders("Week Number", "Start Date", "End Date", "Runs", "Total Distance, km", "Total Time", "Average Speed, km/h")
            .setSortableProperties("week")
            .withFullWidth();

    private final MainView view;

    WeekTab(MainView view) {
        this.view = view;
    }

    WeekTab init() {
        addComponent(weeksTable);

        setConverters();

        reloadData();

        return this;
    }

    void reloadData() {
        weeksTable.setBeans(
                view.ui.joggingRepository.getWeeks(
                        UserUtil.currentUser().getId())
        );
    }

    private void setConverters() {
        weeksTable.setConverter("start", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });
        weeksTable.setConverter("end", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });

        weeksTable.setConverter("time", new StringToLongConverter() {
            @Override
            public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.formatHMS(value);
            }
        });

        weeksTable.setConverter("averageSpeed", new StringToFloatConverter() {
            @Override
            public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.format(value);
            }
        });

    }

}
