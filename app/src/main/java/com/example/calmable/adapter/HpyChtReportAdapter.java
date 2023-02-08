package com.example.calmable.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.HpyChtReportActivity;
import com.example.calmable.HpyChtUpdateActivity;
import com.example.calmable.R;

import java.util.ArrayList;
import java.util.Collections;

public class HpyChtReportAdapter extends RecyclerView.Adapter<HpyChtReportAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList list_id, list_event, list_rate , list_time;

    public HpyChtReportAdapter(Context context, Activity activity, ArrayList list_id, ArrayList list_event, ArrayList list_rate, ArrayList list_time) {
        this.context = context;
        this.activity = activity;
        this.list_id = list_id;
        this.list_event = list_event;
        this.list_rate = list_rate;
        this.list_time = list_time;
    }

    @NonNull
    @Override
    public HpyChtReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hpy_chrt_report_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.location_id.setText(String.valueOf(list_id.get(position)));      //id
        holder.hpy_event.setText(String.valueOf(list_event.get(position)));     //rate
        holder.hpy_rate.setText(String.valueOf(list_rate.get(position)));       //time
        holder.hpy_time.setText(String.valueOf(list_time.get(position)));       //event

        holder.progressBar.setProgress(Integer.valueOf(String.valueOf(list_event.get(position))));
        //holder.seekBarReportRate.setProgress(Integer.parseInt(String.valueOf(list_event.get(position))))

        Log.d("TAG", "--happiness rates--" + String.valueOf(list_event.get(position)));

        holder.seekBarReportRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               // int rate = (int) list_event.get(position);
                int rate = Integer.parseInt(String.valueOf(list_event.get(position)));

                progress = rate;

                Log.d("TAG", "_______AAAAA_____AAAAA______" + rate);
                Log.d("TAG", "_______AAAAA_____AAAAA______" + rate);
                Log.d("TAG", "_______AAAAA_____AAAAA______" + rate);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView location_id, hpy_event , hpy_rate, hpy_time;
        RelativeLayout mainLayout;
        SeekBar seekBarReportRate;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location_id = itemView.findViewById(R.id.reportId);
            hpy_event = itemView.findViewById(R.id.hpyChtEventRpTV);
            hpy_rate = itemView.findViewById(R.id.hpyChtRateRpTV);
            hpy_time = itemView.findViewById(R.id.hpyChtTimeRpTV);
            progressBar = itemView.findViewById(R.id.hpyProgressBar);
            mainLayout = itemView.findViewById(R.id.HptChtReport);
            seekBarReportRate = itemView.findViewById(R.id.seekBarReportRate);



        }
    }
}
