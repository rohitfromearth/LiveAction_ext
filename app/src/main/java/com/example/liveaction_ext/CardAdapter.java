package com.example.liveaction_ext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final List<CardItem> cardItems;
    private final Context context;

    public CardAdapter(List<CardItem> cardItems, Context context) {
        this.cardItems = cardItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_itm, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);

        holder.logoImageView.setImageResource(cardItem.getLogoResId());
        holder.titleTextView.setText(cardItem.getTitle());
        holder.info1TextView.setText(cardItem.getInfo1());
        holder.info2TextView.setText(cardItem.getInfo2());
        holder.info3TextView.setText(cardItem.getInfo3());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView logoImageView;
        public TextView titleTextView;
        public TextView info1TextView;
        public TextView info2TextView;
        public TextView info3TextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            info1TextView = itemView.findViewById(R.id.info1TextView);
            info2TextView = itemView.findViewById(R.id.info2TextView);
            info3TextView = itemView.findViewById(R.id.info3TextView);
        }
    }
}
