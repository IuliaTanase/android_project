package com.example.proiect.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proiect.MainActivity;
import com.example.proiect.utils.Apartment;
import com.example.proiect.utils.Tenant;
import com.example.proiect.adapters.TenantAdapter;
import com.example.proiect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantsFragment extends Fragment {

    private static String TENANTS_KEY = "tenants_key";
    private static String APARTMENTS_KEY = "apartment_key";

    private ListView lvTenants;
    private List<Tenant> tenants = new ArrayList<>();
    private List<Apartment> apartments;

    private SharedPreferences preferences;

    int entries = 0;
    public TenantsFragment() {}

    public static TenantsFragment newInstance(ArrayList<Tenant> tenants, ArrayList<Apartment> apartments) {
        TenantsFragment fragment = new TenantsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(TenantsFragment.TENANTS_KEY, tenants);
        bundle.putParcelableArrayList(TenantsFragment.APARTMENTS_KEY, apartments);

        System.out.println(tenants);
        System.out.println(apartments);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tenants, container, false);
        initializeComponents(view);
        loadFromPref(view);
        notifyInternalAdapter();
        return view;
    }

    private void loadFromPref(View view) {
        boolean switchChecked = preferences.getBoolean(MainActivity.CHECKED, false);
        if(switchChecked) {
            view.setBackground(getResources().getDrawable(R.drawable.black_background));
        } else {
            view.setBackground(getResources().getDrawable(R.drawable.app_background));
        }
    }

    private void initializeComponents(View view) {
        lvTenants = view.findViewById(R.id.androidele_lv_tenants);

        preferences = requireContext().getSharedPreferences(MainActivity.THEME_PREF, Context.MODE_PRIVATE);

        if (getArguments() != null) {
            tenants = getArguments().getParcelableArrayList(TENANTS_KEY);
            apartments = getArguments().getParcelableArrayList(APARTMENTS_KEY);
        }

        if(getContext() != null) {
            TenantAdapter tenantAdapter = new TenantAdapter(getContext().getApplicationContext(),
                    R.layout.lv_tenants_row_layout, tenants, apartments, getLayoutInflater());
            lvTenants.setAdapter(tenantAdapter);

        }
    }

//    private void addTenants(List<Apartment> apartments) {
//        for(int i = 0; i < apartments.size(); i++) {
//            Tenant apartmentTenant = apartments.get(i).getTenant();
//            if( apartmentTenant != null) {
//                Tenant tenant = new Tenant(apartmentTenant.getFullName(), apartmentTenant.getPhone());
//                tenants.add(tenant);
//            }
//        }
//
//    }

    public void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvTenants.getAdapter();
        adapter.notifyDataSetChanged();
    }
}