package vista;

import controlador.ControladorProduccion;
import utilidades.EstadoPlan;
import utilidades.GestionHuertosException;

import javax.swing.*;
import java.awt.event.*;

public class CambioDeEstadoPlanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
//comentario para commit correcciones
    private JLabel cambioDeEstadoPlanLabel;
    private JLabel nombreLabel;
    private JLabel cumplimientoDeMetaLabel;
    private JLabel estadoActualLabel;
    private JLabel IDPlanLabel;
    private JLabel nuevoEstadoLabel;
    private JTextField textFieldID;
    private JComboBox<EstadoPlan> comboBoxNEstado;
    private JLabel showNombreLabel;
    private JLabel showCumplimientoMetaLabel;
    private JLabel showEstadoActualLabel;

    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public CambioDeEstadoPlanDialog() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        comboBoxNEstado.setModel(new DefaultComboBoxModel<>(EstadoPlan.values()));

        buttonOK.setEnabled(false);
        comboBoxNEstado.setEnabled(false);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        textFieldID.addActionListener(e -> buscarPlan());

        pack();
        setLocationRelativeTo(null);
    }

    private void buscarPlan() {
        try {
            int idBuscado = Integer.parseInt(textFieldID.getText().trim());
            String[] listado = cp.listPlanesCosecha();
            boolean encontrado = false;


            for (String linea : listado) {
                String[] datos = linea.split(";\\s*");
                int idActual = Integer.parseInt(datos[0].trim());

                if (idActual == idBuscado) {

                    showNombreLabel.setText(datos[1].trim());
                    showEstadoActualLabel.setText(datos[6].trim());
                    showCumplimientoMetaLabel.setText(datos[10].trim() + "%");

                    comboBoxNEstado.setEnabled(true);
                    buttonOK.setEnabled(true);

                    try {
                        comboBoxNEstado.setSelectedItem(EstadoPlan.valueOf(datos[6].trim()));
                    } catch (Exception ex) {/*Ignorar si hay error de formato*/}

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "Plan no encontrado con ID: " + idBuscado, "Error", JOptionPane.ERROR_MESSAGE);
                limpiar();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        showNombreLabel.setText("-");
        showEstadoActualLabel.setText("-");
        showCumplimientoMetaLabel.setText("-");
        buttonOK.setEnabled(false);
        comboBoxNEstado.setEnabled(false);
    }

    private void onOK() {
        try {
            int id = Integer.parseInt(textFieldID.getText());
            EstadoPlan nuevoEstado = (EstadoPlan) comboBoxNEstado.getSelectedItem();

            cp.changeEstadoPlan(id, nuevoEstado);

            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.");
            dispose();
        } catch (GestionHuertosException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el formato del ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {

        // CÓDIGO AGREGADO PARA PRUEBAS
        try {
            ControladorProduccion.getInstance().readDataFromTextFile();
            System.out.println("Datos cargados correctamente para la prueba.");
        } catch (Exception e) {
            System.err.println("Error cargando datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
        // --------

        CambioDeEstadoPlanDialog dialog = new CambioDeEstadoPlanDialog();
        dialog.setVisible(true);
        System.exit(0);
    }
}