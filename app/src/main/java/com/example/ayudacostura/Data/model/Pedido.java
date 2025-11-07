package com.example.ayudacostura.Data.model;

public class Pedido {
    private String id;
    private String nombre;
    private String descripcion;
    private String tipoCostura;
    private String medidas;
    private String estado;

    public Pedido() {
        // Requerido por Firebase
    }

    public Pedido(String id, String nombre, String descripcion, String tipoCostura, String medidas, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCostura = tipoCostura;
        this.medidas = medidas;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoCostura() { return tipoCostura; }
    public void setTipoCostura(String tipoCostura) { this.tipoCostura = tipoCostura; }

    public String getMedidas() { return medidas; }
    public void setMedidas(String medidas) { this.medidas = medidas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
