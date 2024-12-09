package com.example.proyectofinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostSliderAdapter extends RecyclerView.Adapter<PostSliderAdapter.SliderViewHolder> {

    private List<String> images;

    // Constructor para recibir las imágenes
    public PostSliderAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_image, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        // Usamos Picasso o cualquier librería para cargar la imagen
        String imageUrl = images.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    // ViewHolder para manejar el slider
    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_slider_image);
        }
    }

}
