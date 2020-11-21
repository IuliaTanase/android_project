package com.example.proiect.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.AddApartmentActivity;
import com.example.proiect.Models.Apartment;
import com.example.proiect.Models.ApartmentAdapter;
import com.example.proiect.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ApartmentsFragment extends Fragment{


    private static String APARTMENTS_KEY = "apartments_key";
    private static int NEW_APARTMENT_REQUEST_CODE = 210;

    private FloatingActionButton btnAddApartment;
    Animation grow;

    private ListView lvApartments;
    private List<Apartment> apartments = new ArrayList<>();

    private CountDownTimer timer;

    public ApartmentsFragment() {}


    public static ApartmentsFragment newInstance(ArrayList<Apartment> apartments) {

        ApartmentsFragment fragment = new ApartmentsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ApartmentsFragment.APARTMENTS_KEY, apartments);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apartments, container, false);
        initializeComponents(view);
        btnAddApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddApartment.startAnimation(grow);
                timer.start();

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_APARTMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Apartment apartment = (Apartment) data.getParcelableExtra(AddApartmentActivity.NEW_APARTMENT_KEY);
            if(apartment != null) {
                Toast.makeText(requireContext().getApplicationContext(), R.string.new_apartment_received, Toast.LENGTH_LONG).show();
                apartments.add(apartment);
                notifyInternalAdapter();
            }

        }
    }

    private void initializeComponents(View view) {

        btnAddApartment = view.findViewById(R.id.androidele_fab_addNewApartment);
        lvApartments = view.findViewById(R.id.androidele_lv_apartments);
        grow = AnimationUtils.loadAnimation(getContext(),R.anim.grow);
        timer = new CountDownTimer(1700,1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(requireContext().getApplicationContext(), AddApartmentActivity.class);
                startActivityForResult(intent, NEW_APARTMENT_REQUEST_CODE);

            }
        };

        if(getArguments() != null) {
            apartments = getArguments().getParcelableArrayList(APARTMENTS_KEY);

            Log.i("ApartmentsFragment", apartments.toString());
        }

        if(getContext() != null) {
           ApartmentAdapter adapter = new ApartmentAdapter(getContext().getApplicationContext(),
                    R.layout.lv_row_layout, apartments, getLayoutInflater());
            lvApartments.setAdapter(adapter);
        }
    }

    public void notifyInternalAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvApartments.getAdapter();
        adapter.notifyDataSetChanged();
    }

}