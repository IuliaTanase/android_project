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

import java.util.List;

public class ApartmentAdapter extends ArrayAdapter<Apartment> {

    private Context context;
    private int resource;
    private List<Apartment> apartments;
    private LayoutInflater inflater;

    public ApartmentAdapter(@NonNull Context context, int resource, @NonNull List<Apartment> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.apartments = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(resource, parent, false);
        Apartment apartment = apartments.get(position);

        if(apartment != null) {
            addApartmentName(view, apartment.getTitle());
            addTenantName(view, apartment.getTenant().getFullName());
            addApartmentAddress(view, apartment.getAddress());
        }
        return view;
    }

    private void addApartmentName(View view, String apartmentTitle) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_apartment_name);
        populateTextViewContent(apartmentTitle, textView);
    }

    private void addTenantName(View view, String tenantName) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_tenant_name);
        populateTextViewContent(tenantName, textView);
    }

    private void addApartmentAddress(View view, String address) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_apartment_address);
        populateTextViewContent(address, textView);
    }

    private void populateTextViewContent(String value, TextView textView) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_default_value);
        }
    }


}
