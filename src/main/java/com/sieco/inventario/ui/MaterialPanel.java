package com.sieco.inventario.ui;

import com.sieco.inventario.dao.MaterialDAO;
import com.sieco.inventario.modelo.Material;
import com.sieco.inventario.util.AdminAuth;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MaterialPanel extends JPanel {
    private final MaterialDAO dao = new MaterialDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Nombre","Tipo","Unidad","Cantidad","Observaciones"}, 0) {
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table = new JTable(model);

    public MaterialPanel() {
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
            List<Material> list = dao.obtenerTodos();
            for (Material m : list) {
                model.addRow(new Object[]{
                        m.getIdMaterial(), m.getNombre(), m.getTipo(),
                        m.getUnidadMedida(), m.getCantidadDisponible(), m.getObservaciones()
                });
            }
        } catch (Exception ex) { 
            error(ex);
        }
    }

    private void onAdd() {
        if (!promptAdmin()) return;
        Material m = showEditor(null);
        if (m==null) return;

        try {
            if (dao.agregar(m)) {
                msg("Material agregado.");
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

        Material m = new Material();
        m.setIdMaterial((Integer) model.getValueAt(row,0));
        m.setNombre((String)  model.getValueAt(row,1));
        m.setTipo((String)    model.getValueAt(row,2));
        m.setUnidadMedida((String) model.getValueAt(row,3));
        m.setCantidadDisponible((Integer) model.getValueAt(row,4));
        m.setObservaciones((String) model.getValueAt(row,5));

        m = showEditor(m);
        if (m==null) return;

        try {
            if (dao.actualizar(m)) {
                msg("Material actualizado.");
                loadData();
            } else {
                msg("No se pudo actualizar.");
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    private Material showEditor(Material m) {
        JTextField tfNombre = new JTextField(m==null? "" : m.getNombre());
        JTextField tfTipo   = new JTextField(m==null? "" : m.getTipo());
        JTextField tfUni    = new JTextField(m==null? "" : m.getUnidadMedida());
        JSpinner spCant     = new JSpinner(new SpinnerNumberModel(m==null? 0 : m.getCantidadDisponible(), 0, 1_000_000, 1));
        JTextField tfObs    = new JTextField(m==null? "" : m.getObservaciones());

        JPanel p = new JPanel(new GridLayout(0,1,6,6));
        p.add(labeled("Nombre:", tfNombre));
        p.add(labeled("Tipo:", tfTipo));
        p.add(labeled("Unidad de medida:", tfUni));
        p.add(labeled("Cantidad:", spCant));
        p.add(labeled("Observaciones:", tfObs));

        int ok = JOptionPane.showConfirmDialog(this, p, m==null? "Agregar Material":"Editar Material", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return null;

        Material out = (m==null? new Material() : m);
        out.setNombre(tfNombre.getText().trim());
        out.setTipo(tfTipo.getText().trim());
        out.setUnidadMedida(tfUni.getText().trim());
        out.setCantidadDisponible(((Number)spCant.getValue()).intValue());
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
