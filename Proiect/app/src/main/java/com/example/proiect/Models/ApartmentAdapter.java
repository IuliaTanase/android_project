package com.example.proiect.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.proiect.R;

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
            //addExpirationTime(view, bankAccount.getExpirationMonth(), bankAccount.getExpirationYear());
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

    private void populateTextViewContent(String value, TextView textView) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_row_default_value);
        }
    }

    private void addApartmentAddress(View view, String address) {
        TextView textView = view.findViewById(R.id.androidele_tv_row_apartment_address);
        populateTextViewContent(address, textView);
    }

//    private void addExpirationTime(View view, int expirationMonth, int expirationYear) {
//        TextView textView = view.findViewById(R.id.tv_row_expiration_time);
//        String value = context.getString(R.string.lv_row_expiration_time_format, expirationMonth, expirationYear);
//        populateTextViewContent(value, textView);
//    }
}
