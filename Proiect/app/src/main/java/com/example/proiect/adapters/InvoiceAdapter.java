package com.example.proiect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiect.R;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.models.Invoice;
import com.example.proiect.database.models.Utility;
import com.example.proiect.database.models.UtilityAndInvoice;
import com.example.proiect.database.service.InvoiceService;
import com.example.proiect.utils.DateConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

public class InvoiceAdapter extends ArrayAdapter<Invoice> {

    private Context ctx;
    private int res;
    private List<Invoice> invoices;
    private LayoutInflater infl;
    private InvoiceService invoiceService;
    private DateConverter dateConverter;
    private List<Utility> utilities;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> objects,LayoutInflater inflater,InvoiceService invoiceService,List<Utility> utilities) {
        super(context, resource, objects);
        this.ctx=context;
        this.res=resource;
        this.infl=inflater;
        this.invoices = objects;
        this.invoiceService = invoiceService;
        dateConverter = new DateConverter();
        this.utilities = utilities;
        System.out.println("CONSTRUCTOR INVOICE ADAPTER");
        System.out.println(objects.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("getview ADAPTER INVOICE");
        View v = infl.inflate(res,parent,false);
        Invoice invoice = invoices.get(position);
        Utility utility = utilities.get(position);

        if(invoice!=null){
            addApartmentTitle(v,invoice.getApartmentName());
            addDate(v,invoice.getDate());
            addSum(v,invoice.getSum());
            addUtilityName(v,utility.getName());
            addImage(v,utility.getName());
        }

        FloatingActionButton floatingActionButton = v.findViewById(R.id.androidele_fabDeleteInvoice);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceService.delete(deleteInvoiceFromDB(position),invoices.get(position));
            }
        });
        return v;
    }

    private Callback<Integer> deleteInvoiceFromDB(final int pos) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result!=-1){
                    invoices.remove(pos);
                    utilities.remove(pos);
                    notifyDataSetChanged();
                }
            }
        };
    }


    private void addImage(View v, String name) {
        ImageView imageView = v.findViewById(R.id.androidele_IVinvoicesImage);
        if(name!=null && !name.isEmpty()){
            if(name.equalsIgnoreCase("Energy")){
                imageView.setImageResource(R.drawable.energy);
            }else if(name.equalsIgnoreCase("Gas")){
                imageView.setImageResource(R.drawable.gas);
            }else if(name.equalsIgnoreCase("Water")){
                imageView.setImageResource(R.drawable.water);
            }
        }else
        {
            imageView.setImageResource(R.drawable.improvement);
        }
    }

    private void addUtilityName(View v, String name) {
        TextView tvName = v.findViewById(R.id.androidele_tvutNameInvoices);
        if(name!=null && !name.isEmpty()){
            tvName.setText(name);
        }else
        {
            tvName.setText(R.string.no_invoice_utility);
        }
    }

    private void addSum(View v, double sum) {
        TextView tvSum = v.findViewById(R.id.androidele_tvValue);
        if(!Double.isNaN(sum)){
            tvSum.setText(String.valueOf(sum));
        }else
        {
            tvSum.setText(R.string.noinvoice_value);
        }
    }

    private void addDate(View v, Date date) {

        TextView tvDate = v.findViewById(R.id.androidele_tvDateInvoice);
        if(tvDate!=null){
            String dt = dateConverter.dateToString(date);
            tvDate.setText(dt);
        }else
        {
            tvDate.setText(R.string.noinvoice_date);
        }
    }

    private void addApartmentTitle(View v, String apName) {
        TextView tvApName = v.findViewById(R.id.androidele_tvApTitleInvoices);
        if(apName!= null && !apName.isEmpty()){

            tvApName.setText(apName);
        }else
        {
            tvApName.setText(R.string.noinvoice_apartment);
        }
    }
}
