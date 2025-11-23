package com.example.ayudacostura.Data.model;

/**
 * Modelo que representa un Cliente dentro del proyecto.
 * Esta clase es usada por Firebase para mapear los datos.
 */
public class Cliente {

    // Identificador único del cliente
    private String id;

    // Nombre del cliente
    private String nombre;

    // Teléfono del cliente
    private String telefono;

    // Constructor vacío requerido por Firebase para deserializar datos
    public Cliente() {}

    // Constructor principal utilizado al crear un cliente manualmente
    public Cliente(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}