package views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("Logística - Menú Principal");
        setSize(420, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Menú principal", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton listarButton = new JButton("Listar Vehículos");
        JButton agregarButton = new JButton("Agregar Vehículo");

        listarButton.addActionListener(e -> {
            new ListarVehiculosView().setVisible(true);
            dispose();
        });

        agregarButton.addActionListener(e -> {
            new AgregarVehiculoView().setVisible(true);
            dispose();
        });

        JPanel botones = new JPanel(new GridLayout(2, 1, 10, 10));
        botones.add(listarButton);
        botones.add(agregarButton);

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(botones, BorderLayout.CENTER);

        setContentPane(contenedor);
    }
}
