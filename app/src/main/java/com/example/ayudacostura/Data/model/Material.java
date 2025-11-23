package com.example.ayudacostura.Data.model;

import java.util.UUID;

/**
 * Modelo que representa un Material del inventario.
 * Incluye nombre, cantidad, descripción e imagen opcional.
 */
public class Material {

    // Identificador único del material
    private String id;

    // Nombre del material (ej: "Tela roja")
    private String nombre;

    // Cantidad disponible (texto porque puede incluir unidades ej: "2m")
    private String cantidad;

    // Descripción opcional del material
    private String descripcion;

    // URL de imagen almacenada en Firebase Storage
    private String imagenUrl;

    // Constructor vacío requerido por Firebase
    public Material() {}

    // Constructor completo (cuando ya existe un ID)
    public Material(String id, String nombre, String cantidad, String descripcion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Constructor usado al agregar un nuevo material (genera ID automáticamente)
    public Material(String nombre, String cantidad, String descripcion, String imagenUrl) {
        this.id = UUID.randomUUID().toString(); // Genera un ID único
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Getters y setters estándar
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCantidad() { return cantidad; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}