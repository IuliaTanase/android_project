package com.example.proiect.utils;

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

public class TenantAdapter extends ArrayAdapter<Tenant> {

    private Context ctx;
    private List<Tenant> tenants;
    private List<Apartment> apartments;
    private LayoutInflater inflater;
    private int res;

    public TenantAdapter(@NonNull Context context, int resource, @NonNull List<Tenant> objects, List<Apartment> apartments, LayoutInflater inflater) {
        super(context, resource, objects);
        ctx = context;
        tenants = objects;
        this.apartments = apartments;
        res = resource;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(res, parent,false);
        TextView tv = view.findViewById(R.id.androidele_tv_phoneTenant);

//        Tenant t = tenants.get(position);
//        Apartment a = apartments.stream().filter(ap -> ap.getTenant().getId() == t.getId()).findFirst().get();
//
//        if(t != null){
//            addFullName(view,t.getFullName());
//            addPhone(view,t.getPhone());
//            addApartmentTitle(view,a.getTitle());
//        }
//        return  view;

//        for(int i = 0; i < apartments.size(); i++) {
        //TODO - la adaugare nu populeaza bine adapter??
            Tenant tenant = apartments.get(position).getTenant();
            if(tenant != null) {
                if(!tenant.getFullName().equals("-")) {
                    addFullName(view, tenant.getFullName());
                    addPhone(view, tenant.getPhone());
                    addApartmentTitle(view, apartments.get(position).getTitle());
                }
            }
//        }
        return  view;
    }

    private void addApartmentTitle(View view, String title) {
        TextView tv = view.findViewById(R.id.androidele_tv_apartmentTitleTenant);
        setTextViewTexts(tv, title);
    }

    private void addPhone(View view, String phone) {
        TextView tv = view.findViewById(R.id.androidele_tv_phoneTenant);
         setTextViewTexts(tv, phone);
    }

    private void addFullName(View view, String fullName) {
        TextView tv = view.findViewById(R.id.androidele_tv_FullNameTenant);
        setTextViewTexts(tv, fullName);
    }

    private void setTextViewTexts(TextView tv, String val) {
        if(val != null && !val.trim().isEmpty()){
            tv.setText(val);
        } else {
            tv.setText(R.string.androidele_nothingHere);
        }
    }
}
