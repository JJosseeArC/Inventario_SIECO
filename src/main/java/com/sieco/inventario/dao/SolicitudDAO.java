package com.sieco.inventario.dao;

import com.sieco.inventario.modelo.Solicitud;
import com.sieco.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDAO {

    public boolean agregar(Solicitud solicitud) {
        String sql = "INSERT INTO solicitud (fecha, cuadrilla_id, supervisor_id, obra_text, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(solicitud.getFecha().getTime()));

            if (solicitud.getCuadrillaId() == null || solicitud.getCuadrillaId().isBlank())
                stmt.setNull(2, Types.VARCHAR);
            else
                stmt.setString(2, solicitud.getCuadrillaId());

            if (solicitud.getSupervisorId() == null || solicitud.getSupervisorId().isBlank())
                stmt.setNull(3, Types.VARCHAR);
            else
                stmt.setString(3, solicitud.getSupervisorId());

            if (solicitud.getObraText() == null || solicitud.getObraText().isBlank())
                stmt.setNull(4, Types.VARCHAR);
            else
                stmt.setString(4, solicitud.getObraText());

            stmt.setString(5, solicitud.getEstado());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar solicitud: " + e.getMessage());
            return false;
        }
    }

    public int agregarYObtenerId(Solicitud solicitud) {
        String sql = "INSERT INTO solicitud (fecha, cuadrilla_id, supervisor_id, obra_text, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, new java.sql.Date(solicitud.getFecha().getTime()));

            if (solicitud.getCuadrillaId() == null || solicitud.getCuadrillaId().isBlank())
                stmt.setNull(2, Types.VARCHAR);
            else
                stmt.setString(2, solicitud.getCuadrillaId());

            if (solicitud.getSupervisorId() == null || solicitud.getSupervisorId().isBlank())
                stmt.setNull(3, Types.VARCHAR);
            else
                stmt.setString(3, solicitud.getSupervisorId());

            if (solicitud.getObraText() == null || solicitud.getObraText().isBlank())
                stmt.setNull(4, Types.VARCHAR);
            else
                stmt.setString(4, solicitud.getObraText());

            stmt.setString(5, solicitud.getEstado());

            int filas = stmt.executeUpdate();
            if (filas == 0) return -1;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar solicitud y obtener ID: " + e.getMessage());
            return -1;
        }
    }

    public List<Solicitud> obtenerTodas() {
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT id_solicitud, fecha, cuadrilla_id, supervisor_id, obra_text, estado FROM solicitud ORDER BY id_solicitud DESC";
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Solicitud s = new Solicitud();
                s.setIdSolicitud(rs.getInt("id_solicitud"));
                s.setFecha(rs.getDate("fecha"));
                s.setCuadrillaId(rs.getString("cuadrilla_id"));  
                s.setSupervisorId(rs.getString("supervisor_id"));
                s.setObraText(rs.getString("obra_text"));         
                s.setEstado(rs.getString("estado"));
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener solicitudes: " + e.getMessage());
        }
        return lista;
    }

    public String obtenerEstado(int idSolicitud) {
        String sql = "SELECT estado FROM solicitud WHERE id_solicitud = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("estado");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener estado de la solicitud: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarEstado(int idSolicitud, String nuevoEstado) {
        String sql = "UPDATE solicitud SET estado = ? WHERE id_solicitud = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado de la solicitud: " + e.getMessage());
            return false;
        }
    }
}
