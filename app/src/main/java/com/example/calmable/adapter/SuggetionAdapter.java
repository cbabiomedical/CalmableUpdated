package com.example.calmable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calmable.MusicPlayer;
import com.example.calmable.R;
import com.example.calmable.model.CalmChart;
import com.example.calmable.model.FavModel;

import java.util.ArrayList;

public class SuggetionAdapter extends RecyclerView.Adapter<SuggetionAdapter.ViewHolder> {

    private ArrayList<CalmChart> listOfSugMusic = new ArrayList<CalmChart>();;
    private Context context;
    public static CalmChartAdapter.ViewHolder viewHolder;


    public SuggetionAdapter(ArrayList<CalmChart> listOfSugMusic, Context context) {
        this.listOfSugMusic = listOfSugMusic;
        this.context = context;
    }


    @NonNull
    @Override
    public SuggetionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_sugg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggetionAdapter.ViewHolder holder, int position) {

        CalmChart calmChart = listOfSugMusic.get(position);

        holder.songName.setText(listOfSugMusic.get(position).getSongName());
        //holder.timeToRelax.setText(String.valueOf(listOfSugMusic.get(position).getTimeToRelaxIndex()));

        holder.songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listOfSugMusic.get(position);

                Intent intent = new Intent(context, MusicPlayer.class);

                String songName = listOfSugMusic.get(position).getSongName();
                String url = listOfSugMusic.get(position).getSongUrl();
                int relaxIndex = listOfSugMusic.get(position).getTimeToRelaxIndex();

                intent.putExtra("songName", songName);
                intent.putExtra("url", url);
                intent.putExtra("relaxIndex", relaxIndex);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfSugMusic.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView songName , timeToRelax;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.songTitle);
            //timeToRelax = itemView.findViewById(R.id.calmChrtRealxIndex);
            recyclerView = itemView.findViewById(R.id.listOfSongRecycleView);

        }
    }
}
