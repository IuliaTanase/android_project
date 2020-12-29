package com.example.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.database.models.Invoice;
import com.example.proiect.utils.DateConverter;

import java.util.Date;

public class AddInvoiceActivity extends AppCompatActivity {

    public final static String INVOICE_KEY ="invoice_key";
    public final static String UTILITY_KEY ="utility_key";
    private Intent i;
    private Invoice invoice=null;
    private DateConverter dateConverter;

    private ScrollView scrollView;
    private TextView tvAdd;
    private SharedPreferences preferences;
    private EditText etUtilityID;
    private EditText etApartmentName;
    private EditText etDate;
    private EditText etValueInvoice;
    private Button btnSave;
    private long[] ids = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        initialize();
        i=getIntent();
        loadFromPrefs();
        if(i.hasExtra(INVOICE_KEY)){
            invoice = (Invoice) i.getSerializableExtra(INVOICE_KEY);
            populateViews();
        }
        if(i.hasExtra(UTILITY_KEY)){
            ids= i.getLongArrayExtra(UTILITY_KEY);
        }
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

    private void setTextViewsColorLight() {
        tvAdd.setTextColor(getResources().getColor(R.color.colorAccent));
        etApartmentName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        etUtilityID.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        etDate.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        etValueInvoice.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void populateViews() {
        if(invoice==null){
            return;
        }

        etApartmentName.setText(invoice.getApartmentName());
        etDate.setText(dateConverter.dateToString(invoice.getDate()));
        etValueInvoice.setText(String.valueOf(invoice.getSum()));
        etUtilityID.setText(String.valueOf(invoice.getIdUtility()));
    }

    private void initialize() {
        scrollView = findViewById(R.id.androidele_scrollView);
        tvAdd = findViewById(R.id.androidele_tvAddInvoice);
        dateConverter = new DateConverter();
        etApartmentName = findViewById(R.id.androidele_etApartmentTitleInvoicesAdd);
        etDate = findViewById(R.id.androidele_etDateAddInvoices);
        etUtilityID = findViewById(R.id.androidele_etUtilityNameAddInvoice);
        etValueInvoice=findViewById(R.id.androidele_etValueAddInvoice);
        ArrayAdapter<CharSequence> adapterUtilityName = ArrayAdapter.createFromResource(getApplicationContext(),R.array.utility_names,R.layout.support_simple_spinner_dropdown_item);
        btnSave = findViewById(R.id.androidele_btnSaveAddInvoice);
        btnSave.setOnClickListener(saveInvoice());

        preferences = getSharedPreferences(MainActivity.THEME_PREF, MODE_PRIVATE);
    }

    private View.OnClickListener saveInvoice() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String apartTitle = etApartmentName.getText().toString();
                    Date date = dateConverter.stringToDate(etDate.getText().toString());
                    double value = Double.parseDouble(etValueInvoice.getText().toString());
                    long id = Long.parseLong(etUtilityID.getText().toString());
                    if(invoice==null) {
                        invoice = new Invoice(date, value, apartTitle, id);
                    }else
                    {
                        invoice.setApartmentName(apartTitle);
                        invoice.setDate(date);
                        invoice.setSum(value);
                        invoice.setIdUtility(id);
                    }
                    i.putExtra(INVOICE_KEY,invoice);
                    setResult(RESULT_OK,i);
                    System.out.println("put result invoice " + invoice.getApartmentName());
                    finish();

                }
            }

        };
    }

    private boolean validate() {

        if(etApartmentName.getText()==null || etApartmentName.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),getString(R.string.complete_apart_name),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etUtilityID.getText()==null || etUtilityID.getText().toString().isEmpty() || Long.parseLong(etUtilityID.getText().toString())<0) {
            Toast.makeText(getApplicationContext(), R.string.utility_id_invalid, Toast.LENGTH_SHORT);
            return false;
        }else{
            boolean existent = false;
            if(ids!=null) {
                for (long i : ids) {
                    if (i == Long.parseLong(etUtilityID.getText().toString())) {
                        existent = true;
                    }
                }
                if (!existent) {
                    Toast.makeText(getApplicationContext(), getString(R.string.doesnt_exist), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        if(etValueInvoice.getText()==null || etValueInvoice.getText().toString().isEmpty() || Double.parseDouble(etValueInvoice.getText().toString())<0){
            Toast.makeText(getApplicationContext(),getString(R.string.invalid_value),Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etDate.getText() == null || dateConverter.stringToDate(etDate.getText().toString().trim()) == null){
            Toast.makeText(getApplicationContext(),getString(R.string.complete_invoice_date),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}