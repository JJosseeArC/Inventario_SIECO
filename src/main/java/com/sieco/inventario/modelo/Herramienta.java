package com.sieco.inventario.modelo;

import java.util.Date;

public class Herramienta {
    private int idEquipoHerramienta;
    private String nombre;
    private String marca;
    private String modelo;
    private String tipo;
    private String estado;
    private Date fechaIncorporacion;
    private String observaciones;

    // Constructor vacío
    public Herramienta() {}

    // Constructor con todos los campos
    public Herramienta(int idEquipoHerramienta, String nombre, String marca, String modelo,
                       String tipo, String estado, Date fechaIncorporacion, String observaciones) {
        this.idEquipoHerramienta = idEquipoHerramienta;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaIncorporacion = fechaIncorporacion;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdEquipoHerramienta() {
        return idEquipoHerramienta;
    }

    public void setIdEquipoHerramienta(int idEquipoHerramienta) {
        this.idEquipoHerramienta = idEquipoHerramienta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIncorporacion() {
        return fechaIncorporacion;
    }

    public void setFechaIncorporacion(Date fechaIncorporacion) {
        this.fechaIncorporacion = fechaIncorporacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Herramienta{" +
                "idEquipoHerramienta=" + idEquipoHerramienta +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaIncorporacion=" + fechaIncorporacion +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
