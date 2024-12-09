package com.example.proyectofinal.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.proyectofinal.adapters.PostAdapter;
import com.example.proyectofinal.databinding.FragmentHomeBinding;
import com.example.proyectofinal.model.Post;
import com.example.proyectofinal.view.MainActivity;
import com.example.proyectofinal.view.PostActivity;
import com.example.proyectofinal.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PostViewModel postViewModel;
    private List<Post> postList;
    private PostAdapter adapter;
    private int currentPage = 0; // Página actual
    private boolean isLoading = false; // Para evitar cargas múltiples

    public HomeFragment() {}

    public static HomeFragment newInstance(String p1, String p2) {
        return new HomeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPosts(); // Llama al método para recargar los posts
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa el ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        postList = new ArrayList<>();
        adapter = new PostAdapter(postList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        cargarPosts();

        // Listener para cargar más posts al hacer scroll
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Comprueba si el usuario ha llegado al final
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int totalItemCount = layoutManager.getItemCount();
                    int visibleItemCount = layoutManager.getChildCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // Si estamos en el final de la lista
                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        cargarPosts(); // Cargar más posts
                    }
                }
            }
        });


        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });
    }

    private void cargarPosts() {
        if (isLoading) return; // Evita solicitudes múltiples mientras se está cargando
        isLoading = true; // Marca como cargando
        postViewModel.getPosts(currentPage).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                postList.addAll(posts); // Añade nuevos posts a la lista existente
                adapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
                currentPage++; // Incrementa la página después de cargar los posts
            }
            isLoading = false; // Permite nuevas solicitudes
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onLogout() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void setupMenu() {
        binding.fab.setOnClickListener(v -> onLogout());
    }
}