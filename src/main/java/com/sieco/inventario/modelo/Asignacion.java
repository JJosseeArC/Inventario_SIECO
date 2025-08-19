package com.sieco.inventario.modelo;

import java.util.Date;

public class Asignacion {
    private int idAsignacion;
    private int solicitudId;
    private Date fechaEntrega;
    private String estadoEntrega;
    private String observaciones;

    public Asignacion() {}

    public Asignacion(int idAsignacion, int solicitudId, Date fechaEntrega, String estadoEntrega, String observaciones) {
        this.idAsignacion = idAsignacion;
        this.solicitudId = solicitudId;
        this.fechaEntrega = fechaEntrega;
        this.estadoEntrega = estadoEntrega;
        this.observaciones = observaciones;
    }

    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }

    public int getSolicitudId() { return solicitudId; }
    public void setSolicitudId(int solicitudId) { this.solicitudId = solicitudId; }

    public Date getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(Date fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { this.estadoEntrega = estadoEntrega; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
