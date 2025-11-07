package com.example.ayudacostura.Data.model;

public class Material {
    private String id;
    private String nombre;
    private String cantidad;
    private String descripcion;

    public Material() {
        // Requerido por Firebase
    }

    public Material(String id, String nombre, String cantidad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCantidad() { return cantidad; }
    public String getDescripcion() { return descripcion; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
