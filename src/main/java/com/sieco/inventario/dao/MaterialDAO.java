package com.sieco.inventario.dao;

import com.sieco.inventario.modelo.Material;
import com.sieco.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> obtenerTodos() {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM material";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Material m = new Material();
                m.setIdMaterial(rs.getInt("id_material"));
                m.setNombre(rs.getString("nombre"));
                m.setTipo(rs.getString("tipo"));
                m.setUnidadMedida(rs.getString("unidad_medida"));
                m.setCantidadDisponible(rs.getInt("cantidad_disponible")); 
                m.setObservaciones(rs.getString("observaciones"));
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener materiales: " + e.getMessage());
        }
        return lista;
    }

    public boolean agregar(Material material) {
        String sql = "INSERT INTO material (id_material, nombre, tipo, unidad_medida, cantidad_disponible, observaciones) " + // ✅
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, material.getIdMaterial());
            stmt.setString(2, material.getNombre());
            stmt.setString(3, material.getTipo());
            stmt.setString(4, material.getUnidadMedida());
            stmt.setInt(5, material.getCantidadDisponible()); // ✅
            stmt.setString(6, material.getObservaciones());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar material: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Material material) {
        String sql = "UPDATE material SET nombre=?, tipo=?, unidad_medida=?, cantidad_disponible=?, observaciones=? " + // ✅
                     "WHERE id_material=?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, material.getNombre());
            stmt.setString(2, material.getTipo());
            stmt.setString(3, material.getUnidadMedida());
            stmt.setInt(4, material.getCantidadDisponible()); // ✅
            stmt.setString(5, material.getObservaciones());
            stmt.setInt(6, material.getIdMaterial());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar material: " + e.getMessage());
            return false;
        }
    }

    public boolean restarCantidadMaterial(int materialId, int cantidadSolicitada) {
        String consulta = "SELECT cantidad_disponible FROM material WHERE id_material = ?"; // ✅
        String actualizacion = "UPDATE material SET cantidad_disponible = cantidad_disponible - ? WHERE id_material = ?"; // ✅

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cantidadActual = rs.getInt("cantidad_disponible"); // ✅
                    if (cantidadActual >= cantidadSolicitada) {
                        try (PreparedStatement stmtUpdate = conn.prepareStatement(actualizacion)) {
                            stmtUpdate.setInt(1, cantidadSolicitada);
                            stmtUpdate.setInt(2, materialId);
                            return stmtUpdate.executeUpdate() > 0;
                        }
                    } else {
                        System.out.println("No hay suficiente cantidad de material con id " + materialId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al restar cantidad de material: " + e.getMessage());
        }
        return false;
    }

    public int obtenerCantidadMaterial(int idMaterial) {
        String sql = "SELECT cantidad_disponible FROM material WHERE id_material = ?"; // ✅

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMaterial);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad_disponible"); // ✅
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cantidad de material: " + e.getMessage());
        }
        return 0;
    }
    
 // Sumar stock (para devoluciones)
    public boolean sumarCantidadMaterial(int materialId, int cantidad) {
        String sql = "UPDATE material " +
                     "SET cantidad_disponible = cantidad_disponible + ? " +
                     "WHERE id_material = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, materialId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al sumar cantidad de material: " + e.getMessage());
            return false;
        }
    }

}

