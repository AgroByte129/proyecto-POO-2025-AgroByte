package vista;

import controlador.ControladorProduccion;
import utilidades.GestionHuertosException;

import javax.swing.*;
import java.awt.*;
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
        setTitle("Crear Cultivo");
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass()
                        .getResource("/vista/icons/32x32/agregar-archivo.png")
                )
        );
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
    }

    private void onOK() {

        try {

            GUIHelper.validarNoVacio(textFieldID.getText(), "ID");
            GUIHelper.validarNoVacio(textFieldEspecie.getText(), "Especie");
            GUIHelper.validarNoVacio(textFieldVariedad.getText(), "Variedad");
            GUIHelper.validarNoVacio(textFieldRendimiento.getText(), "Rendimiento");

            int id = Integer.parseInt(textFieldID.getText().trim());

            float rendimiento = Float.parseFloat(textFieldRendimiento.getText().trim().replace(",", "."));

            String especie = textFieldEspecie.getText().trim();
            String variedad = textFieldVariedad.getText().trim();

            cp.createCultivo(id, especie, variedad, rendimiento);

            GUIMsg.info("Cultivo creado exitosamente.");
            dispose();

        } catch (NumberFormatException e) {
            GUIMsg.error("El ID debe ser entero y el Rendimiento un número decimal válido.");
        } catch (GestionHuertosException e) {
            GUIMsg.error(e.getMessage());
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void display() {
        CreacionDeCultivoDialog dialog = new CreacionDeCultivoDialog();
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
    }
}
