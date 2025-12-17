package vista;

import controlador.ControladorProduccion;
import utilidades.Calidad;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;

//Diego Jara Betancourt y Arturo Gómez Senn

public class AgregarPesajeCosechador extends JDialog {
    private JPanel contentPane;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JLabel agregaciónDePesajeCosechadorLabel;
    private JTextField textFieldIDPesaje;
    private JComboBox <String> comboBoxCosechador;
    private JComboBox <String> comboBoxCuadrilla;
    private JComboBox <Calidad> comboBoxCalidad;
    private JTextField textFieldCantidadKilos;
    private JLabel IDPesajeLabel;
    private JLabel cosechadorLabel;
    private JLabel cuadrillaLabel;
    private JLabel cantidadKilosLabel;
    private JLabel calidadLabel;

    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public AgregarPesajeCosechador() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(aceptarButton);

        cargarCosechadores();
        cargarCalidades();

        comboBoxCosechador.addActionListener(e -> cargarCuadrillasDelCosechador());

        if (comboBoxCosechador.getItemCount() > 0) {
            comboBoxCosechador.setSelectedIndex(0);
        }

        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    private void cargarCalidades() {
        comboBoxCalidad.setModel(new DefaultComboBoxModel<>(Calidad.values()));
    }

    private void cargarCosechadores() {
        comboBoxCosechador.removeAllItems();
        String[] lista = cp.listCosechadores();
        for (String c : lista) {
            comboBoxCosechador.addItem(c);
        }
    }

    private void cargarCuadrillasDelCosechador() {
        comboBoxCuadrilla.removeAllItems();

        String seleccion = (String) comboBoxCosechador.getSelectedItem();
        if (seleccion == null) return;

        try {

            String[] partes = seleccion.split(";");
            Rut rut = Rut.of(partes[0].trim());

            String[] cuadrillas = cp.getCuadrillasDeCosechadorDePlan(rut);

            for (String cuad : cuadrillas) {
                comboBoxCuadrilla.addItem(cuad);
            }
        } catch (GestionHuertosException e) {
            // Si el cosechador no tiene cuadrillas activas, el combo queda vacío
            // No es necesario mostrar error intrusivo aquí, solo dejarlo vacío
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOK() {
        // add your code here
        try {

            if (textFieldIDPesaje.getText().isBlank() || textFieldCantidadKilos.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Complete ID y Kilos.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (comboBoxCosechador.getSelectedItem() == null || comboBoxCuadrilla.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar Cosechador y Cuadrilla válida.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPesaje = Integer.parseInt(textFieldIDPesaje.getText().trim());
            float kilos = Float.parseFloat(textFieldCantidadKilos.getText().trim().replace(",", "."));
            Calidad calidad = (Calidad) comboBoxCalidad.getSelectedItem();

            String seleccionCosechador = (String) comboBoxCosechador.getSelectedItem();
            Rut rutCosechador = Rut.of(seleccionCosechador.split(";")[0].trim());

            String seleccionCuadrilla = (String) comboBoxCuadrilla.getSelectedItem();
            String[] datosCuadrilla = seleccionCuadrilla.split(";");

            int idCuadrilla = Integer.parseInt(datosCuadrilla[0].trim());
            int idPlan = Integer.parseInt(datosCuadrilla[2].trim());

            cp.addPesaje(idPesaje, rutCosechador, idPlan, idCuadrilla, kilos, calidad);

            JOptionPane.showMessageDialog(this, "Pesaje agregado exitosamente.");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID debe ser entero y Kilos número decimal.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (GestionHuertosException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado al procesar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        try {
            ControladorProduccion.getInstance().readDataFromTextFile();
        } catch (Exception e) {}

        AgregarPesajeCosechador dialog = new AgregarPesajeCosechador();
        dialog.setVisible(true);
        System.exit(0);
    }

    public static void display() {
        AgregarPesajeCosechador dialog = new AgregarPesajeCosechador();
        dialog.setVisible(true);
    }
}
