package com.example.proiect.utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private static final String FORMAT_DATE = "dd/MM/yyyy";
    private final SimpleDateFormat formatter;

    public DateConverter() {
        formatter = new SimpleDateFormat(FORMAT_DATE, Locale.US);
    }

    @TypeConverter
    public Date stringToDate(String value) {
        try {
            return formatter.parse(value);
        } catch(ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public String dateToString(Date value) {
        if(value == null) {
            return null;
        }
        return formatter.format(value);
    }
}
