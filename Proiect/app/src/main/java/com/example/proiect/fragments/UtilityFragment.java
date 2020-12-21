package com.example.proiect.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proiect.AddUtilityActivity;
import com.example.proiect.R;
import com.example.proiect.adapters.UtilityAdapter;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.models.Utility;
import com.example.proiect.database.service.UtilityService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class UtilityFragment extends Fragment {

    public final static int CODE_ADD_UTILITY = 202;
    public final static int CODE_UPDATE_UTILITY=203;
    private Spinner spinnerProviders;
    private ListView lv_utilities;
    private Button addUtility;
    private List<Utility> utilities = new ArrayList<>();
    private UtilityService utilityService;

    public UtilityFragment() {
        // Required empty public constructor
    }


    public static UtilityFragment newInstance() {
        UtilityFragment fragment = new UtilityFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utility, container, false);
        utilityService = new UtilityService(view.getContext());
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        lv_utilities = view.findViewById(R.id.androidele_lvUtilities);
        addUtility = view.findViewById(R.id.androidele_btnAddUtility);
        spinnerProviders = view.findViewById(R.id.androidele_spinnerUtilitiesProviders);
        addAdapterProviders();
        addAdapterUtilities();
        spinnerProviders.setOnItemSelectedListener(selectItemSpinner());
        addUtility.setOnClickListener(goToAddActivity(view));
        lv_utilities.setOnItemClickListener(updateUtilityClick());

    }

    private AdapterView.OnItemClickListener updateUtilityClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getContext(),AddUtilityActivity.class);
                i.putExtra(AddUtilityActivity.UTILITY_KEY,utilities.get(position));
                startActivityForResult(i,CODE_UPDATE_UTILITY);
            }
        };
    }


    private void addAdapterUtilities() {
        UtilityAdapter utilityAdapter = new UtilityAdapter(getContext(),R.layout.lv_utilities_row,utilities,getLayoutInflater(),utilityService);
        lv_utilities.setAdapter(utilityAdapter);
    }


    private View.OnClickListener goToAddActivity(View view) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), AddUtilityActivity.class);
                startActivityForResult(i,202);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data !=null){
            Utility utility = (Utility) data.getSerializableExtra(AddUtilityActivity.UTILITY_KEY);
            if(utility != null) {
                if (requestCode == CODE_ADD_UTILITY) {
                    Toast.makeText(getContext(), "A new utility was added", Toast.LENGTH_SHORT).show();
                    utilityService.insert(insertUtilityIntoDB(), utility);
                }else
                {
                    Toast.makeText(getContext(),"The utility has been updated",Toast.LENGTH_SHORT).show();
                    utilityService.update(updateUtilityFromDB(),utility);
                }
            }
        }
    }

    private Callback<Utility> updateUtilityFromDB() {
        return new Callback<Utility>() {
            @Override
            public void runResultOnUiThread(Utility result) {
                if(result!=null){
                    for(Utility u : utilities){
                        if(u.getIdUtility() == result.getIdUtility()){
                            u.setName(result.getName());
                            u.setProvider(result.getProvider());
                            break;
                        }
                    }
                    notifyAdapterUtilities();
                }
            }
        };
    }

    private Callback<Utility> insertUtilityIntoDB() {
        return new Callback<Utility>() {
            @Override
            public void runResultOnUiThread(Utility result) {
                if(result!= null){
                    utilities.add(result);
                    notifyAdapterUtilities();
                }
            }
        };
    }

    private void notifyAdapterUtilities() {
        UtilityAdapter adapter = (UtilityAdapter)lv_utilities.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemSelectedListener selectItemSpinner() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String providerName = spinnerProviders.getItemAtPosition(position).toString();

                if(providerName.equalsIgnoreCase("ALL")){
                    utilityService.getAll(getAllFromDb());
                }else{
                    utilityService.getAllFromProvider(getAllFromDBbasedOnProvider(),providerName);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private Callback<List<Utility>> getAllFromDb() {
        return new Callback<List<Utility>>() {
            @Override
            public void runResultOnUiThread(List<Utility> result) {
                utilities.clear();
                if(result!=null){
                    utilities.addAll(result);
                }
                notifyAdapterUtilities();
            }
        };
    }

    private Callback<List<Utility>> getAllFromDBbasedOnProvider() {
        return new Callback<List<Utility>>() {
            @Override
            public void runResultOnUiThread(List<Utility> result) {
                // am pus inainte de verificarea daca este null deoarece vrem sa apara listview-ul gol in cazul in care
                // la respectivul provider nu exista utilitati in baza de date
                utilities.clear();

                if(result!=null){
                    utilities.addAll(result);
                }
                notifyAdapterUtilities();
            }
        };
    }


    private void addAdapterProviders() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.providers,R.layout.support_simple_spinner_dropdown_item);
        spinnerProviders.setAdapter(adapter);
    }
}