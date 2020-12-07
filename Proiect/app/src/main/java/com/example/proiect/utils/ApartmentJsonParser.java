package com.example.proiect.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApartmentJsonParser {
    public static final String APARTMENT_ID = "id";
    public static final String APARTMENT_TITLE = "apartmentTitle";
    public static final String APARTMENT_NR_OF_ROOMS = "nrOfRooms";
    public static final String APARTMENT_RENT_PER_MONTH = "rentPerMonth";
    public static final String APARTMENT_ADDRESS = "address";
    public static final String APARTMENT_DESCRIPTION = "description";
    public static final String APARTMENT_AVAILABILITY = "availability";
    public static final String APARTMENT_FREE_DATE = "freeDate";
    public static final String APARTMENT_TENANT = "tenant";
    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_FULLNAME = "fullName";
    public static final String TENANT_PHONE = "phone";

    public static DateConverter dateConverter = new DateConverter();

    public static List<Apartment> fromJson(String json) {
        try {
            JSONArray array = new JSONArray(json);
            return readApartments(array);
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<Apartment> readApartments(JSONArray array) throws JSONException {
        List<Apartment> apartments = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Apartment apartment = readApartment(array.getJSONObject(i));
            apartments.add(apartment);
        }
        return apartments;
    }

    private static Apartment readApartment(JSONObject object) throws JSONException {
        int apartmentId = object.getInt(APARTMENT_ID);
        String apartmentTitle = object.getString(APARTMENT_TITLE);
        int nrOfRooms = object.getInt(APARTMENT_NR_OF_ROOMS);
        double rentPerMonth = object.getDouble(APARTMENT_RENT_PER_MONTH);
        String address = object.getString(APARTMENT_ADDRESS);
        String description = object.getString(APARTMENT_DESCRIPTION);
        boolean availability = object.getBoolean(APARTMENT_AVAILABILITY);
        Date freeDate = dateConverter.stringToDate(object.getString(APARTMENT_FREE_DATE));

        JSONObject jsonObject = object.getJSONObject(APARTMENT_TENANT);
        Tenant tenant = new Tenant();
        tenant.setId(jsonObject.getInt(TENANT_ID));
        tenant.setFullName(jsonObject.getString(TENANT_FULLNAME));
        tenant.setPhone(jsonObject.getString(TENANT_PHONE));
        return new Apartment(apartmentId, apartmentTitle, nrOfRooms, rentPerMonth, address, description, availability, freeDate, tenant);
    }
}
