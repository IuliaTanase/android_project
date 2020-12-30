package com.example.proiect.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proiect.AddApartmentActivity;
import com.example.proiect.MainActivity;
import com.example.proiect.R;
import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.network.HttpManager;
import com.example.proiect.adapters.ApartmentAdapter;
import com.example.proiect.utils.Apartment;
import com.example.proiect.utils.ApartmentJsonParser;
import com.example.proiect.utils.Tenant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class ApartmentsFragment extends Fragment {

    public static final String APARTMENTS_LIST_LENGTH = "apartmentsList_length";
    public static final String TENANTS_LIST_LENGTH = "tenantsList_length";
    private static String APARTMENTS_KEY = "apartments_key";
    private static String TENANTS_KEY = "tenants_key";
    private static int NEW_APARTMENT_REQUEST_CODE = 210;

    private FloatingActionButton btnAddApartment;
    Animation grow;
    private CountDownTimer timer;

    private ListView lvApartments;
    private List<Apartment> apartments = new ArrayList<>();
    private List<Tenant> tenants = new ArrayList<>();

    private SharedPreferences themePreferences;

    public ApartmentsFragment() {}

    public static final String APARTMENTS_ADDED_URL = "https://jsonkeeper.com/b/ZI6F";
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    public static ApartmentsFragment newInstance(ArrayList<Apartment> apartments, ArrayList<Tenant> tenants) {

        ApartmentsFragment fragment = new ApartmentsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(ApartmentsFragment.APARTMENTS_KEY, apartments);
        bundle.putParcelableArrayList(ApartmentsFragment.TENANTS_KEY, tenants);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apartments, container, false);
        initializeComponents(view);
        loadFromPref(view);

        btnAddApartment.setOnClickListener(addAnimation());

        if(apartments.size() == 0) {
            getApartmentsFromHttp();
        }
        return view;
    }

    private void initializeComponents(View view) {

        btnAddApartment = view.findViewById(R.id.androidele_fab_addNewApartment);
        lvApartments = view.findViewById(R.id.androidele_lv_apartments);

        grow = AnimationUtils.loadAnimation(getContext(),R.anim.grow);
        timer = new CountDownTimer(900,1) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {

                Intent intent = new Intent(requireContext().getApplicationContext(), AddApartmentActivity.class);
                intent.putExtra(APARTMENTS_LIST_LENGTH, apartments.size());
                intent.putExtra(TENANTS_LIST_LENGTH, tenants.size());
                startActivityForResult(intent, NEW_APARTMENT_REQUEST_CODE);
            }
        };

        themePreferences = requireContext().getSharedPreferences(MainActivity.THEME_PREF, Context.MODE_PRIVATE);

        if(getArguments() != null) {
            apartments = getArguments().getParcelableArrayList(APARTMENTS_KEY);
            tenants = getArguments().getParcelableArrayList(TENANTS_KEY);
        }

        if(getContext() != null) {
            ApartmentAdapter adapter = new ApartmentAdapter(getContext().getApplicationContext(),
                    R.layout.lv_apartaments_row_layout, apartments, getLayoutInflater());
            lvApartments.setAdapter(adapter);
        }
    }

    private void loadFromPref(View view) {
        boolean switchChecked = themePreferences.getBoolean(MainActivity.CHECKED, false);
        if(switchChecked) {
            view.setBackground(getResources().getDrawable(R.drawable.black_background));
        } else {
            view.setBackground(getResources().getDrawable(R.drawable.app_background));
        }
    }

    private View.OnClickListener addAnimation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddApartment.startAnimation(grow);
                timer.start();
            }
        };
    }

    private void getApartmentsFromHttp() {
        Callable<String> asyncOperation = new HttpManager(APARTMENTS_ADDED_URL);
        Callback<String> mainThreadOperation = receiveApartmentsFromHttp();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
    }

    private Callback<String> receiveApartmentsFromHttp() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                apartments.addAll(ApartmentJsonParser.fromJson(result));
                addTenants(apartments);
                notifyInternalAdapter();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_APARTMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Apartment apartment = (Apartment) data.getParcelableExtra(AddApartmentActivity.NEW_APARTMENT_KEY);
            if(apartment != null) {
                Toast.makeText(requireContext().getApplicationContext(), R.string.new_apartment_received, Toast.LENGTH_LONG).show();
                apartments.add(apartment);
                addTenant(apartment);
                notifyInternalAdapter();
            }
        }
    }

    public void addTenant(Apartment apartment) {
        if(apartment != null) {
            if(!apartment.getTenant().getFullName().equals("-")) {
                Tenant tenant = new Tenant(apartment.getTenant().getFullName(), apartment.getTenant().getPhone());
                tenants.add(tenant);
            }
        }
    }

    private void addTenants(List<Apartment> apartments) {
        for(int i = 0; i < apartments.size(); i++) {
            Tenant apartmentTenant = apartments.get(i).getTenant();
            if(apartmentTenant != null) {
                if(!apartmentTenant.getFullName().equals("-")) {
                    Tenant tenant = new Tenant(apartmentTenant.getFullName(), apartmentTenant.getPhone());
                    tenants.add(tenant);
                }
            }
        }
    }

    public void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvApartments.getAdapter();
        adapter.notifyDataSetChanged();
    }

}