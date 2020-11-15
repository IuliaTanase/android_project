package com.example.proiect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proiect.Models.Tenant;
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

    private ListView lvTenants;
    private List<Tenant> tenants = new ArrayList<>();

    public TenantsFragment() {}

    public static TenantsFragment newInstance(ArrayList<Tenant> tenants) {
        TenantsFragment fragment = new TenantsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TenantsFragment.TENANTS_KEY, tenants);
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
            Log.i("TenantsFragment", tenants.toString());
        }

        if(getContext() != null) {
            ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                    android.R.layout.simple_list_item_1, tenants);
            lvTenants.setAdapter(adapter);
        }
    }

    private void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvTenants.getAdapter();
        adapter.notifyDataSetChanged();
    }
}