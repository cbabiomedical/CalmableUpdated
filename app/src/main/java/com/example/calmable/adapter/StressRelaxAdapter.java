package com.example.calmable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.R;
import com.example.calmable.model.StressRelaxModel;

import java.util.List;

public class StressRelaxAdapter extends RecyclerView.Adapter<StressRelaxAdapter.ViewHolder> {

    Context context;
    List<StressRelaxModel> stressRelaxModelList;

    public StressRelaxAdapter(Context context, List<StressRelaxModel> stressRelaxModelList) {
        this.context = context;
        this.stressRelaxModelList = stressRelaxModelList;
    }

    @NonNull
    @Override
    public StressRelaxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stressrelaxitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StressRelaxAdapter.ViewHolder holder, int position) {
        StressRelaxModel stressRelaxModel = stressRelaxModelList.get(position);

        holder.relaxindex1.setText(String.valueOf(stressRelaxModel.getRelaxation()));
        holder.stressindex1.setText(String.valueOf(stressRelaxModel.getStressed()));
        holder.relaxstresstime1.setText(stressRelaxModel.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return stressRelaxModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView relaxindex1, stressindex1, relaxstresstime1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relaxindex1 = itemView.findViewById(R.id.relaxindexed);
            stressindex1 = itemView.findViewById(R.id.stressindexed);
            relaxstresstime1 = itemView.findViewById(R.id.relaxstresstimeed);
        }
    }
}
