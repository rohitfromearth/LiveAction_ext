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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);

        holder.logoImageView.setImageResource(cardItem.getLogoResId());
        holder.titleTextView.setText(cardItem.getTitle());

        holder.ts_text.setText(cardItem.getTs_text());
        holder.average_text.setText(cardItem.getAverage_text());
        holder.variance_text.setText(cardItem.getVariance_text());

        holder.ts_value.setText(cardItem.getTs_value());
        holder.average_value.setText(cardItem.getAverage_value());
        holder.variance_value.setText(cardItem.getVariance_value());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView logoImageView;
        public TextView titleTextView;
        public TextView ts_text;
        public TextView average_text;
        public TextView variance_text;
        public TextView ts_value;
        public TextView average_value;
        public TextView variance_value;

        public CardViewHolder(View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);

            ts_text = itemView.findViewById(R.id.ts_text);
            average_text = itemView.findViewById(R.id.average_text);
            variance_text =  itemView.findViewById(R.id.variance_text);

            ts_value = itemView.findViewById(R.id.ts_value);
            average_value = itemView.findViewById(R.id.average_value);
            variance_value =  itemView.findViewById(R.id.variance_value);
        }
    }
}
