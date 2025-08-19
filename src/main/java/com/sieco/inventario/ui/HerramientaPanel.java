package com.sieco.inventario.ui;

import com.sieco.inventario.dao.HerramientaDAO;
import com.sieco.inventario.modelo.Herramienta;
import com.sieco.inventario.util.AdminAuth;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HerramientaPanel extends JPanel {
    private final HerramientaDAO dao = new HerramientaDAO();

    
    private static final int COL_ID        = 0;
    private static final int COL_NOMBRE    = 1;
    private static final int COL_MARCA     = 2;
    private static final int COL_MODELO    = 3;
    private static final int COL_TIPO      = 4;
    private static final int COL_ESTADO    = 5;
    private static final int COL_FECHA_INC = 6;
    private static final int COL_OBS       = 7;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Nombre","Marca","Modelo","Tipo","Estado","Fecha Inc.","Observaciones"}, 0) {
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable table = new JTable(model);

    public HerramientaPanel() {
        setLayout(new BorderLayout(8,8));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Agregar");
        JButton edit = new JButton("Editar");
        JButton refresh = new JButton("Actualizar");
        btns.add(add); btns.add(edit); btns.add(refresh);
        add(btns, BorderLayout.NORTH);

        add.addActionListener(e -> onAdd());
        edit.addActionListener(e -> onEdit());
        refresh.addActionListener(e -> loadData());

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Herramienta> list = dao.obtenerTodas(); // muestra todas; el estado indica disponibilidad
            for (Herramienta h : list) {
                model.addRow(new Object[]{
                        h.getIdEquipoHerramienta(),
                        h.getNombre(),
                        h.getMarca(),
                        h.getModelo(),
                        h.getTipo(),
                        h.getEstado(),
                        h.getFechaIncorporacion(),
                        h.getObservaciones()
                });
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void onAdd() {
        if (!promptAdmin()) return;
        Herramienta h = showEditor(null);
        if (h == null) return;

        try {
            if (dao.agregar(h)) {
                msg("Herramienta agregada.");
                loadData();
            } else {
                msg("No se pudo agregar.");
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void onEdit() {
        if (!promptAdmin()) return;
        int row = table.getSelectedRow();
        if (row < 0) { msg("Selecciona un registro."); return; }

        Herramienta h = new Herramienta();
        h.setIdEquipoHerramienta((Integer) model.getValueAt(row, COL_ID));
        h.setNombre((String) model.getValueAt(row, COL_NOMBRE));
        h.setMarca((String) model.getValueAt(row, COL_MARCA));
        h.setModelo((String) model.getValueAt(row, COL_MODELO));
        h.setTipo((String) model.getValueAt(row, COL_TIPO));
        h.setEstado((String) model.getValueAt(row, COL_ESTADO));
        h.setFechaIncorporacion((Date) model.getValueAt(row, COL_FECHA_INC));
        h.setObservaciones((String) model.getValueAt(row, COL_OBS));

        h = showEditor(h);
        if (h == null) return;

        try {
            if (dao.actualizar(h)) {
                msg("Herramienta actualizada.");
                loadData();
            } else {
                msg("No se pudo actualizar.");
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private Herramienta showEditor(Herramienta h) {
        JTextField tfNombre = new JTextField(h==null? "" : h.getNombre());
        JTextField tfMarca  = new JTextField(h==null? "" : h.getMarca());
        JTextField tfModelo = new JTextField(h==null? "" : h.getModelo());
        JTextField tfTipo   = new JTextField(h==null? "" : h.getTipo());
        JTextField tfEstado = new JTextField(h==null? "" : h.getEstado());

        JSpinner spFecha = new JSpinner(new SpinnerDateModel(
                h==null? new Date() : h.getFechaIncorporacion(),
                null, null, Calendar.DAY_OF_MONTH));
        spFecha.setEditor(new JSpinner.DateEditor(spFecha, "yyyy-MM-dd"));

        JTextField tfObs    = new JTextField(h==null? "" : h.getObservaciones());

        JPanel p = new JPanel(new GridLayout(0,1,6,6));
        p.add(labeled("Nombre:", tfNombre));
        p.add(labeled("Marca:", tfMarca));
        p.add(labeled("Modelo:", tfModelo));
        p.add(labeled("Tipo:", tfTipo));
        p.add(labeled("Estado:", tfEstado));
        p.add(labeled("Fecha incorporación:", spFecha));
        p.add(labeled("Observaciones:", tfObs));

        int ok = JOptionPane.showConfirmDialog(this, p, h==null? "Agregar Herramienta":"Editar Herramienta", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return null;

        Herramienta out = (h==null? new Herramienta() : h);
        out.setNombre(tfNombre.getText().trim());
        out.setMarca(tfMarca.getText().trim());
        out.setModelo(tfModelo.getText().trim());
        out.setTipo(tfTipo.getText().trim());
        out.setEstado(tfEstado.getText().trim());
        out.setFechaIncorporacion((Date) spFecha.getValue());
        out.setObservaciones(tfObs.getText().trim());
        return out;
    }

    private boolean promptAdmin() {
        String pass = JOptionPane.showInputDialog(this, "Clave de administrador:");
        if (pass == null) return false;
        return AdminAuth.verificarClave(pass.trim());
    }

    private JPanel labeled(String lab, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(8,0));
        p.add(new JLabel(lab), BorderLayout.WEST);
        p.add(c, BorderLayout.CENTER);
        return p;
    }
    private void msg(String s){ JOptionPane.showMessageDialog(this, s); }
    private void error(Exception e){ JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
}
