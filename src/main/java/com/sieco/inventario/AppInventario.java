package com.sieco.inventario;

import com.sieco.inventario.dao.HerramientaDAO;
import com.sieco.inventario.dao.MaterialDAO;
import com.sieco.inventario.modelo.Herramienta;
import com.sieco.inventario.modelo.Material;
import com.sieco.inventario.modelo.Solicitud;
import com.sieco.inventario.dao.SolicitudDAO;
import com.sieco.inventario.util.AdminAuth;
import com.sieco.inventario.modelo.SolicitudDetalle;
import com.sieco.inventario.dao.SolicitudDetalleDAO;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class AppInventario {

    private static final Scanner scanner = new Scanner(System.in);
    private static final MaterialDAO materialDAO = new MaterialDAO();
    private static final HerramientaDAO herramientaDAO = new HerramientaDAO();

    public static void main(String[] args) {
        int opcion;

        do {
            System.out.println("\n--- MENÚ INVENTARIO ---");
            System.out.println("1. Listar materiales");
            System.out.println("2. Agregar material");
            System.out.println("3. Actualizar material");
            System.out.println("4. Listar herramientas");
            System.out.println("5. Agregar herramienta");
            System.out.println("6. Actualizar herramienta");
            System.out.println("7. Nueva solicitud");
            System.out.println("8. Listar solicitudes");
            System.out.println("9. Aprobar / Rechazar solicitud");
            System.out.println("10. Registrar entrega");
            System.out.println("11. Modificar contraseña de Administrador");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> listarMateriales();
                case 2 -> agregarMaterial();
                case 3 -> actualizarMaterial();
                case 4 -> listarHerramientas();
                case 5 -> agregarHerramienta();
                case 6 -> actualizarHerramienta();
                case 7 -> agregarSolicitud();
                case 8 -> listarSolicitudes();
                case 9 -> aprobarRechazarSolicitud();
                case 10 -> registrarEntrega();
                case 11 -> AdminAuth.modificarClave(scanner);
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // === MÉTODO PARA REQUERIR CONTRASEÑA ===
    private static boolean requiereClave(Scanner scanner) {
        if (!AdminAuth.solicitarClave(scanner)) {
            System.out.println("Acceso denegado.");
            return false;
        }
        return true;
    }

    // === MATERIALES ===
    private static void listarMateriales() {
        List<Material> lista = materialDAO.obtenerTodos();
        for (Material m : lista) System.out.println(m);
    }

    private static void agregarMaterial() {
        if (!requiereClave(scanner)) return;

        Material m = new Material();
        System.out.print("ID del material: ");
        m.setIdMaterial(Integer.parseInt(scanner.nextLine()));
        System.out.print("Nombre: ");
        m.setNombre(scanner.nextLine());
        System.out.print("Tipo: ");
        m.setTipo(scanner.nextLine());
        System.out.print("Unidad de medida: ");
        m.setUnidadMedida(scanner.nextLine());
        System.out.print("Cantidad disponible: ");
        m.setCantidadDisponible(Integer.parseInt(scanner.nextLine()));
        System.out.print("Observaciones (opcional): ");
        m.setObservaciones(scanner.nextLine());

        System.out.println(materialDAO.agregar(m) ? "Material agregado." : "Error al agregar material.");
    }

    private static void actualizarMaterial() {
        if (!requiereClave(scanner)) return;

        Material m = new Material();
        System.out.print("ID del material a actualizar: ");
        m.setIdMaterial(Integer.parseInt(scanner.nextLine()));
        System.out.print("Nuevo nombre: ");
        m.setNombre(scanner.nextLine());
        System.out.print("Nuevo tipo: ");
        m.setTipo(scanner.nextLine());
        System.out.print("Nueva unidad de medida: ");
        m.setUnidadMedida(scanner.nextLine());
        System.out.print("Nueva cantidad disponible: ");
        m.setCantidadDisponible(Integer.parseInt(scanner.nextLine()));
        System.out.print("Observaciones: ");
        m.setObservaciones(scanner.nextLine());

        System.out.println(materialDAO.actualizar(m) ? "Material actualizado." : "Error al actualizar material.");
    }

    // === HERRAMIENTAS (por pieza: estado) ===
    private static void listarHerramientas() {
        List<Herramienta> lista = herramientaDAO.obtenerTodas();
        for (Herramienta h : lista) System.out.println(h);
    }

    private static void agregarHerramienta() {
        if (!requiereClave(scanner)) return;

        Herramienta h = new Herramienta();
        System.out.print("ID de la herramienta: ");
        h.setIdEquipoHerramienta(Integer.parseInt(scanner.nextLine()));
        System.out.print("Nombre: ");
        h.setNombre(scanner.nextLine());
        System.out.print("Marca: ");
        h.setMarca(scanner.nextLine());
        System.out.print("Modelo: ");
        h.setModelo(scanner.nextLine());
        System.out.print("Tipo: ");
        h.setTipo(scanner.nextLine());
        System.out.print("Estado (DISPONIBLE/NO_DISPONIBLE): ");
        h.setEstado(scanner.nextLine());
        System.out.print("Fecha de incorporación (YYYY-MM-DD): ");
        h.setFechaIncorporacion(Date.valueOf(scanner.nextLine()));
        System.out.print("Observaciones: ");
        h.setObservaciones(scanner.nextLine());

        System.out.println(herramientaDAO.agregar(h) ? "Herramienta agregada." : "Error al agregar herramienta.");
    }

    private static void actualizarHerramienta() {
        if (!requiereClave(scanner)) return;

        Herramienta h = new Herramienta();
        System.out.print("ID de la herramienta a actualizar: ");
        h.setIdEquipoHerramienta(Integer.parseInt(scanner.nextLine()));
        System.out.print("Nuevo nombre: ");
        h.setNombre(scanner.nextLine());
        System.out.print("Nueva marca: ");
        h.setMarca(scanner.nextLine());
        System.out.print("Nuevo modelo: ");
        h.setModelo(scanner.nextLine());
        System.out.print("Nuevo tipo: ");
        h.setTipo(scanner.nextLine());
        System.out.print("Nuevo estado (DISPONIBLE/NO_DISPONIBLE): ");
        h.setEstado(scanner.nextLine());
        System.out.print("Nueva fecha de incorporación (YYYY-MM-DD): ");
        h.setFechaIncorporacion(Date.valueOf(scanner.nextLine()));
        System.out.print("Observaciones: ");
        h.setObservaciones(scanner.nextLine());

        System.out.println(herramientaDAO.actualizar(h) ? "Herramienta actualizada." : "Error al actualizar herramienta.");
    }

    // === SOLICITUD ===
    private static void agregarSolicitud() {
        if (!AdminAuth.solicitarClave(scanner)) {
            System.out.println("Acceso denegado.");
            return;
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setFecha(new java.util.Date());

        System.out.print("ID de la cuadrilla (o ENTER si no aplica): ");
        String cuad = scanner.nextLine().trim();
        solicitud.setCuadrillaId(cuad.isEmpty() ? null : cuad);

        System.out.print("ID del supervisor (o ENTER si no aplica): ");
        String sup = scanner.nextLine().trim();
        solicitud.setSupervisorId(sup.isEmpty() ? null : sup);

        System.out.print("Obra (texto libre, opcional): ");
        String obraTxt = scanner.nextLine().trim();
        solicitud.setObraText(obraTxt.isEmpty() ? null : obraTxt);

        solicitud.setEstado("pendiente");

        SolicitudDAO solicitudDAO = new SolicitudDAO();
        int idGenerado = solicitudDAO.agregarYObtenerId(solicitud);
        if (idGenerado == -1) {
            System.out.println("Error al agregar la solicitud.");
            return;
        }
        System.out.println("Solicitud agregada con ID: " + idGenerado);

        SolicitudDetalleDAO detalleDAO = new SolicitudDetalleDAO();
        boolean agregarMas = true;

        while (agregarMas) {
            SolicitudDetalle detalle = new SolicitudDetalle();
            detalle.setSolicitudId(idGenerado);

            System.out.print("¿Deseas agregar un material o herramienta/equipo? (m/h): ");
            String tipo = scanner.nextLine().trim().toLowerCase();

            if (tipo.equals("m")) {
                System.out.print("ID del material: ");
                int idMat = Integer.parseInt(scanner.nextLine().trim());
                detalle.setMaterialId(idMat);
                detalle.setEquipoId(null);

                System.out.print("Cantidad solicitada: ");
                detalle.setCantidadSolicitada(Integer.parseInt(scanner.nextLine().trim()));

            } else if (tipo.equals("h")) {
                System.out.print("ID de la herramienta: ");
                int idHerr = Integer.parseInt(scanner.nextLine().trim());
                detalle.setEquipoId(idHerr);
                detalle.setMaterialId(null);

                // Herramienta = pieza única -> cantidad 1
                System.out.print("Cantidad solicitada (se forzará a 1): ");
                try { Integer.parseInt(scanner.nextLine().trim()); } catch (Exception ignored) {}
                detalle.setCantidadSolicitada(1);

            } else {
                System.out.println("Opción inválida.");
                continue;
            }

            System.out.print("Observaciones (opcional): ");
            detalle.setObservaciones(scanner.nextLine());

            if (detalleDAO.agregar(detalle)) {
                System.out.println("Detalle agregado.");
            } else {
                System.out.println("Error al agregar detalle.");
            }

            System.out.print("¿Deseas agregar otro detalle? (s/n): ");
            agregarMas = "s".equalsIgnoreCase(scanner.nextLine().trim());
        }

        System.out.println("Solicitud completa registrada con sus detalles.");
    }

    private static void aprobarRechazarSolicitud() {
        if (!requiereClave(scanner)) return;

        System.out.print("ID de la solicitud: ");
        int idSolicitud = Integer.parseInt(scanner.nextLine());

        System.out.print("¿Desea aprobar o rechazar la solicitud? (aprobar/rechazar): ");
        String decision = scanner.nextLine().trim().toLowerCase();

        SolicitudDAO solicitudDAO = new SolicitudDAO();
        SolicitudDetalleDAO detalleDAO = new SolicitudDetalleDAO();

        if ("rechazar".equals(decision)) {
            solicitudDAO.actualizarEstado(idSolicitud, "rechazada");
            System.out.println("Solicitud rechazada.");
            return;
        }

        // Validación
        List<SolicitudDetalle> detalles = detalleDAO.obtenerPorSolicitud(idSolicitud);
        boolean hayFaltantes = false;

        for (SolicitudDetalle d : detalles) {
            if (d.getMaterialId() != null) {
                int disp = materialDAO.obtenerCantidadMaterial(d.getMaterialId());
                if (disp < d.getCantidadSolicitada()) {
                    System.out.println("Faltante de material ID " + d.getMaterialId() +
                            " | Disponible: " + disp + " | Solicitado: " + d.getCantidadSolicitada());
                    hayFaltantes = true;
                }
            } else if (d.getEquipoId() != null) {
                boolean disponible = herramientaDAO.estaDisponible(d.getEquipoId()); // ✅ por estado
                boolean cantidadValida = d.getCantidadSolicitada() == 1;
                if (!disponible || !cantidadValida) {
                    System.out.println("Faltante/No válido herramienta ID " + d.getEquipoId() +
                            " | Disponible: " + (disponible ? 1 : 0) + " | Solicitado: " + d.getCantidadSolicitada());
                    hayFaltantes = true;
                }
            }
        }

        if (hayFaltantes) {
            solicitudDAO.actualizarEstado(idSolicitud, "rechazada");
            System.out.println("Solicitud rechazada por falta de stock/disponibilidad.");
        } else {
            solicitudDAO.actualizarEstado(idSolicitud, "aprobada");
            System.out.println("Solicitud aprobada. El descuento/actualización se hará al registrar la entrega.");
        }
    }

    private static void listarSolicitudes() {
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        List<Solicitud> solicitudes = solicitudDAO.obtenerTodas();

        if (solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
            return;
        }

        System.out.println("\n=== LISTADO DE SOLICITUDES ===");
        for (Solicitud s : solicitudes) {
            String cuadrilla = (s.getCuadrillaId() != null) ? s.getCuadrillaId() : "N/A";
            String supervisor = (s.getSupervisorId() != null) ? s.getSupervisorId() : "N/A";
            String obra = (s.getObraText() != null) ? s.getObraText() : "N/A";

            System.out.println("------------------------------");
            System.out.println("ID: " + s.getIdSolicitud());
            System.out.println("Fecha: " + s.getFecha());
            System.out.println("Cuadrilla: " + cuadrilla);
            System.out.println("Supervisor: " + supervisor);
            System.out.println("Obra: " + obra);
            System.out.println("Estado: " + s.getEstado());
        }
        System.out.println("------------------------------");
    }

    // == ENTREGA ==
    private static void registrarEntrega() {
        if (!requiereClave(scanner)) return;

        System.out.print("ID de la solicitud a entregar: ");
        int idSolicitud = Integer.parseInt(scanner.nextLine());

        SolicitudDAO solicitudDAO = new SolicitudDAO();
        String estado = solicitudDAO.obtenerEstado(idSolicitud);

        if (estado == null) {
            System.out.println("La solicitud no existe.");
            return;
        }
        if (!"aprobada".equalsIgnoreCase(estado)) {
            System.out.println("Solo se puede entregar una solicitud APROBADA. Estado actual: " + estado);
            return;
        }

        SolicitudDetalleDAO detalleDAO = new SolicitudDetalleDAO();
        List<SolicitudDetalle> detalles = detalleDAO.obtenerPorSolicitud(idSolicitud);

        // Revalidación antes de entregar
        boolean hayFaltantes = false;
        for (SolicitudDetalle d : detalles) {
            if (d.getMaterialId() != null) {
                int disp = materialDAO.obtenerCantidadMaterial(d.getMaterialId());
                if (disp < d.getCantidadSolicitada()) {
                    System.out.println("Faltante de material ID " + d.getMaterialId() +
                            " | Disponible: " + disp + " | Solicitado: " + d.getCantidadSolicitada());
                    hayFaltantes = true;
                }
            } else if (d.getEquipoId() != null) {
                boolean disponible = herramientaDAO.estaDisponible(d.getEquipoId());
                if (!disponible || d.getCantidadSolicitada() != 1) {
                    System.out.println("Herramienta ID " + d.getEquipoId() +
                            " no disponible o cantidad inválida.");
                    hayFaltantes = true;
                }
            }
        }
        if (hayFaltantes) {
            solicitudDAO.actualizarEstado(idSolicitud, "rechazada");
            System.out.println("Entrega cancelada. La solicitud se marcó como RECHAZADA por falta de stock al momento de la entrega.");
            return;
        }

        // Aplicar entrega
        boolean exito = true;
        for (SolicitudDetalle d : detalles) {
            if (d.getMaterialId() != null) {
                if (!materialDAO.restarCantidadMaterial(d.getMaterialId(), d.getCantidadSolicitada())) {
                    exito = false;
                }
            } else if (d.getEquipoId() != null) {
                if (!herramientaDAO.marcarNoDisponible(d.getEquipoId())) { // ✅ cambia estado
                    exito = false;
                }
            }
        }

        if (!exito) {
            System.out.println("Error al actualizar inventario. No se registró la entrega.");
            return;
        }

        solicitudDAO.actualizarEstado(idSolicitud, "entregada");
        System.out.println("Entrega realizada. Material descontado y herramientas marcadas NO_DISPONIBLE. Solicitud ENTREGADA.");
    }
}

