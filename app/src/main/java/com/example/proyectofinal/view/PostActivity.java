package com.example.proyectofinal.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.proyectofinal.R;
import com.example.proyectofinal.adapters.ImageAdapter;
import com.example.proyectofinal.databinding.ActivityPostBinding;
import com.example.proyectofinal.model.Post;
import com.example.proyectofinal.util.ImageUtils;
import com.example.proyectofinal.util.Validaciones;
import com.example.proyectofinal.view.fragments.HomeFragment;
import com.example.proyectofinal.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final int MAX_IMAGES = 3;

    private ActivityPostBinding binding;
    private PostViewModel postViewModel;
    private final List<String> imagenesUrls = new ArrayList<>();
    private ImageAdapter adapter;
    private String categoria;

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        setupViewModels();
        setupCategorySpinner();
        setupGalleryLauncher();

        binding.btnPublicar.setOnClickListener(v -> publicarPost());
        volverAtras();

    }

    private void setupRecyclerView() {
        adapter = new ImageAdapter(imagenesUrls, this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setAdapter(adapter);
        updateRecyclerViewVisibility();
    }

    private void setupViewModels() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getPostSuccess().observe(this, success -> {
            String message = success ? "Post publicado con éxito" : "Error al publicar";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (success) finish();
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.categorias_array)
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapter);

        binding.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoria = null;
            }
        });
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null && imagenesUrls.size() < MAX_IMAGES) {
                            ImageUtils.subirImagenAParse(this, imageUri, new ImageUtils.ImageUploadCallback() {
                                @Override
                                public void onSuccess(String imageUrl) {
                                    imagenesUrls.add(imageUrl);
                                    adapter.notifyDataSetChanged();
                                    updateRecyclerViewVisibility();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(PostActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (imagenesUrls.size() >= MAX_IMAGES) {
                            Toast.makeText(this, "Máximo de imágenes alcanzado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        binding.uploadImage.setOnClickListener(v -> {
            ImageUtils.pedirPermisos(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ImageUtils.openGallery(this, galleryLauncher);
        });
    }

    private void publicarPost() {
        String titulo = binding.itTitulo.getText().toString().trim();
        String descripcion = binding.etDescripcion.getText().toString().trim();

        if (!Validaciones.validarTexto(titulo)) {
            binding.itTitulo.setError("El título no es válido");
            return;
        }
        if (!Validaciones.validarTexto(descripcion)) {
            binding.etDescripcion.setError("La descripción no es válida");
            return;
        }

        Post post = new Post(titulo, descripcion, 0, categoria, 0, new ArrayList<>(imagenesUrls));
        postViewModel.publicar(post);




    }

    private void updateRecyclerViewVisibility() {
        binding.recyclerView.setVisibility(imagenesUrls.isEmpty() ? View.GONE : View.VISIBLE);
        binding.uploadImage.setVisibility(imagenesUrls.size() < MAX_IMAGES ? View.VISIBLE : View.GONE);
    }

    private void volverAtras() {
        // Evento volver a login
        binding.circleImageBackPost.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeFragment.class);
            startActivity(intent);
        });
    }
}
