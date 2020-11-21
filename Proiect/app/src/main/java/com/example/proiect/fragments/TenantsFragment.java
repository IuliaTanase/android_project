package com.example.proiect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proiect.Models.Apartment;
import com.example.proiect.Models.Tenant;
import com.example.proiect.Models.TenantAdapter;
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

    public TenantsFragment() {}

    public static TenantsFragment newInstance(ArrayList<Tenant> tenants,ArrayList<Apartment> apartments) {
        TenantsFragment fragment = new TenantsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(TenantsFragment.TENANTS_KEY, tenants);
        bundle.putParcelableArrayList(TenantsFragment.APARTMENTS_KEY,apartments);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tenants, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        lvTenants = view.findViewById(R.id.androidele_lv_tenants);

        if (getArguments() != null) {
            tenants = getArguments().getParcelableArrayList(TENANTS_KEY);
            System.out.println( "getArgument tenants " + tenants.get(0).getFullName());
            apartments = getArguments().getParcelableArrayList(APARTMENTS_KEY);
            System.out.println( "getArgument apartments " + apartments.get(0).getTitle());
        }

        if(getContext() != null) {
            TenantAdapter tenantAdapter = new TenantAdapter(getContext().getApplicationContext(),
                    R.layout.row_tentant, tenants,apartments,getLayoutInflater());
            lvTenants.setAdapter(tenantAdapter);
        }
    }

    private void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvTenants.getAdapter();
        adapter.notifyDataSetChanged();
    }
}