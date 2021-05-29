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

public class RekomendasiAdapter extends RecyclerView.Adapter<RekomendasiAdapter.ViewHolder> {
    private OnItemClick onItemClick;
    private List<SongsModel> rekomendasiList;

    public RekomendasiAdapter(List<SongsModel> rekomendasiList, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.rekomendasiList = rekomendasiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rekomendasi, parent, false);
        return new ViewHolder(view, onItemClick );
    }

    @Override
    public void onBindViewHolder(@NonNull RekomendasiAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(rekomendasiList.get(position).getCover_url())
                .into(holder.ivImage);
        holder.tvTitle.setText(rekomendasiList.get(position).getTitle());
        holder.tvArtist.setText(rekomendasiList.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return rekomendasiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnItemClick onItemClick;
        ImageView ivImage;
        TextView tvTitle, tvArtist;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.itemRekomendasiClicked(getAdapterPosition());
                }
            });

            ivImage = itemView.findViewById(R.id.iv_artis_rekomendasi);
            tvTitle = itemView.findViewById(R.id.tv_title_rekomendasi);
            tvArtist = itemView.findViewById(R.id.tv_artis_rekomendasi);
        }
    }

    public interface OnItemClick {
        void itemRekomendasiClicked(int position);
    }
}
