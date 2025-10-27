package Vista;

import Modelo.*;
import Modelo.Repository.EmpleadoRepository;
import Utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JCheckBox chkVerPassword;

    public LoginFrame() {
        setTitle("Bienvenido a Total Talent");
        setSize(450, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrado en pantalla
        setUndecorated(false); // Para que aparezcan bordes normales de ventana
        initComponents();
    }

    private void initComponents() {
        // ===== Colores pastel =====
        Color fondo = new Color(235, 245, 255);       // Azul pastel claro
        Color panelColor = new Color(250, 250, 255);  // Blanco azulado
        Color botonColor = new Color(174, 198, 255);  // Azul pastel medio
        Color textoColor = new Color(70, 70, 90);     // Gris azulado oscuro
        Color bordeInput = new Color(200, 200, 230);  // Azul muy suave

        // ===== Panel principal =====
        JPanel panel = new JPanel();
        panel.setBackground(fondo);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Título =====
        JLabel lblTitulo = new JLabel("Bienvenido a Total Talent", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(textoColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // ===== Subtítulo =====
        JLabel lblSubtitulo = new JLabel("Log In", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(textoColor);
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        // ===== Correo =====
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(textoColor);
        panel.add(lblCorreo, gbc);

        gbc.gridx = 1;
        txtCorreo = new JTextField(20);
        txtCorreo.setBackground(panelColor);
        txtCorreo.setForeground(textoColor);
        txtCorreo.setBorder(new LineBorder(bordeInput, 2, true)); // borde redondeado
        panel.add(txtCorreo, gbc);

        // ===== Contraseña =====
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(textoColor);
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(panelColor);
        txtPassword.setForeground(textoColor);
        txtPassword.setBorder(new LineBorder(bordeInput, 2, true));
        panel.add(txtPassword, gbc);

        // ===== Checkbox ver contraseña =====
        gbc.gridy = 4;
        gbc.gridx = 1;
        chkVerPassword = new JCheckBox("Mostrar contraseña");
        chkVerPassword.setBackground(fondo);
        chkVerPassword.setForeground(textoColor);
        chkVerPassword.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
        panel.add(chkVerPassword, gbc);

        // ===== Botón Login =====
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(botonColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new LineBorder(botonColor.darker(), 2, true)); // borde redondeado
        btnLogin.addActionListener(this::onLogin);
        panel.add(btnLogin, gbc);

        add(panel);
    }

    // ==== Lógica de autenticación  ====
    private void onLogin(ActionEvent ev) {
        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());
        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese correo y contraseña");
            return;
        }
        if (!ValidationUtils.isValidEmail(correo)) {
            JOptionPane.showMessageDialog(this, "El correo debe terminar en .com");
            return;
        }

        try {
            Connection conn = DatabaseConfig.getConnection();
            EmpleadoRepository repo = new EmpleadoRepository(conn);
            Empleado usuario = repo.buscarPorCorreo(correo);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado");
                conn.close();
                return;
            }
            if (!ValidationUtils.isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres");
                conn.close();
                return;
            }
            if (!password.equals(usuario.getPassword())) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
                conn.close();
                return;
            }

            // registrar login
            repo.registrarLogin(usuario.getId());
            DashboardFrame dash = new DashboardFrame(conn, usuario);
            dash.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexion: " + ex.getMessage());
        }
    }

}
