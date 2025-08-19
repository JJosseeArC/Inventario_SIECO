package com.sieco.inventario;

import com.sieco.inventario.util.ConexionBD;
import java.sql.Connection;

public class ConexionPrueba {
    public static void main(String[] args) {
        Connection conn = ConexionBD.getConnection();

        if (conn != null) {
            System.out.println(" Conexión exitosa a la base de datos.");
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(" Error al cerrar la conexión: " + e.getMessage());
            }
        } else {
            System.out.println(" No se pudo establecer conexión.");
        }
    }
}
