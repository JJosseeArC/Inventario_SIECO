package com.sieco.inventario.util;

import java.io.*;
import java.util.Scanner;

public class AdminAuth {
    private static final String ARCHIVO_CONTRASEÑA = "admin_pswd.txt";
    private static final String CONTRASEÑA_POR_DEFECTO = "admin123";

    // Se ejecuta al cargar la clase
    static {
        inicializarArchivoClave();
    }

    private static void inicializarArchivoClave() {
        File archivo = new File(ARCHIVO_CONTRASEÑA);
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write(CONTRASEÑA_POR_DEFECTO);
                writer.flush();
                System.out.println("Archivo de contraseña creado con la clave por defecto.");
            } catch (IOException e) {
                System.err.println("Error al crear archivo de contraseña: " + e.getMessage());
            }
        }
    }


    public static boolean solicitarClave(Scanner scanner) {
        System.out.print("Ingrese la contraseña de acceso: ");
        String input = scanner.nextLine();
        return input.equals(obtenerClaveDelArchivo());
    }

    private static String obtenerClaveDelArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CONTRASEÑA))) {
            return br.readLine().trim();
        } catch (IOException e) {
            System.err.println("Error al leer la clave de administrador: " + e.getMessage());
            return "";
        }
    }

    public static boolean verificarClave(String claveIngresada) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_CONTRASEÑA))) {
            String claveAlmacenada = reader.readLine();
            return claveAlmacenada != null && claveAlmacenada.equals(claveIngresada);
        } catch (IOException e) {
            System.err.println("Error al leer la clave de administrador: " + e.getMessage());
            return false;
        }
    }

    public static void modificarClave(Scanner scanner) {
        System.out.print("Ingrese la contraseña actual: ");
        String actual = scanner.nextLine();

        if (verificarClave(actual)) {
            System.out.print("Ingrese la nueva contraseña: ");
            String nueva = scanner.nextLine();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CONTRASEÑA))) {
                writer.write(nueva);
                System.out.println("Contraseña actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al modificar la contraseña: " + e.getMessage());
            }
        } else {
            System.out.println("Contraseña actual incorrecta.");
        }
    }
}

