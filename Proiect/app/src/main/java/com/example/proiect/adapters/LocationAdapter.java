package com.example.proiect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiect.R;
import com.example.proiect.utils.Apartment;
import com.example.proiect.utils.Location;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    private Context context;
    private int resource;
    private List<Location> locations;
    private LayoutInflater inflater;

    public LocationAdapter(@NonNull Context context, int resource, @NonNull List<Location> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.locations = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(resource, parent, false);
        Location location = locations.get(position);

        if(location != null) {
            addLocationCity(view, location.getCity());
            addLocationAddress(view, location.getAddress());
            addLocationApartment(view, location.getApartment().getTitle());
            addLocationPrice(view, location.getApartment().getRentPerMonth());
            addLocationTenant(view, location.getApartment().getTenant().getFullName());
        }
        return view;
    }

    private void addLocationCity(View view, String locationCity) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_location_city);
        populateTextViewContent(locationCity, textView);
    }

    private void addLocationAddress(View view, String locationAddress) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_location_address);
        populateTextViewContent(locationAddress, textView);
    }

    private void addLocationApartment(View view, String apartmentTitle) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_location_apartmentTitle);
        populateTextViewContent(apartmentTitle, textView);
    }

    private void addLocationPrice(View view, double price) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_location_price);
        populateTextViewContent(String.valueOf(price), textView);
    }

    private void addLocationTenant(View view, String tenantName) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_location_tenantName);
        populateTextViewContent(tenantName, textView);
    }

    private void populateTextViewContent(String value, TextView textView) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_default_value);
        }
    }

}
