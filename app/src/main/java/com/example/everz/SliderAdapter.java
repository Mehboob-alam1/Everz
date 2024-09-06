package com.example.everz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private OnSliderButtonClickListener listener;

    public SliderAdapter(List<SliderItem> sliderItems, OnSliderButtonClickListener listener) {
        this.sliderItems = sliderItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderItem sliderItem = sliderItems.get(position);
        holder.imageView.setImageResource(sliderItem.getImageResource());
        holder.titleTextView.setText(sliderItem.getTitle());
        holder.descriptionTextView.setText(sliderItem.getDescription());
        holder.button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSliderButtonClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        Button button;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            button = itemView.findViewById(R.id.button);
        }
    }

    public interface OnSliderButtonClickListener {
        void onSliderButtonClick();
    }
}




