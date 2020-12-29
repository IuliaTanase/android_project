package com.example.proiect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiect.AddUtilityActivity;
import com.example.proiect.R;
import com.example.proiect.asyncTask.Callback;
import com.example.proiect.database.models.Utility;
import com.example.proiect.database.service.UtilityService;
import com.example.proiect.fragments.UtilityFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class UtilityAdapter extends ArrayAdapter<Utility> {


    private Context ctx;
    private int res;
    private List<Utility> utilities;
    private LayoutInflater inflater;
    private UtilityService utilityService;

    public UtilityAdapter(@NonNull Context context, int resource, @NonNull List<Utility> objects, LayoutInflater inflater, UtilityService utilityService) {
        super(context, resource, objects);
        this.ctx = context;
        this.res=resource;
        this.utilities = objects;
        this.inflater=inflater;
        this.utilityService = utilityService;
        System.out.println("CONSTRUCTOR UTILITY ADAPTER ");
        System.out.println(objects.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("getview UTILITY");
        View v = inflater.inflate(res,parent,false);

        Utility utility = utilities.get(position);
        if(utility!=null){
            System.out.println("UTILITY ID - " +utility.getIdUtility());
            addUtilityName(utility.getName(),v);
            addUtilityProvider(utility.getProvider(),v);
            addUtilityImage(utility.getName(),v);
        }
        FloatingActionButton fab = v.findViewById(R.id.androidele_fabDelete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilityService.delete(deleteUtilityFromDb(position),utilities.get(position));
            }
        });

        return v;

    }

    private Callback<Integer> deleteUtilityFromDb(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result != -1) {
                    utilities.remove(position);
                    notifyDataSetChanged();
                }
            }
        };
    }

    private void addUtilityImage(String name,View v) {

        ImageView imageView = v.findViewById(R.id.androidele_IVutilityImage);
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

    private void addUtilityProvider(String provider,View v) {

        TextView tvProvider = v.findViewById(R.id.androidele_tvUtilityProvider);
        if(provider!=null && !provider.isEmpty()){
            tvProvider.setText(provider);
        }else
        {
            tvProvider.setText(R.string.no_provider);
        }
    }

    private void addUtilityName(String name,View v) {
        TextView tvName = v.findViewById(R.id.androidele_tvNameUtility);
        if(name!=null && !name.isEmpty()){
            tvName.setText(name);
        }else
        {
            tvName.setText(R.string.no_utility_name);
        }
    }
}
