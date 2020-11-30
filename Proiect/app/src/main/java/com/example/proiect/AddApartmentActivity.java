package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.proiect.Models.Apartment;
import com.example.proiect.Models.DateConverter;
import com.example.proiect.Models.Tenant;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Date;

public class AddApartmentActivity extends AppCompatActivity {
    private TextInputEditText tietTitle;
    private Spinner spnNrOfRooms;
    private TextInputEditText tietRentPerMonth;
    private TextInputEditText tietAddress;
    private TextInputEditText tietDescription;
    private TextInputLayout tilTenantName;
    private TextInputEditText tietTenantName;
    private RadioGroup rgAvailability;
    private EditText etAvailabilityDate;
    private Button btnAddApartment;

    private DateConverter dateConverter = new DateConverter();
    private Intent intent;
    public static final String NEW_APARTMENT_KEY = "apartment_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);

        intent = getIntent();
        initializeComponentsFromWidgets();
        setAdapterOnSpinner();

        btnAddApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    Apartment apartmentAdded = createApartmentFromWidgets();
                    intent.putExtra(NEW_APARTMENT_KEY, apartmentAdded);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        rgAvailability.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.androidele_rb_occupied) {
                    tilTenantName.setVisibility(View.VISIBLE);
                } else {
                    tilTenantName.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void initializeComponentsFromWidgets() {
        tietTitle = findViewById(R.id.androidele_tiet_apartmentTitle);
        spnNrOfRooms = findViewById(R.id.androidele_spn_NrOfRooms);
        tietRentPerMonth = findViewById(R.id.androidele_tiet_rentPerMonth);
        tietAddress = findViewById(R.id.androidele_tiet_apartmentAddress);
        tietDescription = findViewById(R.id.androidele_tiet_apartmentDescription);
        tilTenantName = findViewById(R.id.androidele_til_apartmentTenant);
        tietTenantName = findViewById(R.id.androidele_tiet_apartmentTenant);
        rgAvailability = findViewById(R.id.androidele_rg_availability);
        etAvailabilityDate = findViewById(R.id.androidele_etDate);
        btnAddApartment = findViewById(R.id.androidele_btnAddApartment);
    }

    public void setAdapterOnSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.apartment_nr_of_rooms, android.R.layout.simple_spinner_dropdown_item);
        spnNrOfRooms.setAdapter(adapter);
    }

    private boolean validate() {
        if(tietTitle.getText() == null || tietTitle.getText().toString().isEmpty()) {
            tietTitle.setError(getString(R.string.androidele_Title_required));
            tietTitle.requestFocus();
            return false;
        }

        if(tietRentPerMonth.getText() == null || Double.parseDouble(tietRentPerMonth.getText().toString().trim()) < 0) {
            tietRentPerMonth.setError(getString(R.string.androidele_incorrect_rent_field));
            tietRentPerMonth.requestFocus();
            return false;
        }

        if(etAvailabilityDate.getText() == null || dateConverter.stringToDate(etAvailabilityDate.getText().toString().trim()) == null) {
            etAvailabilityDate.setError(getString(R.string.androidele_invalid_format_availabilityDate));
            etAvailabilityDate.requestFocus();
            return false;
        }
        return true;
    }

    private Apartment createApartmentFromWidgets() {
        String title = tietTitle.getText().toString().trim();
        int nrOfRooms = Integer.parseInt(spnNrOfRooms.getSelectedItem().toString());
        double rentPerMonth = Double.parseDouble(tietRentPerMonth.getText().toString());
        String address = tietAddress.getText().toString();
        String description = tietDescription.getText().toString();
        boolean availability = true;
        if(rgAvailability.getCheckedRadioButtonId() == R.id.androidele_rb_occupied) {
            availability = false;
        }
        Date availabilityDate = dateConverter.stringToDate(etAvailabilityDate.getText().toString().trim());

        Tenant tenant;
        if(rgAvailability.getCheckedRadioButtonId() == R.id.androidele_rb_occupied) {
            String tenantName = tietTenantName.getText().toString();
            tenant = new Tenant(tenantName);
        } else {
            tenant = new Tenant();
        }
        return new Apartment(title, nrOfRooms, rentPerMonth, address, description, availability, availabilityDate, tenant);

    }
}