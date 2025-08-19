package com.sieco.inventario.dao;

import com.sieco.inventario.modelo.Herramienta;
import com.sieco.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class HerramientaDAO {

    public List<Herramienta> obtenerTodas() {
        List<Herramienta> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipo_herramienta";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Herramienta h = new Herramienta();
                h.setIdEquipoHerramienta(rs.getInt("id_equipo_herramienta"));
                h.setNombre(rs.getString("nombre"));
                h.setMarca(rs.getString("marca"));
                h.setModelo(rs.getString("modelo"));
                h.setTipo(rs.getString("tipo"));
                h.setEstado(rs.getString("estado"));
                h.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
                h.setObservaciones(rs.getString("observaciones"));
                lista.add(h);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener herramientas: " + e.getMessage());
        }

        return lista;
    }

    public boolean agregar(Herramienta herramienta) {
        String sql = "INSERT INTO equipo_herramienta (id_equipo_herramienta, nombre, marca, modelo, tipo, estado, fecha_incorporacion, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, herramienta.getIdEquipoHerramienta());
            stmt.setString(2, herramienta.getNombre());
            stmt.setString(3, herramienta.getMarca());
            stmt.setString(4, herramienta.getModelo());
            stmt.setString(5, herramienta.getTipo());
            stmt.setString(6, herramienta.getEstado());
            stmt.setDate(7, new java.sql.Date(herramienta.getFechaIncorporacion().getTime()));
            stmt.setString(8, herramienta.getObservaciones());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar herramienta: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Herramienta herramienta) {
        String sql = "UPDATE equipo_herramienta SET nombre=?, marca=?, modelo=?, tipo=?, estado=?, fecha_incorporacion=?, observaciones=? " +
                     "WHERE id_equipo_herramienta=?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, herramienta.getNombre());
            stmt.setString(2, herramienta.getMarca());
            stmt.setString(3, herramienta.getModelo());
            stmt.setString(4, herramienta.getTipo());
            stmt.setString(5, herramienta.getEstado());
            stmt.setDate(6, new java.sql.Date(herramienta.getFechaIncorporacion().getTime()));
            stmt.setString(7, herramienta.getObservaciones());
            stmt.setInt(8, herramienta.getIdEquipoHerramienta());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar herramienta: " + e.getMessage());
            return false;
        }
    }

 // Obtén SOLO herramientas disponibles para asignación
    public java.util.List<com.sieco.inventario.modelo.Herramienta> obtenerDisponibles() {
        java.util.List<com.sieco.inventario.modelo.Herramienta> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM equipo_herramienta WHERE estado = 'DISPONIBLE'";
        try (java.sql.Connection conn = com.sieco.inventario.util.ConexionBD.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                com.sieco.inventario.modelo.Herramienta h = new com.sieco.inventario.modelo.Herramienta();
                h.setIdEquipoHerramienta(rs.getInt("id_equipo_herramienta"));
                h.setNombre(rs.getString("nombre"));
                h.setMarca(rs.getString("marca"));
                h.setModelo(rs.getString("modelo"));
                h.setTipo(rs.getString("tipo"));
                h.setEstado(rs.getString("estado"));
                h.setFechaIncorporacion(rs.getDate("fecha_incorporacion"));
                h.setObservaciones(rs.getString("observaciones"));
                lista.add(h);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al obtener herramientas disponibles: " + e.getMessage());
        }
        return lista;
    }

    // Revisar Disponible
    public boolean estaDisponible(int id) {
        String sql = "SELECT estado FROM equipo_herramienta WHERE id_equipo_herramienta = ?";
        try (java.sql.Connection c = com.sieco.inventario.util.ConexionBD.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return "DISPONIBLE".equalsIgnoreCase(rs.getString("estado"));
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al validar disponibilidad de herramienta: " + e.getMessage());
        }
        return false;
    }

    // Marcar NO_DISPONIBLE al entregar
    public boolean marcarNoDisponible(int id) {
        String sql = "UPDATE equipo_herramienta SET estado = 'NO_ DISPONIBLE' WHERE id_equipo_herramienta = ?";
        try (java.sql.Connection c = com.sieco.inventario.util.ConexionBD.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Error al marcar herramienta NO_DISPONIBLE: " + e.getMessage());
            return false;
        }
    }

    // Sumar Stock (para devoluciones)
    public boolean marcarDisponible(int id) {
        String sql = "UPDATE equipo_herramienta SET estado = 'DISPONIBLE' WHERE id_equipo_herramienta = ?";
        try (java.sql.Connection c = com.sieco.inventario.util.ConexionBD.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Error al marcar herramienta DISPONIBLE: " + e.getMessage());
            return false;
        }
    }

}

