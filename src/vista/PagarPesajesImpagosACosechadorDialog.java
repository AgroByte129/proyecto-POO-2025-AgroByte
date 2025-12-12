package vista;

import controlador.ControladorProduccion;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

//Arturo Felipe Gómez Senn

public class PagarPesajesImpagosACosechadorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonAceptar;
    private JButton buttonCancel;
    private JLabel totalAPagarLabel;
    private JLabel showTotalAPagarLabel;
    private JLabel pagoDePesajesPendientesLabel;
    private JTextField textFieldIDPago;
    private JTextField textFieldRUTCosechador;
    private JLabel IDPagoLabel;
    private JLabel RUTCosechadorLabel;
    private JTable tableDetalle;

    private DefaultTableModel tableModel;
    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public PagarPesajesImpagosACosechadorDialog() {

        setTitle("Pago de Pesajes Pendientes");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAceptar);

        String[] columnas = {"ID", "Fecha", "Calidad", "Kilos", "Precio Kg.", "Monto", "Estado"};

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDetalle.setModel(tableModel);

        buttonAceptar.setEnabled(false); // Deshabilitado hasta encontrar una deuda
        showTotalAPagarLabel.setText("$ 0.0");

        buttonAceptar.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        textFieldRUTCosechador.addActionListener(e -> buscarDeuda());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
    }

    private void buscarDeuda() {
        try {
            Rut rut = Rut.of(textFieldRUTCosechador.getText());

            String[] listado = cp.listPesajesCosechador(rut);

            tableModel.setRowCount(0); // Limpia la tabla anterior
            double total = 0;
            boolean hayDeuda = false;

            for (String linea : listado) {
                // Intentemos usar regex para tolerar los datos que vengan con o sin espacios
                String[] datos = linea.split("\\s*;\\s*");

                if (datos.length > 6 && datos[6].trim().equalsIgnoreCase("Impago")) {

                    tableModel.addRow(datos);

                    try {
                        total += Double.parseDouble(datos[5].trim().replace(",", "."));
                    } catch (NumberFormatException e) {
                        System.err.println("Error al leer monto: " + datos[5]);
                    }
                    hayDeuda = true;
                }
            }

            showTotalAPagarLabel.setText(String.format("$ %,.1f", total));

            if (hayDeuda) {
                buttonAceptar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "El cosechador no tiene pesajes pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                buttonAceptar.setEnabled(false);
            }

        } catch (GestionHuertosException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "RUT inválido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOK() {
        try {
            int idPago = Integer.parseInt(textFieldIDPago.getText());
            Rut rut = Rut.of(textFieldRUTCosechador.getText());

            double montoPagado = cp.addPagoPesaje(idPago, rut);

            JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.\nMonto total: $" + montoPagado);
            dispose();
        } catch (GestionHuertosException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de pago debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    // Main sólo para pruebas individuales
    public static void main(String[] args) {
        try {
            ControladorProduccion.getInstance().readDataFromTextFile();
            System.out.println("Datos cargados para prueba.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        PagarPesajesImpagosACosechadorDialog dialog = new PagarPesajesImpagosACosechadorDialog();
        dialog.setVisible(true);
        System.exit(0);
    }
}