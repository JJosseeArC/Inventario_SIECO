package com.sieco.inventario.ui;

import com.sieco.inventario.util.AdminAuth;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AdministracionPanel extends JPanel {

    public AdministracionPanel() {
        setLayout(new BorderLayout());

        
        JLabel titulo = new JLabel("Administración");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(titulo, BorderLayout.NORTH);

    
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Contraseña", new PasswordPanel());
        tabs.addTab("Supervisores", placeholder("Gestión de supervisores (En Proceso)"));
        tabs.addTab("Cuadrillas",  placeholder("Gestión de cuadrillas (En Proceso)"));
        tabs.addTab("Obras",       placeholder("Gestión de obras (En Proceso)"));

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel placeholder(String text) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    // =================== SUBPANEL: CAMBIO DE CONTRASEÑA ===================
    private static class PasswordPanel extends JPanel {
        private final JPasswordField pfActual  = new JPasswordField();
        private final JPasswordField pfNueva   = new JPasswordField();
        private final JPasswordField pfConfirm = new JPasswordField();
        private final JCheckBox cbMostrar      = new JCheckBox("Mostrar contraseñas");
        private final JButton btnGuardar       = new JButton("Guardar");
        private final JButton btnLimpiar       = new JButton("Limpiar");

        PasswordPanel() {
            setLayout(new BorderLayout());

            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

            JPanel card = new JPanel(new GridBagLayout());
            card.setBorder(new TitledBorder("Cambio de contraseña"));
            card.setMaximumSize(new Dimension(520, Integer.MAX_VALUE)); 
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 10, 6, 10);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;

            int row = 0;

            // Actual
            c.gridx = 0; c.gridy = row;
            card.add(new JLabel("Contraseña actual:"), c);
            c.gridx = 1; c.weightx = 1;
            card.add(pfActual, c);

            // Nueva
            row++; c.weightx = 0;
            c.gridx = 0; c.gridy = row;
            card.add(new JLabel("Nueva contraseña:"), c);
            c.gridx = 1; c.weightx = 1;
            card.add(pfNueva, c);

            // Confirmación
            row++; c.weightx = 0;
            c.gridx = 0; c.gridy = row;
            card.add(new JLabel("Confirmar nueva contraseña:"), c);
            c.gridx = 1; c.weightx = 1;
            card.add(pfConfirm, c);

            // Mostrar / ocultar
            row++;
            c.gridx = 1; c.gridy = row; c.weightx = 1;
            card.add(cbMostrar, c);

            // Botones
            JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            botones.add(btnGuardar);
            botones.add(btnLimpiar);

           
            center.add(Box.createVerticalStrut(10));
            center.add(card);
            center.add(Box.createVerticalStrut(10));
            center.add(botones);
            center.add(Box.createVerticalGlue());

            add(center, BorderLayout.CENTER);

            // Listeners
            cbMostrar.addActionListener(e -> toggleEcho());
            btnGuardar.addActionListener(e -> onGuardar());
            btnLimpiar.addActionListener(e -> limpiar());

            KeyAdapter enter = new KeyAdapter() {
                @Override public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) onGuardar();
                }
            };
            pfActual.addKeyListener(enter);
            pfNueva.addKeyListener(enter);
            pfConfirm.addKeyListener(enter);
        }

        private void toggleEcho() {
            Character def = (Character) UIManager.get("PasswordField.echoChar");
            char echo = cbMostrar.isSelected() ? 0 : (def != null ? def.charValue() : '\u2022');
            pfActual.setEchoChar(echo);
            pfNueva.setEchoChar(echo);
            pfConfirm.setEchoChar(echo);
        }

        private void onGuardar() {
            String actual  = new String(pfActual.getPassword()).trim();
            String nueva   = new String(pfNueva.getPassword()).trim();
            String confirm = new String(pfConfirm.getPassword()).trim();

            if (actual.isEmpty() || nueva.isEmpty() || confirm.isEmpty()) {
                msg("Completa todos los campos."); return;
            }
            if (!AdminAuth.verificarClave(actual)) {
                msg("La contraseña actual no es correcta."); return;
            }
            if (!nueva.equals(confirm)) {
                msg("La nueva contraseña y su confirmación no coinciden."); return;
            }
            if (nueva.length() < 6) {
                msg("La nueva contraseña debe tener al menos 6 caracteres."); return;
            }

            
            String fakeInput = actual + System.lineSeparator() + nueva + System.lineSeparator();
            try (Scanner sc = new Scanner(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)))) {
                AdminAuth.modificarClave(sc);
            } catch (Exception ex) {
                msg("No se pudo cambiar la contraseña: " + ex.getMessage());
                return;
            }

            msg("Contraseña actualizada.");
            limpiar();
        }

        private void limpiar() {
            pfActual.setText("");
            pfNueva.setText("");
            pfConfirm.setText("");
            pfActual.requestFocusInWindow();
        }

        private void msg(String s){ JOptionPane.showMessageDialog(this, s); }
    }
}

