package com.example.ayudacostura.Data.model;

public class Pedido {
    private String id;
    private String nombre;
    private String descripcion;
    private String tipoCostura;
    private String medidas;
    private String estado;
    private String imagenUrl;
    private String clienteNombre;

    public Pedido() {
        // Requerido por Firebase
    }

    public Pedido(String id, String nombre, String descripcion, String tipoCostura,
                  String medidas, String estado, String clienteNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCostura = tipoCostura;
        this.medidas = medidas;
        this.estado = estado;
        this.clienteNombre = clienteNombre;
    }

    // Getters y Setters
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

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    // Compatibilidad extra
    public String getCliente() { return clienteNombre; }
    public void setCliente(String cliente) { this.clienteNombre = cliente; }
}
