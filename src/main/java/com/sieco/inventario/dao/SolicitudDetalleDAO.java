package com.sieco.inventario.dao;

import com.sieco.inventario.modelo.SolicitudDetalle;
import com.sieco.inventario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class SolicitudDetalleDAO {

    public boolean agregar(SolicitudDetalle detalle) {
        String sql = "INSERT INTO solicitud_detalle (solicitud_id, material_id, equipo_id, cantidad_solicitada, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getSolicitudId());

        
            if (detalle.getMaterialId() != null) {
                stmt.setInt(2, detalle.getMaterialId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (detalle.getEquipoId() != null) {
                stmt.setInt(3, detalle.getEquipoId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            stmt.setInt(4, detalle.getCantidadSolicitada());
            stmt.setString(5, detalle.getObservaciones());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar detalle de solicitud: " + e.getMessage());
            return false;
        }
    }
    
    public List<SolicitudDetalle> obtenerPorSolicitud(int solicitudId) {
        List<SolicitudDetalle> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitud_detalle WHERE solicitud_id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, solicitudId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SolicitudDetalle detalle = new SolicitudDetalle();
                    detalle.setIdSolicitudDetalle(rs.getInt("id_solicitud_detalle"));
                    detalle.setSolicitudId(rs.getInt("solicitud_id"));
                    detalle.setMaterialId(rs.getObject("material_id") != null ? rs.getInt("material_id") : null);
                    detalle.setEquipoId(rs.getObject("equipo_id") != null ? rs.getInt("equipo_id") : null);
                    detalle.setCantidadSolicitada(rs.getInt("cantidad_solicitada"));
                    detalle.setObservaciones(rs.getString("observaciones"));

                    lista.add(detalle);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de la solicitud: " + e.getMessage());
        }

        return lista;
    }
    
 // Devuelve true si la solicitud tiene al menos un detalle con equipo/herramienta
    public boolean existeHerramientaEnSolicitud(int solicitudId) {
        String sql = "SELECT 1 FROM solicitud_detalle " +
                     "WHERE solicitud_id = ? AND equipo_id IS NOT NULL LIMIT 1";
        try (var c = ConexionBD.getConnection();
             var p = c.prepareStatement(sql)) {
            p.setInt(1, solicitudId);
            try (var rs = p.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("SolicitudDetalleDAO.existeHerramientaEnSolicitud: " + e.getMessage());
            return false;
        }
    }

    
   


}
