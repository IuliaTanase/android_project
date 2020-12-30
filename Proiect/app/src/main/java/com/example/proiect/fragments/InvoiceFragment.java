package com.example.proiect.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proiect.AddInvoiceActivity;
import com.example.proiect.AddUtilityActivity;
import com.example.proiect.MainActivity;
import com.example.proiect.R;
import com.example.proiect.adapters.InvoiceAdapter;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.Utility;
import com.example.proiect.database.models.UtilityAndInvoice;
import com.example.proiect.database.service.InvoiceService;
import com.example.proiect.database.service.UtilityService;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class InvoiceFragment extends Fragment {

    private final static int CODE_ADD_INVOICE = 205;
    private final static int CODE_UPDATE_INVOICE = 207;

    Spinner spinnerValue;
    Spinner spinnerUtilityName;
    ListView lv_invoices;
    List<UtilityAndInvoice> invoices = new ArrayList<>();  //lista cu toate legaturile dintre factura-utilitate
    List<Invoice> justInvoices = new ArrayList<>();   //lista cu toate facturile
    List<Utility> utilities = new ArrayList<>();     //lista cu utilitati corespondente fiecarei facturi (exista duplicate aici in cazul in care mai multe facturi au aceeasi utilitate). Doar utilitatile pt care exista cel putin o factura
    Button btnAddInvoice;
    InvoiceService invoiceService;
    UtilityService utilityService;
    List<Utility> existingUtilities=new ArrayList<>();  //lista cu toate utilitati, indiferent daca au facturi corespondente sau nu
    long[] ids=new long[100];
    EditText etUpdateInvoice;
    Button btnUpdateInvoice;
    private SharedPreferences preferences;

    public InvoiceFragment() {
        // Required empty public constructor
    }


    public static InvoiceFragment newInstance() {
        InvoiceFragment fragment = new InvoiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_invoice, container, false);
       invoiceService = new InvoiceService(getContext());
       utilityService = new UtilityService(getContext());
       utilityService.getAll(getAllUtilitiesFromDB());
       initialize(view);
       loadFromPrefs();

       return view;
    }

    private Callback<List<Utility>> getAllUtilitiesFromDB() {
        return new Callback<List<Utility>>() {
            @Override
            public void runResultOnUiThread(List<Utility> result) {
                existingUtilities.clear();
                if(result!=null){
                    existingUtilities.addAll(result);
                }
                for(int i=0;i<result.size();i++){
                    ids[i] = result.get(i).getIdUtility();
                }
            }
        };
    }

    private void initialize(View view) {
        spinnerUtilityName = view.findViewById(R.id.androidele_spinnerInvoicesUtilityName);
        ArrayAdapter<CharSequence> adapterUtilityName = ArrayAdapter.createFromResource(requireContext(),R.array.utility_names,R.layout.support_simple_spinner_dropdown_item);
        spinnerUtilityName.setAdapter(adapterUtilityName);
        spinnerUtilityName.setOnItemSelectedListener(selectValue());

        spinnerValue = view.findViewById(R.id.androidele_spinnerValue);
        ArrayAdapter<CharSequence> adapterValues = ArrayAdapter.createFromResource(requireContext(),R.array.values,R.layout.support_simple_spinner_dropdown_item);
        spinnerValue.setAdapter(adapterValues);
        spinnerValue.setOnItemSelectedListener(selectValue());

        lv_invoices = view.findViewById(R.id.androidele_lvInvoices);
        addAdapterInvoices();

        btnAddInvoice = view.findViewById(R.id.androidele_btnAddInvoice);
        btnAddInvoice.setOnClickListener(goToAddInvoice());

        lv_invoices.setOnItemClickListener(clickInvoiceEvent());

        etUpdateInvoice= view.findViewById(R.id.androidele_etUpdateInvoice);
        btnUpdateInvoice = view.findViewById(R.id.androidele_btnUpdateInvoice);
        btnUpdateInvoice.setOnClickListener(updateItem());
        preferences = requireContext().getSharedPreferences(MainActivity.THEME_PREF, MODE_PRIVATE);
    }

    private void loadFromPrefs() {
        boolean switchChecked = preferences.getBoolean(MainActivity.CHECKED, false);
        if(switchChecked) {
            etUpdateInvoice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private View.OnClickListener updateItem() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int index = Integer.parseInt(etUpdateInvoice.getText().toString());
                    if(index > 0 && index <= justInvoices.size()){
                        Intent i = new Intent(getContext(), AddUtilityActivity.class);
                        Intent intent = new Intent(getContext(),AddInvoiceActivity.class);
                        intent.putExtra(AddInvoiceActivity.INVOICE_KEY,justInvoices.get(index-1));
                        intent.putExtra(AddInvoiceActivity.UTILITY_KEY,ids);
                        startActivityForResult(intent,CODE_UPDATE_INVOICE);
                    }else{
                        Toast.makeText(getContext(), R.string.invalid,Toast.LENGTH_LONG).show();
                    }

            }
        };
    }

    private AdapterView.OnItemClickListener clickInvoiceEvent() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),AddInvoiceActivity.class);
                intent.putExtra(AddInvoiceActivity.INVOICE_KEY,justInvoices.get(0));
                intent.putExtra(AddInvoiceActivity.UTILITY_KEY,ids);
                startActivityForResult(intent,CODE_UPDATE_INVOICE);
            }
        };
    }

    private void addAdapterInvoices() {
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(requireContext(),R.layout.lv_invoices_row,justInvoices,getLayoutInflater(),invoiceService,utilities);
        lv_invoices.setAdapter(invoiceAdapter);
    }


    private AdapterView.OnItemSelectedListener selectValue() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = spinnerValue.getSelectedItem().toString();
                String utilityName = spinnerUtilityName.getSelectedItem().toString();

                if(value.equalsIgnoreCase(getString(R.string.all))){
                    if(utilityName.equalsIgnoreCase(getString(R.string.all))){
                        invoiceService.getAll(getAllInvoicesFromDB());
                    }else{
                        invoiceService.getInvoicesFromUtility(getAllInvoicesFromDB(),utilityName);
                    }
                }else
                {
                    if(utilityName.equalsIgnoreCase(getString(R.string.all))){
                        invoiceService.getInvoicesWithMaxSum(getAllInvoicesFromDBVal(value),Double.parseDouble(value));
                    }else{
                        invoiceService.getInvoicesWithSumAndName(getAllInvoicesFromDBVal(value),utilityName,Double.parseDouble(value));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
    }

    private Callback<List<UtilityAndInvoice>> getAllInvoicesFromDBVal(String value) {
        return new Callback<List<UtilityAndInvoice>>() {
            @Override
            public void runResultOnUiThread(List<UtilityAndInvoice> result) {
                invoices.clear();
                justInvoices.clear();
                utilities.clear();

                if(result != null){
                    for(UtilityAndInvoice ui : result){

                        for(Invoice i : ui.invoices){
                            if(i.getSum() < Double.parseDouble(value)) {
                                if (!justInvoices.contains(i)) {
                                    justInvoices.add(i);
                                    utilities.add(ui.utility); //avem 3 liste aici - una de resultatele primite din baza, una cu toate facturile si una cu fiecare utilitate pentru fiecare factura
                                }
                            }
                        }
                    }
                }
                notifyAdapterInvoices();
            }
        };
    }

    private Callback<List<UtilityAndInvoice>> getAllInvoicesFromDB() {
        return new Callback<List<UtilityAndInvoice>>() {
            @Override
            public void runResultOnUiThread(List<UtilityAndInvoice> result) {
                invoices.clear();
                justInvoices.clear();
                utilities.clear();

                if(result != null){
                    for(UtilityAndInvoice ui : result){

                        for(Invoice i : ui.invoices){
                            if(!justInvoices.contains(i)) {
                                justInvoices.add(i);
                                utilities.add(ui.utility); //avem 3 liste aici - una de resultatele primite din baza, una cu toate facturile si una cu fiecare utilitate pentru fiecare factura
                            }
                        }
                    }
                }
                notifyAdapterInvoices();
            }
        };
    }

    private void notifyAdapterInvoices() {
        InvoiceAdapter invoiceAdapter = (InvoiceAdapter) lv_invoices.getAdapter();
        invoiceAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener goToAddInvoice() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddInvoiceActivity.class);
                i.putExtra(AddInvoiceActivity.UTILITY_KEY,ids);
                startActivityForResult(i,CODE_ADD_INVOICE);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null){
            Invoice invoice = (Invoice) data.getSerializableExtra(AddInvoiceActivity.INVOICE_KEY);
            if(invoice!=null) {
                if (requestCode == CODE_ADD_INVOICE) {
                    Toast.makeText(getContext(), R.string.new_invoicee, Toast.LENGTH_SHORT).show();
                    invoiceService.insert(insertInvoiceIntoDB(),invoice);
                }else{
                    Toast.makeText(getContext(),getString(R.string.invoice_updated),Toast.LENGTH_SHORT).show();
                    invoiceService.update(updateInvoiceIntoDB(),invoice);
                }
            }
        }

    }

    private Callback<Invoice> updateInvoiceIntoDB() {
        return new Callback<Invoice>() {
            @Override
            public void runResultOnUiThread(Invoice result) {
                if(result!=null){
                    for(int i=0;i<justInvoices.size();i++){
                        if(justInvoices.get(i).getIdInvoice() == result.getIdInvoice()){
                            justInvoices.get(i).setApartmentName(result.getApartmentName());
                            justInvoices.get(i).setSum(result.getSum());
                            justInvoices.get(i).setDate(result.getDate());
                            justInvoices.get(i).setIdUtility(result.getIdUtility());
                            for(Utility u:existingUtilities){
                                if(u.getIdUtility()==result.getIdUtility()){
                                    utilities.set(i,u);  //inlocuim utilitatea corespunzatoare facturii modificate
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private Callback<Invoice> insertInvoiceIntoDB() {
        return new Callback<Invoice>() {
            @Override
            public void runResultOnUiThread(Invoice result) {
                justInvoices.add(result);
                if(result!=null){
                    //verificam daca utilitatea respectivei facturi exista
                    for(Utility u:existingUtilities){
                        if(u.getIdUtility()==result.getIdUtility()){
                            utilities.add(u);
                            break;
                        }
                    }

                    notifyAdapterInvoices();
                }
            }
        };
    }

}