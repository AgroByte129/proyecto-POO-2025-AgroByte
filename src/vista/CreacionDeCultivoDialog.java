package vista;

import controlador.ControladorProduccion;
import utilidades.GestionHuertosException;

import javax.swing.*;
import java.awt.event.*;

//Arturo Gómez Senn

public class CreacionDeCultivoDialog extends JDialog {
    private JPanel contentPane;
    private JButton aceptarButton;
    private JButton buttonCancel;
    private JLabel creaciónDeCultivoLabel;
    private JTextField textFieldID;
    private JTextField textFieldEspecie;
    private JTextField textFieldVariedad;
    private JTextField textFieldRendimiento;
    private JLabel IDLabel;
    private JLabel especieLabel;
    private JLabel variedadLabel;
    private JLabel rendimientoLabel;

    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public CreacionDeCultivoDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(aceptarButton);


        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    private void onOK() {

        try {

            if (textFieldEspecie.getText().isBlank() || textFieldVariedad.getText().isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Debe completar Especie y Variedad.",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(textFieldID.getText().trim());

            float rendimiento = Float.parseFloat(textFieldRendimiento.getText().trim().replace(",", "."));

            String especie = textFieldEspecie.getText().trim();
            String variedad = textFieldVariedad.getText().trim();

            cp.createCultivo(id, especie, variedad, rendimiento);

            JOptionPane.showMessageDialog(this, "Cultivo creado exitosamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un entero y el Rendimiento un número decimal válido.",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (GestionHuertosException e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        try {
            ControladorProduccion.getInstance().readDataFromTextFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CreacionDeCultivoDialog dialog = new CreacionDeCultivoDialog();
        dialog.setVisible(true);
        System.exit(0);
    }
}
