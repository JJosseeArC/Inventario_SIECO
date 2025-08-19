package com.sieco.inventario.ui;

import com.sieco.inventario.dao.HerramientaDAO;
import com.sieco.inventario.dao.MaterialDAO;
import com.sieco.inventario.dao.SolicitudDAO;
import com.sieco.inventario.dao.SolicitudDetalleDAO;
import com.sieco.inventario.modelo.Herramienta;
import com.sieco.inventario.modelo.Material;
import com.sieco.inventario.modelo.Solicitud;
import com.sieco.inventario.modelo.SolicitudDetalle;
import com.sieco.inventario.util.AdminAuth;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitudPanel extends JPanel {

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();
    private final SolicitudDetalleDAO detalleDAO = new SolicitudDetalleDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final HerramientaDAO herramientaDAO = new HerramientaDAO();

    private final DefaultTableModel solicitudesModel = new DefaultTableModel(
            new Object[]{"ID","Fecha","Cuadrilla","Supervisor","Obra","Incluye herramienta","Estado"}, 0) {
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable tbSolicitudes = new JTable(solicitudesModel);

    private final JTextField tfCuadrilla  = new JTextField();
    private final JTextField tfSupervisor = new JTextField();
    private final JTextField tfObra       = new JTextField();

    private static final int D_COL_TIPO   = 0;
    private static final int D_COL_IDREF  = 1;
    private static final int D_COL_NOMBRE = 2;
    private static final int D_COL_CANT   = 3;
    private static final int D_COL_OBS    = 4;

    private final DefaultTableModel detallesModel = new DefaultTableModel(
            new Object[]{"Tipo","ID Ref","Nombre","Cantidad","Obs."}, 0) {
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable tbDetalles = new JTable(detallesModel);

    public SolicitudPanel() {
        setLayout(new BorderLayout(8,8));

        // ----- Top
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Solicitudes"), BorderLayout.NORTH);
        top.add(new JScrollPane(tbSolicitudes), BorderLayout.CENTER);

        JButton btnRefresh         = new JButton("Actualizar");
        JButton btnVerDetalles     = new JButton("Ver detalles");
        JButton btnAprobarRechazar = new JButton("Aprobar / Rechazar");
        JButton btnEntregar        = new JButton("Registrar entrega");
        JButton btnDevolver        = new JButton("Registrar devolución"); 
        JPanel topBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBtns.add(btnRefresh);
        topBtns.add(btnVerDetalles);
        topBtns.add(btnAprobarRechazar);
        topBtns.add(btnEntregar);
        topBtns.add(btnDevolver);
        top.add(topBtns, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> cargarSolicitudes());
        btnVerDetalles.addActionListener(e -> onVerDetalles());
        btnAprobarRechazar.addActionListener(e -> onAprobarRechazar());
        btnEntregar.addActionListener(e -> onRegistrarEntrega());
        btnDevolver.addActionListener(e -> onRegistrarDevolucion());

        
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Nueva solicitud"), BorderLayout.NORTH);

        tfCuadrilla.setColumns(36);
        tfSupervisor.setColumns(36);
        tfObra.setColumns(36);
        Dimension tfSize = new Dimension(420, tfCuadrilla.getPreferredSize().height);
        tfCuadrilla.setPreferredSize(tfSize);
        tfSupervisor.setPreferredSize(tfSize);
        tfObra.setPreferredSize(tfSize);

        JPanel form = new JPanel(new GridLayout(0,1,6,6));
        form.add(labeled("Cuadrilla:", tfCuadrilla));
        form.add(labeled("Supervisor:", tfSupervisor));
        form.add(labeled("Obra:", tfObra));

        JButton btnAddMat   = new JButton("Agregar Material...");
        JButton btnAddHer   = new JButton("Agregar Herramienta...");
        JButton btnDelDet   = new JButton("Eliminar detalle");
        JButton btnGuardar  = new JButton("Guardar solicitud");
        JPanel detBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detBtns.add(btnAddMat); detBtns.add(btnAddHer); detBtns.add(btnDelDet); detBtns.add(btnGuardar);

        btnAddMat.addActionListener(e -> agregarDetalle(true));
        btnAddHer.addActionListener(e -> agregarDetalle(false));
        btnDelDet.addActionListener(e -> { int r = tbDetalles.getSelectedRow(); if (r >= 0) detallesModel.removeRow(r); });
        btnGuardar.addActionListener(e -> onGuardarSolicitud());

        configurarAnchosDetalles();
        bottom.add(form, BorderLayout.WEST);
        bottom.add(new JScrollPane(tbDetalles), BorderLayout.CENTER);
        bottom.add(detBtns, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        cargarSolicitudes();
    }

    private void configurarAnchosDetalles() {
        TableColumnModel cm = tbDetalles.getColumnModel();
        cm.getColumn(D_COL_TIPO).setPreferredWidth(90);
        cm.getColumn(D_COL_IDREF).setPreferredWidth(70);
        cm.getColumn(D_COL_NOMBRE).setPreferredWidth(180);
        cm.getColumn(D_COL_CANT).setPreferredWidth(70);
        cm.getColumn(D_COL_OBS).setPreferredWidth(160);
        tbDetalles.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private JPanel labeled(String lab, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(8,0));
        p.add(new JLabel(lab), BorderLayout.WEST);
        p.add(c, BorderLayout.CENTER);
        return p;
    }
    private void msg(String s){ JOptionPane.showMessageDialog(this, s); }
    private void error(Exception e){ JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }

    
    private void cargarSolicitudes() {
        solicitudesModel.setRowCount(0);
        try {
            List<Solicitud> all = solicitudDAO.obtenerTodas();
            for (Solicitud s : all) {
                boolean incluyeHerr = detalleDAO.existeHerramientaEnSolicitud(s.getIdSolicitud());
                solicitudesModel.addRow(new Object[]{
                        s.getIdSolicitud(),
                        s.getFecha(),
                        s.getCuadrillaId(),
                        s.getSupervisorId(),
                        s.getObraText(),
                        incluyeHerr ? "Sí" : "No", 
                        s.getEstado()
                });
            }
        } catch (Exception ex) { error(ex); }
    }

    // ===== Guardar nueva
    private void onGuardarSolicitud() {
        String cuadrilla  = tfCuadrilla.getText().trim();
        String supervisor = tfSupervisor.getText().trim();
        String obra       = tfObra.getText().trim();
        if (cuadrilla.isBlank() || supervisor.isBlank() || obra.isBlank()) {
            msg("Cuadrilla, Supervisor y Obra son obligatorios."); return;
        }
        try {
            Solicitud s = new Solicitud();
            s.setFecha(new Date());
            s.setCuadrillaId(cuadrilla);
            s.setSupervisorId(supervisor);
            s.setObraText(obra);
            s.setEstado("pendiente");

            int id = solicitudDAO.agregarYObtenerId(s);

            for (int i=0; i<detallesModel.getRowCount(); i++) {
                String tipo = (String) detallesModel.getValueAt(i, D_COL_TIPO);
                int refId   = (Integer) detallesModel.getValueAt(i, D_COL_IDREF);
                int cant    = (Integer) detallesModel.getValueAt(i, D_COL_CANT);
                String obs  = (String) detallesModel.getValueAt(i, D_COL_OBS);

                SolicitudDetalle d = new SolicitudDetalle();
                d.setSolicitudId(id);
                d.setCantidadSolicitada(cant);
                d.setObservaciones(obs);
                if ("Material".equals(tipo)) d.setMaterialId(refId);
                else d.setEquipoId(refId);

                if (!detalleDAO.agregar(d)) { msg("No se pudo guardar un detalle. Revisa la BD."); break; }
            }

            msg("Solicitud guardada con ID: " + id);
            tfCuadrilla.setText(""); tfSupervisor.setText(""); tfObra.setText("");
            detallesModel.setRowCount(0);
            cargarSolicitudes();

        } catch (Exception ex) { error(ex); }
    }

    // ===== Ver detalles
    private void onVerDetalles() {
        int row = tbSolicitudes.getSelectedRow();
        if (row < 0) { msg("Selecciona una solicitud para ver sus detalles."); return; }
        int id = (Integer) solicitudesModel.getValueAt(row, 0);

        try {
            List<SolicitudDetalle> dets = detalleDAO.obtenerPorSolicitud(id);
            if (dets.isEmpty()) { msg("La solicitud no tiene detalles."); return; }

            DefaultTableModel m = new DefaultTableModel(
                    new Object[]{"Tipo","ID Ref","Nombre","Cantidad","Obs."}, 0) {
                public boolean isCellEditable(int r,int c){return false;}
            };

            List<Material> mats = materialDAO.obtenerTodos();
            List<Herramienta> hers = herramientaDAO.obtenerTodas();
            for (SolicitudDetalle d : dets) {
                String tipo = (d.getMaterialId()!=null) ? "Material" : "Herramienta";
                int idRef   = (d.getMaterialId()!=null) ? d.getMaterialId() : d.getEquipoId();
                String nombre = (d.getMaterialId()!=null)
                        ? nombreMaterial(mats, idRef)
                        : nombreHerramienta(hers, idRef);
                m.addRow(new Object[]{ tipo, idRef, nombre, d.getCantidadSolicitada(), d.getObservaciones() });
            }

            JTable t = new JTable(m);
            JScrollPane sp = new JScrollPane(t);
            sp.setPreferredSize(new Dimension(700, 280));
            JOptionPane.showMessageDialog(this, sp, "Detalles de la solicitud " + id, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) { error(ex); }
    }
    private String nombreMaterial(List<Material> mats, int id) {
        for (Material m : mats) if (m.getIdMaterial()==id) return m.getNombre();
        return "(ID " + id + ")";
    }
    private String nombreHerramienta(List<Herramienta> hs, int id) {
        for (Herramienta h : hs) if (h.getIdEquipoHerramienta()==id) return h.getNombre();
        return "(ID " + id + ")";
    }

    // ===== Aprobar / Rechazar
    private void onAprobarRechazar() {
        if (!promptAdmin()) return;
        int row = tbSolicitudes.getSelectedRow();
        if (row < 0) { msg("Selecciona una solicitud."); return; }
        int id = (Integer) solicitudesModel.getValueAt(row, 0);

        String[] opts = {"Aprobar", "Rechazar", "Cancelar"};
        int opt = JOptionPane.showOptionDialog(this, "Solicitud " + id + ":",
                "Aprobar / Rechazar", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
        if (opt == 2 || opt == JOptionPane.CLOSED_OPTION) return;

        try {
            if (opt == 1) { // Rechazar
                solicitudDAO.actualizarEstado(id, "rechazada");
                msg("Solicitud rechazada.");
                cargarSolicitudes();
                return;
            }

            List<SolicitudDetalle> detalles = detalleDAO.obtenerPorSolicitud(id);
            boolean faltante = false;
            for (SolicitudDetalle d : detalles) {
                if (d.getMaterialId() != null) {
                    int disp = materialDAO.obtenerCantidadMaterial(d.getMaterialId());
                    if (disp < d.getCantidadSolicitada()) { faltante = true; break; }
                } else if (d.getEquipoId() != null) {
                    boolean disponible = herramientaDAO.estaDisponible(d.getEquipoId());
                    boolean cantOk = d.getCantidadSolicitada()==1;
                    if (!disponible || !cantOk) { faltante = true; break; }
                }
            }

            if (faltante) {
                solicitudDAO.actualizarEstado(id, "rechazada");
                msg("Solicitud rechazada por falta de stock/disponibilidad.");
            } else {
                solicitudDAO.actualizarEstado(id, "aprobada");
                msg("Solicitud aprobada. Se aplicará la entrega según tipo.");
            }
            cargarSolicitudes();
        } catch (Exception ex) { error(ex); }
    }

    // ===== Registrar entrega
    private void onRegistrarEntrega() {
        if (!promptAdmin()) return;
        int row = tbSolicitudes.getSelectedRow();
        if (row < 0) { msg("Selecciona una solicitud."); return; }
        int id = (Integer) solicitudesModel.getValueAt(row, 0);

        try {
            String estado = solicitudDAO.obtenerEstado(id);
            if (!"aprobada".equalsIgnoreCase(estado)) {
                msg("Solo se puede entregar una solicitud APROBADA. Estado actual: " + estado);
                return;
            }

            List<SolicitudDetalle> detalles = detalleDAO.obtenerPorSolicitud(id);

            // Revalidar antes de entregar
            for (SolicitudDetalle d : detalles) {
                if (d.getMaterialId() != null) {
                    int disp = materialDAO.obtenerCantidadMaterial(d.getMaterialId());
                    if (disp < d.getCantidadSolicitada()) { msg("Stock insuficiente para material ID " + d.getMaterialId()); return; }
                } else if (d.getEquipoId() != null) {
                    boolean disponible = herramientaDAO.estaDisponible(d.getEquipoId());
                    if (!disponible || d.getCantidadSolicitada()!=1) { msg("Herramienta ID " + d.getEquipoId() + " no disponible o cantidad inválida."); return; }
                }
            }

            // Aplicar entrega
            for (SolicitudDetalle d : detalles) {
                if (d.getMaterialId() != null) {
                    if (!materialDAO.restarCantidadMaterial(d.getMaterialId(), d.getCantidadSolicitada())) {
                        msg("No se pudo restar material ID " + d.getMaterialId()); return;
                    }
                } else if (d.getEquipoId() != null) {
                    if (!herramientaDAO.marcarNoDisponible(d.getEquipoId())) {
                        msg("No se pudo marcar NO_DISPONIBLE la herramienta ID " + d.getEquipoId()); return;
                    }
                }
            }

            boolean incluyeHerr = detalleDAO.existeHerramientaEnSolicitud(id);
            solicitudDAO.actualizarEstado(id, incluyeHerr ? "pendiente_de_devolucion" : "entregada");
            msg(incluyeHerr ? "Entrega registrada. Estado: PENDIENTE_DE_DEVOLUCION." : "Entrega registrada. Estado: ENTREGADA.");
            cargarSolicitudes();

        } catch (Exception ex) { error(ex); }
    }

    // ===== Registrar devolución
    private void onRegistrarDevolucion() {
        if (!promptAdmin()) return;
        int row = tbSolicitudes.getSelectedRow();
        if (row < 0) { msg("Selecciona una solicitud."); return; }
        int id = (Integer) solicitudesModel.getValueAt(row, 0);

        try {
            String estado = solicitudDAO.obtenerEstado(id);
            if (!"pendiente_de_devolucion".equalsIgnoreCase(estado)
                    && !"entregada".equalsIgnoreCase(estado)) {
                msg("Solo se permiten devoluciones para estados ENTREGADA o PENDIENTE_DE_DEVOLUCION.");
                return;
            }

            List<SolicitudDetalle> detalles = detalleDAO.obtenerPorSolicitud(id);
            if (detalles.isEmpty()) { msg("La solicitud no tiene detalles."); return; }

            boolean algunaHerrPendiente = false;
            boolean devolvioAlgo = false;

            
            for (SolicitudDetalle d : detalles) {
                if (d.getMaterialId() != null) {
                    JPanel p = new JPanel(new GridLayout(0,1,6,6));
                    p.add(new JLabel("Material ID " + d.getMaterialId() + " | Cantidad entregada: " + d.getCantidadSolicitada()));
                    JSpinner sp = new JSpinner(new SpinnerNumberModel(0, 0, d.getCantidadSolicitada(), 1));
                    p.add(labeled("Cantidad a devolver:", sp));
                    int ok = JOptionPane.showConfirmDialog(this, p, "Devolver material", JOptionPane.OK_CANCEL_OPTION);
                    if (ok == JOptionPane.OK_OPTION) {
                        int devolver = ((Number) sp.getValue()).intValue();
                        if (devolver > 0) {
                            if (!materialDAO.sumarCantidadMaterial(d.getMaterialId(), devolver)) {
                                msg("No se pudo devolver material ID " + d.getMaterialId()); return;
                            }
                            devolvioAlgo = true;
                        }
                    }
                } else if (d.getEquipoId() != null) {
                    JCheckBox cb = new JCheckBox("Marcar DISPONIBLE herramienta ID " + d.getEquipoId());
                    cb.setSelected(true);
                    int ok = JOptionPane.showConfirmDialog(this, cb, "Devolver herramienta", JOptionPane.OK_CANCEL_OPTION);
                    if (ok == JOptionPane.OK_OPTION) {
                        if (cb.isSelected()) {
                            if (!herramientaDAO.marcarDisponible(d.getEquipoId())) {
                                msg("No se pudo marcar DISPONIBLE la herramienta ID " + d.getEquipoId()); return;
                            }
                            devolvioAlgo = true;
                        } else {
                            algunaHerrPendiente = true; // No devolver herramienta
                        }
                    } else {
                        algunaHerrPendiente = true; // Si se omite o cancela se considera pendiente
                    }
                }
            }

            if (!devolvioAlgo) { msg("No se registró ninguna devolución."); return; }

            // Estado final
            if (algunaHerrPendiente) {
                solicitudDAO.actualizarEstado(id, "pendiente_de_devolucion");
                msg("Devolución parcial registrada. Estado: PENDIENTE_DE_DEVOLUCION.");
            } else {
                solicitudDAO.actualizarEstado(id, "devuelta");
                msg("Devolución registrada. Estado: DEVUELTA.");
            }
            cargarSolicitudes();

        } catch (Exception ex) { error(ex); }
    }

    // ===== Auth
    private boolean promptAdmin() {
        String pass = JOptionPane.showInputDialog(this, "Clave de administrador:");
        if (pass == null) return false;
        return AdminAuth.verificarClave(pass.trim());
    }

    // ===== Agregar detalle 
    private void agregarDetalle(boolean esMaterial) {
        try {
            if (esMaterial) {
                List<Material> mats = materialDAO.obtenerTodos();
                if (mats.isEmpty()) { msg("No hay materiales."); return; }

                JComboBox<Material> cb = new JComboBox<>(mats.toArray(new Material[0]));
                cb.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(
                        value==null? "" : value.getIdMaterial()+" - "+value.getNombre()+" (disp: "+value.getCantidadDisponible()+")"));
                JSpinner sp = new JSpinner(new SpinnerNumberModel(1,1,1_000_000,1));
                JTextField tfObs = new JTextField();

                JPanel p = new JPanel(new GridLayout(0,1,6,6));
                p.add(labeled("Material:", cb));
                p.add(labeled("Cantidad:", sp));
                p.add(labeled("Observaciones:", tfObs));

                int ok = JOptionPane.showConfirmDialog(this, p, "Agregar material", JOptionPane.OK_CANCEL_OPTION);
                if (ok != JOptionPane.OK_OPTION) return;

                Material sel = (Material) cb.getSelectedItem();
                detallesModel.addRow(new Object[]{"Material", sel.getIdMaterial(), sel.getNombre(),
                        ((Number)sp.getValue()).intValue(), tfObs.getText().trim()});

            } else {
                List<Herramienta> todas = herramientaDAO.obtenerTodas();
                List<Herramienta> disponibles = new ArrayList<>();
                for (Herramienta h : todas) if ("DISPONIBLE".equalsIgnoreCase(h.getEstado())) disponibles.add(h);
                if (disponibles.isEmpty()) { msg("No hay herramientas disponibles."); return; }

                JComboBox<Herramienta> cb = new JComboBox<>(disponibles.toArray(new Herramienta[0]));
                cb.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                        new JLabel(value==null? "" : value.getIdEquipoHerramienta()+" - "+value.getNombre()+" ("+value.getEstado()+")"));

                JSpinner sp = new JSpinner(new SpinnerNumberModel(1,1,1,1));
                sp.setEnabled(false);
                JTextField tfObs = new JTextField();

                JPanel p = new JPanel(new GridLayout(0,1,6,6));
                p.add(labeled("Herramienta:", cb));
                p.add(labeled("Cantidad:", sp));
                p.add(labeled("Observaciones:", tfObs));

                int ok = JOptionPane.showConfirmDialog(this, p, "Agregar herramienta", JOptionPane.OK_CANCEL_OPTION);
                if (ok != JOptionPane.OK_OPTION) return;

                Herramienta sel = (Herramienta) cb.getSelectedItem();
                detallesModel.addRow(new Object[]{"Herramienta", sel.getIdEquipoHerramienta(), sel.getNombre(), 1, tfObs.getText().trim()});
            }
        } catch (Exception ex) { error(ex); }
    }
}

