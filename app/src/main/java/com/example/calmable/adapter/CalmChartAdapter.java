package com.example.calmable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.CalmChartActivity;
import com.example.calmable.R;
import com.example.calmable.db.FavDB;
import com.example.calmable.model.CalmChart;
import com.example.calmable.model.FavModel;

import java.util.ArrayList;

public class CalmChartAdapter extends RecyclerView.Adapter<CalmChartAdapter.ViewHolder>{


    private ArrayList<CalmChart> listOfClamChrt = new ArrayList<CalmChart>();;
    private Context context;
    public static ViewHolder viewHolder;

    public CalmChartAdapter( Context context , ArrayList<CalmChart> listOfClamChrt) {
        this.context = context;
        this.listOfClamChrt = listOfClamChrt;
    }

    public CalmChartAdapter() {

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calm_chart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CalmChart calmChart = listOfClamChrt.get(position);

        holder.songName.setText(listOfClamChrt.get(position).getSongName());
        holder.timeToRelax.setText(String.valueOf(listOfClamChrt.get(position).getTimeToRelaxIndex()));


    }

    @Override
    public int getItemCount() {
        return listOfClamChrt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView songName , timeToRelax;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.calmChrtSongName);
            timeToRelax = itemView.findViewById(R.id.calmChrtRealxIndex);
            recyclerView = itemView.findViewById(R.id.listOfCalmChrtRecycleView);



        }
    }
}
