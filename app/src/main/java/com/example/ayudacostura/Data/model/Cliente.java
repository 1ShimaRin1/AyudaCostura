package com.example.ayudacostura.Data.model;

public class Cliente {
    private String id;
    private String nombre;
    private String telefono;

    public Cliente() {
        // Requerido por Firebase
    }

    public Cliente(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}