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
import android.widget.Toast;

import com.example.proiect.MainActivity;
import com.example.proiect.R;
import com.example.proiect.adapters.LocationAdapter;
import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.network.HttpManager;
import com.example.proiect.utils.Location;
import com.example.proiect.utils.LocationJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class LocationsFragment extends Fragment {

    private ListView lvLocations;

    private List<Location> locations = new ArrayList<>();
    private static String LOCATIONS_KEY = "locations_key";

    public static final String LOCATIONS_ADDED_URL = "https://jsonkeeper.com/b/E0DA";
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    private SharedPreferences themePreferences;

    public LocationsFragment() { }

    public static LocationsFragment newInstance(ArrayList<Location> locations) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LocationsFragment.LOCATIONS_KEY, locations);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);
        initComp(view);
        loadFromPref(view);

        if(locations.size() == 0) {
            getLocationsFromHttp();
        }

        return view;
    }

    private void initComp(View view) {
        lvLocations = view.findViewById(R.id.androidele_lv_locations);

        themePreferences = requireContext().getSharedPreferences(MainActivity.THEME_PREF, Context.MODE_PRIVATE);

        if(getArguments() != null) {
            locations = getArguments().getParcelableArrayList(LOCATIONS_KEY);
        }

        if(getContext() != null) {
            LocationAdapter adapter = new LocationAdapter(getContext().getApplicationContext(),
                    R.layout.lv_locations_row_layout, locations, getLayoutInflater());
            lvLocations.setAdapter(adapter);
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

    private void getLocationsFromHttp() {
        Callable<String> asyncOperation = new HttpManager(LOCATIONS_ADDED_URL);
        Callback<String> mainThreadOperation = receiveLocationsFromHttp();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
    }

    private Callback<String> receiveLocationsFromHttp() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
                locations.addAll(LocationJsonParser.fromJson(result));
                notifyAdapter();
            }
        };
    }

    private void notifyAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvLocations.getAdapter();
        adapter.notifyDataSetChanged();
    }
}