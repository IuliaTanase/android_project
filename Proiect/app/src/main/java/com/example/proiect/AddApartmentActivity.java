package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.proiect.fragments.ApartmentsFragment;
import com.example.proiect.utils.Apartment;
import com.example.proiect.utils.DateConverter;
import com.example.proiect.utils.Tenant;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddApartmentActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView tvAdd;
    private TextView tvNrOfRooms;
    private TextView tvAvailability;
    private TextView tvDate;
    private TextInputEditText tietTitle;
    private TextInputLayout tilTitle;
    private Spinner spnNrOfRooms;
    private TextInputEditText tietRentPerMonth;
    private TextInputLayout tilRentPerMonth;
    private TextInputEditText tietAddress;
    private TextInputLayout tilAddress;
    private TextInputEditText tietDescription;
    private TextInputLayout tilDescription;
    private TextInputEditText tietTenantName;
    private TextInputLayout tilTenantName;
    private TextInputEditText tietTenantPhone;
    private TextInputLayout tilTenantPhone;
    private RadioGroup rgAvailability;
    private EditText etAvailabilityDate;
    private Button btnAddApartment;

    private DateConverter dateConverter = new DateConverter();
    private Intent intent;
    public static final String NEW_APARTMENT_KEY = "apartment_key";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);

        intent = getIntent();
        initializeComponentsFromWidgets();
        setAdapterOnSpinner();
        loadFromPrefs();

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
                    tilTenantPhone.setVisibility(View.VISIBLE);
                } else {
                    tilTenantName.setVisibility(View.INVISIBLE);
                    tilTenantPhone.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void initializeComponentsFromWidgets() {
        scrollView = findViewById(R.id.androidele_scrollView);
        tvAdd = findViewById(R.id.androidele_tvAddApartment);
        tvNrOfRooms = findViewById(R.id.androidele_tv_nrOfRooms);
        tvAvailability = findViewById(R.id.androidele_tv_availability);
        tvDate = findViewById(R.id.androidele_tv_date);

        tietTitle = findViewById(R.id.androidele_tiet_apartmentTitle);
        tilTitle = findViewById(R.id.androidele_til_apartmentTitle);
        spnNrOfRooms = findViewById(R.id.androidele_spn_NrOfRooms);

        tietRentPerMonth = findViewById(R.id.androidele_tiet_rentPerMonth);
        tilRentPerMonth = findViewById(R.id.androidele_til_rentPerMonth);
        tietAddress = findViewById(R.id.androidele_tiet_apartmentAddress);
        tilAddress = findViewById(R.id.androidele_til_apartmentAddress);
        tietDescription = findViewById(R.id.androidele_tiet_apartmentDescription);
        tilDescription = findViewById(R.id.androidele_til_apartmentDescription);

        tilTenantName = findViewById(R.id.androidele_til_apartmentTenant);
        tietTenantName = findViewById(R.id.androidele_tiet_apartmentTenant);

        tilTenantPhone = findViewById(R.id.androidele_til_tenantPhone);
        tietTenantPhone = findViewById(R.id.androidele_tiet_tenantPhone);

        rgAvailability = findViewById(R.id.androidele_rg_availability);
        etAvailabilityDate = findViewById(R.id.androidele_etDate);
        btnAddApartment = findViewById(R.id.androidele_btnAddApartment);

        preferences = getSharedPreferences(MainActivity.THEME_PREF, MODE_PRIVATE);
    }

    public void setAdapterOnSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.apartment_nr_of_rooms, android.R.layout.simple_spinner_dropdown_item);
        spnNrOfRooms.setAdapter(adapter);
    }

    private void loadFromPrefs() {
        boolean switchChecked = preferences.getBoolean(MainActivity.CHECKED, false);
        if(switchChecked) {
           scrollView.setBackground(getResources().getDrawable(R.drawable.black_background));
           setTextViewsColorLight();
        } else {
            scrollView.setBackground(getResources().getDrawable(R.drawable.app_background));
        }
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
        int id = intent.getIntExtra(ApartmentsFragment.APARTMENTS_LIST_LENGTH, 0);
        String title = Objects.requireNonNull(tietTitle.getText()).toString().trim();
        int nrOfRooms = Integer.parseInt(spnNrOfRooms.getSelectedItem().toString());
        double rentPerMonth = Double.parseDouble(Objects.requireNonNull(tietRentPerMonth.getText()).toString());
        String address = Objects.requireNonNull(tietAddress.getText()).toString();
        String description = Objects.requireNonNull(tietDescription.getText()).toString();
        boolean availability = true;
        if(rgAvailability.getCheckedRadioButtonId() == R.id.androidele_rb_occupied) {
            availability = false;
        }
        Date availabilityDate = dateConverter.stringToDate(etAvailabilityDate.getText().toString().trim());

        Tenant tenant;
        if(rgAvailability.getCheckedRadioButtonId() == R.id.androidele_rb_occupied) {
            int tenantId = intent.getIntExtra(ApartmentsFragment.TENANTS_LIST_LENGTH, 0);

            String tenantName = Objects.requireNonNull(tietTenantName.getText()).toString();
            String tenantPhone = Objects.requireNonNull(tietTenantPhone.getText()).toString();
            tenant = new Tenant(tenantId, tenantName, tenantPhone);
        } else {
            tenant = new Tenant();
        }
        return new Apartment(id, title, nrOfRooms, rentPerMonth, address, description, availability, availabilityDate, tenant);

    }

    private void setTextViewsColorLight() {
        tvAdd.setTextColor(getResources().getColor((R.color.colorPrimary)));
        tvNrOfRooms.setTextColor(getResources().getColor((R.color.colorPrimary)));
        tvAvailability.setTextColor(getResources().getColor((R.color.colorPrimary)));
        tvDate.setTextColor(getResources().getColor((R.color.colorPrimary)));
        tilTitle.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        tilDescription.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        tilAddress.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        tilRentPerMonth.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        tilTenantName.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        tilTenantPhone.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        etAvailabilityDate.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        spnNrOfRooms.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
        rgAvailability.setBackgroundColor(getResources().getColor((R.color.colorPrimary)));
    }

}