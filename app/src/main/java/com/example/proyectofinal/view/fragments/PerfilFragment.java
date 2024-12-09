package com.example.proyectofinal.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private TextView nameUserTextView, emailUserTextView, conteoPublicacionesTextView, instaTextView;
    private CircleImageView circleImageView;

    public PerfilFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializar vistas
        nameUserTextView = rootView.findViewById(R.id.name_user);
        emailUserTextView = rootView.findViewById(R.id.email_user);
        conteoPublicacionesTextView = rootView.findViewById(R.id.conteoPublicaciones);
        instaTextView = rootView.findViewById(R.id.insta);
        circleImageView = rootView.findViewById(R.id.circleImageView);

        // Listener para cambiar la imagen de perfil
        circleImageView.setOnClickListener(v -> openGallery());

        // Cargar la información del perfil desde Parse
        loadUserProfile();

        return rootView;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Actualizar la vista con la nueva imagen
                circleImageView.setImageURI(selectedImageUri);

                // Subir la imagen a Parse
                uploadProfileImageToParse(selectedImageUri);
            }
        }
    }

    private void uploadProfileImageToParse(Uri imageUri) {
        try {
            byte[] imageBytes = getBytesFromUri(imageUri);
            com.parse.ParseFile parseFile = new com.parse.ParseFile("profile.jpg", imageBytes);

            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.put("profilePictureUrl", parseFile);
            currentUser.saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(getContext(), "Imagen de perfil actualizada.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al cargar la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void loadUserProfile() {
        // Obtener el usuario actual desde Parse
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Actualizar las vistas con los datos del usuario
            nameUserTextView.setText(currentUser.getString("name")); // Campo "name" en Parse
            emailUserTextView.setText(currentUser.getEmail());       // Campo "email" estándar en Parse
            instaTextView.setText(currentUser.getString("instagramHandle")); // Campo "instagramHandle" en Parse

            // Consultar y actualizar el conteo de publicaciones
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereEqualTo("user", currentUser);
            query.countInBackground((count, e) -> {
                if (e == null) {
                    conteoPublicacionesTextView.setText(String.valueOf(count));
                } else {
                    conteoPublicacionesTextView.setText("0");
                }
            });

            // Cargar la imagen de perfil con Picasso
            String profilePictureUrl = currentUser.getString("profilePictureUrl");
            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                Picasso.get().load(profilePictureUrl).into(circleImageView);
            } else {
                // Cargar una imagen predeterminada si no hay foto de perfil
                circleImageView.setImageResource(R.drawable.ic_person);
            }
        } else {
            // Manejar el caso cuando no hay usuario
            nameUserTextView.setText("Usuario no encontrado");
            emailUserTextView.setText("");
            conteoPublicacionesTextView.setText("0");
            instaTextView.setText("");
            circleImageView.setImageResource(R.drawable.ic_person);
        }
    }
}
