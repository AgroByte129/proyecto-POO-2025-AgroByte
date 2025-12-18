package vista;

import controlador.ControladorProduccion;
import utilidades.EstadoPlan;
import utilidades.GestionHuertosException;

import javax.swing.*;
import java.awt.event.*;

//Arturo Felipe Gómez Senn

public class CambioDeEstadoPlanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

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

        buttonOK.setEnabled(false);// Deshabilitado hasta encontrar un plan
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

        showNombreLabel.setText("-");
        showCumplimientoMetaLabel.setText("-");
        showEstadoActualLabel.setText("-");

        pack();
        setLocationRelativeTo(null);
    }

    private void buscarPlan() {
        try {

            GUIHelper.validarNoVacio(textFieldID.getText(), "ID Plan");

            int idBuscado = Integer.parseInt(textFieldID.getText().trim());

            String[] listado = cp.listPlanesCosecha();
            boolean encontrado = false;


            for (String linea : listado) {
                // Intentemos usar regex para tolerar los datos que vengan con o sin espacios
                String[] datos = linea.split("\\s*;\\s*");
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
                GUIMsg.error("Plan no encontrado con ID: " + idBuscado);
                limpiar();
            }

        } catch (NumberFormatException ex) {
            GUIMsg.error("El ID debe ser numérico");
        }catch (Exception ex) {
            GUIMsg.error(ex.getMessage());
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

            GUIMsg.info("Estado actualizado correctamente.");
            dispose();
        } catch (Exception e) {
            GUIMsg.error(e.getMessage());
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void display() {
        CambioDeEstadoPlanDialog dialog = new CambioDeEstadoPlanDialog();
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
    }

}