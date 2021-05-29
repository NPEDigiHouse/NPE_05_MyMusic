package com.example.npe_05_mymusic.Adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npe_05_mymusic.Models.songs.SongsModel;
import com.example.npe_05_mymusic.R;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    private OnItemClick onItemClick;
    private List<SongsModel> songsModelList;

    public SongsAdapter(OnItemClick onItemClick, List<SongsModel> songsModelList) {
        this.onItemClick = onItemClick;
        this.songsModelList = songsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_song, parent, false);
        return new ViewHolder(view, onItemClick );
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(songsModelList.get(position).getCover_url())
                .into(holder.ivImage);
        holder.tvTitle.setText(songsModelList.get(position).getTitle());
        holder.tvArtist.setText(songsModelList.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return songsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnItemClick onItemClick;
        ImageView ivImage;
        TextView tvTitle, tvArtist;
        ImageButton btnMore;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;

            ivImage = itemView.findViewById(R.id.iv_image_item_my_song);
            tvTitle = itemView.findViewById(R.id.tv_title_item_my_song);
            tvArtist = itemView.findViewById(R.id.tv_artist_item_my_song);
            btnMore = itemView.findViewById(R.id.ib_more_item_my_song);
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.btnMoreClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClick {
        void btnMoreClicked(int position);
    }
}
