package com.example.calmable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.R;
import com.example.calmable.model.InterventionLocationModel;

import java.util.List;

public class InterventionLocationAdapter extends RecyclerView.Adapter<InterventionLocationAdapter.ViewHolder> {

    Context context;
    List<InterventionLocationModel> interventionLocationModels;

    public InterventionLocationAdapter(Context context, List<InterventionLocationModel> interventionLocationModels) {
        this.context = context;
        this.interventionLocationModels = interventionLocationModels;
    }

    @NonNull
    @Override
    public InterventionLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interventionlocationitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InterventionLocationAdapter.ViewHolder holder, int position) {
        InterventionLocationModel interventionLocationModel = interventionLocationModels.get(position);

        holder.interventionName.setText(interventionLocationModel.getSongName());
        holder.interventionlocation.setText(interventionLocationModel.getAddress());
        holder.interventiontime.setText(interventionLocationModel.getDate());
    }

    @Override
    public int getItemCount() {
        return interventionLocationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView interventionName,interventionlocation,interventiontime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            interventionName = itemView.findViewById(R.id.interventionName);
            interventionlocation = itemView.findViewById(R.id.interventionlocation);
            interventiontime = itemView.findViewById(R.id.interventiontime);
        }
    }
}
