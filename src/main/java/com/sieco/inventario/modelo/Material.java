package com.sieco.inventario.modelo;

public class Material {
    private int idMaterial;
    private String nombre;
    private String tipo;
    private String unidadMedida;
    private int cantidadDisponible;
    private String observaciones;

    // Constructor vacío
    public Material() {
    }

    // Constructor con todos los campos
    public Material(int idMaterial, String nombre, String tipo, String unidadMedida, int cantidadDisponible, String observaciones) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
        this.tipo = tipo;
        this.unidadMedida = unidadMedida;
        this.cantidadDisponible = cantidadDisponible;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Material{" +
                "idMaterial=" + idMaterial +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", cantidadDisponible=" + cantidadDisponible +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
