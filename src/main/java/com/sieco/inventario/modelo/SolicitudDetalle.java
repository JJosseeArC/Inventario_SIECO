package com.sieco.inventario.modelo;

public class SolicitudDetalle {
    private int idSolicitudDetalle;
    private int solicitudId;
    private Integer materialId; 
    private Integer equipoId;  
    private int cantidadSolicitada;
    private String observaciones;

    // Constructor vacío
    public SolicitudDetalle() {}

    // Constructor con todos los campos
    public SolicitudDetalle(int idSolicitudDetalle, int solicitudId, Integer materialId, Integer equipoId, int cantidadSolicitada, String observaciones) {
        this.idSolicitudDetalle = idSolicitudDetalle;
        this.solicitudId = solicitudId;
        this.materialId = materialId;
        this.equipoId = equipoId;
        this.cantidadSolicitada = cantidadSolicitada;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public int getIdSolicitudDetalle() { return idSolicitudDetalle; }
    public void setIdSolicitudDetalle(int idSolicitudDetalle) { this.idSolicitudDetalle = idSolicitudDetalle; }

    public int getSolicitudId() { return solicitudId; }
    public void setSolicitudId(int solicitudId) { this.solicitudId = solicitudId; }

    public Integer getMaterialId() { return materialId; }
    public void setMaterialId(Integer materialId) { this.materialId = materialId; }

    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
