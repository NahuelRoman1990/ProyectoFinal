package com.example.proyectofinal.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvTitulo.setText(post.getTitulo());
        holder.tvDescripcion.setText(post.getDescripcion());

        Log.d("PostAdapter", "Cargando imágenes: " + post.getImagenes());

        List<String> imagenes = post.getImagenes();

        // Verificar si las imágenes no son nulas ni vacías
        if (imagenes != null && !imagenes.isEmpty()) {
            // Recorrer las imágenes y cargarlas dinámicamente
            ImageView[] imageViews = {holder.ivImage1, holder.ivImage2, holder.ivImage3};

            // Ocultar todas las imágenes inicialmente
            for (ImageView imageView : imageViews) {
                imageView.setVisibility(View.GONE);
            }

            // Cargar las imágenes correspondientes
            for (int i = 0; i < imagenes.size(); i++) {
                Picasso.get()
                        .load(imagenes.get(i))
                        .fit()
                        .centerCrop()
                        .into(imageViews[i]);
                imageViews[i].setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        ImageView ivImage1, ivImage2, ivImage3;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            ivImage1 = itemView.findViewById(R.id.ivImage1);
            ivImage2 = itemView.findViewById(R.id.ivImage2);
            ivImage3 = itemView.findViewById(R.id.ivImage3);
        }
    }
}
