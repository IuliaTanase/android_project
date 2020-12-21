package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proiect.R;
import com.example.proiect.database.models.Utility;

public class AddUtilityActivity extends AppCompatActivity {

    public static final String UTILITY_KEY = "utility_key";

    private Spinner spinnerName;
    private Spinner spinnerProvider;
    private Button btnSave;

    private Intent i;
    private Utility utility=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_utility);
        initialize();
        i = getIntent();
        if(i.hasExtra(UTILITY_KEY)){
            utility = (Utility) i.getSerializableExtra(UTILITY_KEY);
            populateViews();
        }
    }

    private void populateViews() {

        if(utility==null){
            return;
        }

        if(utility.getName().equalsIgnoreCase("Energy")){
            spinnerName.setSelection(1);
        }else if(utility.getName().equalsIgnoreCase("Gas")){
                spinnerName.setSelection(2);
             }else {
            spinnerName.setSelection(3);
        }

        String prov = utility.getProvider();
        switch (prov){
            case "ENEL":{
                spinnerProvider.setSelection(1);
                break;
            }
            case "AQUA NOVA":{
                spinnerProvider.setSelection(2);
                break;
            }
            case "DISTRIGAZ":{
                spinnerProvider.setSelection(3);
                break;
            }
            case "ENGIE":{
                spinnerProvider.setSelection(4);
                break;
            }
            case "APA 2000":{
                spinnerProvider.setSelection(5);
                break;
            }
            default:{
                spinnerProvider.setSelection(0);
                break;
            }

        }


    }

    private void initialize() {
        spinnerName = findViewById(R.id.androidele_spinnerUtilityName);
        ArrayAdapter<CharSequence> adapterName = ArrayAdapter.createFromResource(getApplicationContext(),R.array.utility_names,R.layout.support_simple_spinner_dropdown_item);
        spinnerName.setAdapter(adapterName);
        spinnerProvider = findViewById(R.id.androidele_spinnerUtilityProvider);
        ArrayAdapter<CharSequence> adapterProvider = ArrayAdapter.createFromResource(getApplicationContext(),R.array.providers,R.layout.support_simple_spinner_dropdown_item);
        spinnerProvider.setAdapter(adapterProvider);
        btnSave = findViewById(R.id.androidele_btnSaveUtility);
        btnSave.setOnClickListener(saveUtility());
    }

    private View.OnClickListener saveUtility() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String utilityName = spinnerName.getSelectedItem().toString();
                    String provider = spinnerProvider.getSelectedItem().toString();
                    if (utility == null) {
                        utility = new Utility(utilityName, provider);
                    } else {
                        utility.setName(utilityName);
                        utility.setProvider(provider);
                    }
                    i.putExtra(UTILITY_KEY, utility);
                    setResult(RESULT_OK, i);
                    finish();

                }
            }

        };
    }

    private boolean validate() {
        if(spinnerName.getSelectedItemId()==0){
            Toast.makeText(getApplicationContext(),getString(R.string.cant_select_all),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spinnerProvider.getSelectedItemId()==0){
            Toast.makeText(getApplicationContext(),getString(R.string.cant_select_all),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}