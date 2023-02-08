package com.example.calmable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder> {

    Context context;
    ArrayList<Note1> list1;

    public MyAdapter1(Context context, ArrayList<Note1> list1) {
        this.context = context;
        this.list1 = list1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Note1 note1 = list1.get(position);
        holder.person.setText(note1.getPerson());
        holder.reason.setText(note1.getReason());
        holder.time.setText(note1.getTime());
        holder.place.setText(note1.getPlace());
        holder.date.setText(note1.getDate());
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView person, reason, time, place, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            person = itemView.findViewById(R.id.tvperson);
            reason = itemView.findViewById(R.id.tvreason);
            time = itemView.findViewById(R.id.tvtime);
            place = itemView.findViewById(R.id.tvplace);
            date = itemView.findViewById(R.id.tvdate);
        }
    }


}
