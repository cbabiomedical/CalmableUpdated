package com.example.calmable.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.HpyChtUpdateActivity;
import com.example.calmable.R;
import com.example.calmable.db.HpyChtLocationDB;
import com.example.calmable.model.FavModel;
import com.example.calmable.sample.HpyChtPopUp;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList list_location_id, list_location_address, list_location_time;

    HpyChtLocationDB myDB;

    public LocationAdapter(Context context, Activity activity, ArrayList list_location_id, ArrayList list_location_address, ArrayList list_location_time) {
        this.context = context;
        this.activity = activity;
        this.list_location_id = list_location_id;
        this.list_location_address = list_location_address;
        this.list_location_time = list_location_time;
    }

    @NonNull
    @Override
    public LocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.location_item_name, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.MyViewHolder holder, int position) {
        holder.location_id.setText(String.valueOf(list_location_id.get(position)));
        holder.location_address.setText(String.valueOf(list_location_address.get(position)));
        holder.location_time.setText(String.valueOf(list_location_time.get(position)));

        holder.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HpyChtUpdateActivity.class);
                intent.putExtra("id", String.valueOf(list_location_id.get(position)));
                intent.putExtra("address", String.valueOf(list_location_address.get(position)));
                intent.putExtra("time", String.valueOf(list_location_time.get(position)));
                activity.startActivityForResult(intent, 1);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_location_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView location_id, location_address , location_time;
        Button btnNo , btnYes;
        RelativeLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location_id = itemView.findViewById(R.id.locationId);
            location_address = itemView.findViewById(R.id.locationNameTV);
            location_time = itemView.findViewById(R.id.locationTimeTV);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            btnNo = itemView.findViewById(R.id.btnNo);
            btnYes = itemView.findViewById(R.id.btnYes);

        }
    }

}
