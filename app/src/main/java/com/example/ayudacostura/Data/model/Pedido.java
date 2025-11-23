package com.example.ayudacostura.Data.model;

/**
 * Modelo que representa un pedido del cliente.
 * Contiene información como tipo de costura, medidas, estado e imagen opcional.
 */
public class Pedido {

    // Identificador único del pedido
    private String id;

    // Nombre del pedido (ej: "Arreglo de pantalón")
    private String nombre;

    // Descripción opcional del trabajo
    private String descripcion;

    // Tipo de costura (ej: "Arreglo", "Confección", etc.)
    private String tipoCostura;

    // Medidas asociadas al pedido
    private String medidas;

    // Estado del pedido (ej: "Pendiente", "Completado")
    private String estado;

    // URL de imagen almacenada en Firebase Storage
    private String imagenUrl;

    // Nombre del cliente asociado al pedido
    private String clienteNombre;

    // Constructor requerido por Firebase
    public Pedido() {}

    // Constructor completo cuando ya existe un ID
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

    // Getters y setters estándar
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

    // Métodos duplicados para compatibilidad con código anterior
    public String getCliente() { return clienteNombre; }
    public void setCliente(String cliente) { this.clienteNombre = cliente; }
}