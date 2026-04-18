package views;

import domain.Marca;
import domain.Sucursal;
import domain.VehiculoTipo;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AgregarVehiculoView extends JFrame {

    private JTextField patenteField;
    private JComboBox<Marca> marcaCombo;
    private JTextField modeloField;
    private JTextField anioField;
    private JTextField capacidadCargaField;
    private JComboBox<Sucursal> sucursalCombo;
    private JComboBox<VehiculoTipo> tipoCombo;
    private JTextField kwhBaseField;
    private JTextField kmPorLitroField;
    private JTextField litrosExtraField;
    private JLabel kwhBaseLabel;
    private JLabel kmPorLitroLabel;
    private JLabel litrosExtraLabel;

    public AgregarVehiculoView() {
        setTitle("Logística - Agregar Vehículo");
        setSize(560, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
        actualizarCamposPorTipo();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        patenteField = new JTextField(20);
        marcaCombo = new JComboBox<>();
        modeloField = new JTextField(20);
        anioField = new JTextField(20);
        capacidadCargaField = new JTextField(20);
        sucursalCombo = new JComboBox<>();
        tipoCombo = new JComboBox<>(VehiculoTipo.values());
        kwhBaseField = new JTextField(20);
        kmPorLitroField = new JTextField(20);
        litrosExtraField = new JTextField(20);
        kwhBaseLabel = new JLabel("kWh base:");
        kmPorLitroLabel = new JLabel("Km por litro:");
        litrosExtraLabel = new JLabel("Litros extra:");

        for (Marca marca : Controlador.getMarcas()) {
            marcaCombo.addItem(marca);
        }
        for (Sucursal sucursal : Controlador.getSucursales()) {
            sucursalCombo.addItem(sucursal);
        }

        tipoCombo.addActionListener(e -> actualizarCamposPorTipo());

        int fila = 0;
        agregarCampo(formulario, gbc, fila++, "Patente:", patenteField);
        agregarCampo(formulario, gbc, fila++, "Marca:", marcaCombo);
        agregarCampo(formulario, gbc, fila++, "Modelo:", modeloField);
        agregarCampo(formulario, gbc, fila++, "Año:", anioField);
        agregarCampo(formulario, gbc, fila++, "Capacidad de carga:", capacidadCargaField);
        agregarCampo(formulario, gbc, fila++, "Sucursal:", sucursalCombo);
        agregarCampo(formulario, gbc, fila++, "Tipo:", tipoCombo);
        agregarCampo(formulario, gbc, fila++, kwhBaseLabel.getText(), kwhBaseField);
        agregarCampo(formulario, gbc, fila++, kmPorLitroLabel.getText(), kmPorLitroField);
        agregarCampo(formulario, gbc, fila++, litrosExtraLabel.getText(), litrosExtraField);

        JButton guardarButton = new JButton("Guardar");
        JButton volverButton = new JButton("Volver al menú");

        guardarButton.addActionListener(e -> guardarVehiculo());
        volverButton.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        JPanel acciones = new JPanel();
        acciones.add(volverButton);
        acciones.add(guardarButton);

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String textoLabel, java.awt.Component componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(textoLabel), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(componente, gbc);
    }

    private void actualizarCamposPorTipo() {
        VehiculoTipo tipo = (VehiculoTipo) tipoCombo.getSelectedItem();
        boolean esElectrico = tipo == VehiculoTipo.ELECTRICO;

        kwhBaseField.setEnabled(esElectrico);
        kmPorLitroField.setEnabled(!esElectrico);
        litrosExtraField.setEnabled(!esElectrico);

        if (esElectrico) {
            kmPorLitroField.setText("");
            litrosExtraField.setText("");
        } else {
            kwhBaseField.setText("");
        }
    }

    private void guardarVehiculo() {
        try {
            String patente = patenteField.getText();
            Marca marca = (Marca) marcaCombo.getSelectedItem();
            String modelo = modeloField.getText();
            int anio = Integer.parseInt(anioField.getText().trim());
            double capacidadCarga = Double.parseDouble(capacidadCargaField.getText().trim());
            Sucursal sucursal = (Sucursal) sucursalCombo.getSelectedItem();
            VehiculoTipo tipo = (VehiculoTipo) tipoCombo.getSelectedItem();

            double kwhBase = 0;
            double kmPorLitro = 0;
            double litrosExtra = 0;

            if (tipo == VehiculoTipo.ELECTRICO) {
                kwhBase = Double.parseDouble(kwhBaseField.getText().trim());
            } else {
                kmPorLitro = Double.parseDouble(kmPorLitroField.getText().trim());
                litrosExtra = Double.parseDouble(litrosExtraField.getText().trim());
            }

            Controlador.agregarVehiculo(
                    patente,
                    marca,
                    modelo,
                    anio,
                    capacidadCarga,
                    sucursal,
                    tipo,
                    kwhBase,
                    kmPorLitro,
                    litrosExtra
            );

            JOptionPane.showMessageDialog(this, "Vehículo agregado correctamente.");
            new MenuPrincipalView().setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisá los campos numéricos. Hay un dato con formato inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }
}