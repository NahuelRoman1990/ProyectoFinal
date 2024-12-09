package com.example.proyectofinal.model;

import java.util.List;

public class Post {
    private String titulo, descripcion, categoria;
    private int duracion;
    private double presupuesto;
    private List<String> imagenes;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post() {}

    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
    }

    public Post(String titulo, String descripcion, int duracion, String categoria, double presupuesto, List<String> imagenes) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.imagenes = imagenes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }
}