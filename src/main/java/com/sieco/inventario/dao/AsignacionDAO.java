package com.sieco.inventario.dao;

import com.sieco.inventario.modelo.Asignacion;
import com.sieco.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    public boolean agregar(Asignacion a) {
        String sql = "INSERT INTO asignacion (solicitud_id, fecha_entrega, estado_entrega, observaciones) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getSolicitudId());
            stmt.setDate(2, new java.sql.Date(a.getFechaEntrega().getTime()));
            stmt.setString(3, a.getEstadoEntrega());
            stmt.setString(4, a.getObservaciones());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar asignación: " + e.getMessage());
            return false;
        }
    }

    public List<Asignacion> obtenerTodas() {
        List<Asignacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM asignacion ORDER BY id_asignacion DESC";
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Asignacion a = new Asignacion();
                a.setIdAsignacion(rs.getInt("id_asignacion"));
                a.setSolicitudId(rs.getInt("solicitud_id"));
                a.setFechaEntrega(rs.getDate("fecha_entrega"));
                a.setEstadoEntrega(rs.getString("estado_entrega"));
                a.setObservaciones(rs.getString("observaciones"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar asignaciones: " + e.getMessage());
        }
        return lista;
    }
}
