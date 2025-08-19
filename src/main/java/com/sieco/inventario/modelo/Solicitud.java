package com.sieco.inventario.modelo;

import java.util.Date;

public class Solicitud {
    private int idSolicitud;
    private Date fecha;
    private String cuadrillaId;   
    private String supervisorId;  
    private String obraText;      // La obra será tipo texto, posteriormente se vinculará a un id
    private String estado;

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getCuadrillaId() { return cuadrillaId; }
    public void setCuadrillaId(String cuadrillaId) { this.cuadrillaId = cuadrillaId; }

    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }

    public String getObraText() { return obraText; }
    public void setObraText(String obraText) { this.obraText = obraText; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Solicitud{" +
                "idSolicitud=" + idSolicitud +
                ", fecha=" + fecha +
                ", cuadrillaId='" + cuadrillaId + '\'' +
                ", supervisorId='" + supervisorId + '\'' +
                ", obraText='" + obraText + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
    
    
}

