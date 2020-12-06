package com.example.proiect.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationJsonParser {

    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_CITY = "city";
    public static final String LOCATION_ADDRESS = "address";
    public static final String LOCATION_APARTMENT = "apartment";

    public static final String APARTMENT_ID = "apartment_id";
    public static final String APARTMENT_TITLE = "title";
    public static final String APARTMENT_NR_OF_ROOMS = "nrOfRooms";
    public static final String APARTMENT_RENT = "rent";
    public static final String APARTMENT_TENANT = "tenant";

    public static final String TENANT_ID = "tenant_id";
    public static final String TENANT_FULLNAME = "fullName";
    public static final String TENANT_PHONE = "phone";


    public static List<Location> fromJson(String json) {
        try {
            JSONArray array = new JSONArray(json);
            return readLocations(array);
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<Location> readLocations(JSONArray array) throws JSONException {
        List<Location> locations = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Location location;
            location = readLocation(array.getJSONObject(i));
            locations.add(location);
        }
        return locations;
    }

    private static Location readLocation(JSONObject object) throws JSONException {
        int locationId = object.getInt(LOCATION_ID);
        String city = object.getString(LOCATION_CITY);
        String address = object.getString(LOCATION_ADDRESS);

        JSONObject jsonObjectApartment = object.getJSONObject(LOCATION_APARTMENT);
        Apartment apartment = new Apartment();
        apartment.setId(jsonObjectApartment.getInt(APARTMENT_ID));
        apartment.setTitle(jsonObjectApartment.getString(APARTMENT_TITLE));
        apartment.setNrOfRooms(jsonObjectApartment.getInt(APARTMENT_NR_OF_ROOMS));
        apartment.setRentPerMonth(jsonObjectApartment.getDouble(APARTMENT_RENT));

        JSONObject jsonObjectTenant = jsonObjectApartment.getJSONObject(APARTMENT_TENANT);
        Tenant tenant = new Tenant();
        tenant.setId(jsonObjectTenant.getInt(TENANT_ID));
        tenant.setFullName(jsonObjectTenant.getString(TENANT_FULLNAME));
        tenant.setPhone(jsonObjectTenant.getString(TENANT_PHONE));
        apartment.setTenant(tenant);

        return new Location(locationId, city, address, apartment);
    }
}
