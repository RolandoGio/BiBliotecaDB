package gui;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("Sistema de Biblioteca - Colegio Amigos de Don Bosco");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblHeader = new JLabel("Sistema de Biblioteca - Colegio Amigos de Don Bosco", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(4, 52, 84)); // Azul
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setPreferredSize(new Dimension(getWidth(), 50));
        add(lblHeader, BorderLayout.NORTH);

        // Body content
        JPanel panelBody = new JPanel();
        panelBody.setLayout(new BoxLayout(panelBody, BoxLayout.Y_AXIS));
        panelBody.setBackground(new Color(255, 255, 255, 197)); // Gris claro
        panelBody.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblVision = new JLabel("Visión: Ser el mejor recurso educativo en acceso rápido y eficiente al aprendizaje.");
        lblVision.setFont(new Font("Arial", Font.ITALIC, 14));
        lblVision.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMision = new JLabel("Misión: Proveer herramientas tecnológicas para la formación integral de nuestros usuarios.");
        lblMision.setFont(new Font("Arial", Font.ITALIC, 14));
        lblMision.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAutores = new JLabel("Autores:");
        lblAutores.setFont(new Font("Arial", Font.BOLD, 16));
        lblAutores.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAutoresNombres = new JLabel("<html>"
                + "Jennifer Patricia Zaldaña Beltrán - ZB212784<br>"
                + "Rolando Giovanni Guevara Mártir - GM202436<br>"
                + "Ronald Vladimir Urías Orellana - RO230537<br>"
                + "María José Custodio Vásquez - CV242178</html>");
        lblAutoresNombres.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAutoresNombres.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGracias = new JLabel("¡Gracias por utilizar el sistema del Colegio Amigos de Don Bosco!");
        lblGracias.setFont(new Font("Arial", Font.BOLD, 16));
        lblGracias.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGracias.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panelBody.add(lblVision);
        panelBody.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
        panelBody.add(lblMision);
        panelBody.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado
        panelBody.add(lblAutores);
        panelBody.add(lblAutoresNombres);
        panelBody.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado
        panelBody.add(lblGracias);

        add(panelBody, BorderLayout.CENTER);

        // Footer
        JButton btnSiguiente = new JButton("Siguiente");
        btnSiguiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnSiguiente.setBackground(new Color(4, 52, 84)); // Azul
        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setFocusPainted(false);
        btnSiguiente.addActionListener(e -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            dispose();
        });
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(240, 240, 240)); // Gris claro
        panelFooter.add(btnSiguiente);

        add(panelFooter, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centrar la ventana
    }

    // Método main para arrancar la aplicación desde WelcomeScreen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}
