package com.example.proyectofinal.model;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String fotoperfil;
    private String instagramHandle;
    private int postCount; // Agregar el contador de publicaciones

    // Constructor vacío
    public User() {
    }

    // Constructor con username, email y password
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Constructor con id, username, email y password
    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Métodos getter
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }

    public String getInstagramHandle() {
        return instagramHandle;
    }

    public int getPostCount() {
        return postCount;
    }

    // Métodos setter
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public void setInstagramHandle(String instagramHandle) {
        this.instagramHandle = instagramHandle;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    // Método para obtener el nombre completo (en caso de que sea diferente de username)
    public String getName() {
        return this.username; // O puedes devolver otro campo si necesitas un nombre completo
    }

    // Método para obtener la URL de la foto de perfil
    public String getProfilePictureUrl() {
        return this.fotoperfil; // Asegúrate de que fotoperfil sea la URL de la imagen
    }
}
