package com.sieco.inventario.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String url = "jdbc:mysql://localhost:3306/sieco";
    private static final String usuario = "root";
    private static final String contraseña = "Bodega2024";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }
}
